/*********************************************************************************************************************** 
 *  nbBeanShell -- a integration of BeanShell into the NetBeans IDE.                                                    *
 *  Copyright (C) 2011 Thomas Werner                                                                                   *
 *                                                                                                                     *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public  *
 *  License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any     *
 *  later version.                                                                                                     *
 *                                                                                                                     *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more       *
 *  details.                                                                                                           *
 *                                                                                                                     *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see            *
 *  <http://www.gnu.org/licenses/>.                                                                                    *
 **********************************************************************************************************************/
package de.bfg9000.beanshell.integration;

import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Adds support for various actions related to BeanShell scripts to J2SE projects.
 *
 * @author Thomas Werner
 */
@LookupProvider.Registration(projectType = "org-netbeans-modules-java-j2seproject")
public class ProjectLookupProvider implements LookupProvider {

    @Override
    public Lookup createAdditionalLookup(Lookup lookup) {
        final ActionProvider actionProvider = lookup.lookup(ActionProvider.class);
        if(null != actionProvider)
            return Lookups.fixed(new WrappingActionProvider(actionProvider));
        
        return Lookups.fixed(new StandaloneCompileActionProvider(),
                             new StandaloneDebugActionProvider(),
                             new StandaloneRunActionProvider());
    }

    /**
     * This class adds the actions to the predefined actions of the project. It therefore wrapps the existing action
     * provider of the project. This setup is the preferred way up to NetBeans version 7.0.
     */
    private static class WrappingActionProvider implements ActionProvider {

        private final ActionProvider wrappedActionProvider;

        public WrappingActionProvider(ActionProvider wrapped) {
            wrappedActionProvider = wrapped;            
        }

        @Override
        public String[] getSupportedActions() {
            return wrappedActionProvider.getSupportedActions();
        }

        @Override
        public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
            if(null == context.lookup(BeanShellDataObject.class)) {
                wrappedActionProvider.invokeAction(command, context);
                return;
            }

            if(ActionProvider.COMMAND_COMPILE_SINGLE.equals(command))
                new Thread(new CompileAction(context.lookup(BeanShellDataObject.class))).start();
            
            if(ActionProvider.COMMAND_DEBUG_SINGLE.equals(command))
                new Thread(new DebugAction(context.lookup(BeanShellDataObject.class))).start();
            
            if(ActionProvider.COMMAND_RUN_SINGLE.equals(command))
                new Thread(new RunAction(context.lookup(BeanShellDataObject.class))).start();
        }

        @Override
        public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
            final boolean wrappedResult = wrappedActionProvider.isActionEnabled(command, context);
            return wrappedResult || (null != context.lookup(BeanShellDataObject.class));
        }

    }
    
    /**
     * This very basic setup is the preferred way since NetBeans version 7.1.
     */
    private static abstract class AbstractBeanShellActionProvider implements ActionProvider {
        
        private final String command;
        
        public AbstractBeanShellActionProvider(String command) {
            this.command = command;
        }
        
        @Override
        public String[] getSupportedActions() {
            return new String[] {command};
        }

        protected abstract AbstractAction getAction(Lookup context);
        
        @Override
        public void invokeAction(String command, Lookup context) throws IllegalArgumentException {
            if((null != context.lookup(BeanShellDataObject.class)) && command.equals(command))
                new Thread(getAction(context)).start();
        }

        @Override
        public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
            return null != context.lookup(BeanShellDataObject.class);
        }
        
    }
    
    /**
     * This class provides the "run single" action.
     */
    private static final class StandaloneRunActionProvider extends AbstractBeanShellActionProvider {

        public StandaloneRunActionProvider() {
            super(ActionProvider.COMMAND_RUN_SINGLE);
        }
        
        @Override
        protected AbstractAction getAction(Lookup context) {
            return new RunAction(context.lookup(BeanShellDataObject.class));
        }

    }
    
    /**
     * This class provides the "compile single" action.
     */
    private static final class StandaloneCompileActionProvider extends AbstractBeanShellActionProvider {

        public StandaloneCompileActionProvider() {
            super(ActionProvider.COMMAND_COMPILE_SINGLE);
        }
        
        @Override
        protected AbstractAction getAction(Lookup context) {
            return new CompileAction(context.lookup(BeanShellDataObject.class));
        }

    }
    
    /**
     * This class provides the "debug single" action.
     */
    private static final class StandaloneDebugActionProvider extends AbstractBeanShellActionProvider {

        public StandaloneDebugActionProvider() {
            super(ActionProvider.COMMAND_DEBUG_SINGLE);
        }
        
        @Override
        protected AbstractAction getAction(Lookup context) {
            return new DebugAction(context.lookup(BeanShellDataObject.class));
        }

    }

}
