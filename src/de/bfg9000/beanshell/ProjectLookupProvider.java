/*
 * nbBeanShell -- a integration of BeanScript into the NetBeans IDE
 * Copyright (C) 2011 Thomas Werner
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.bfg9000.beanshell;

import org.netbeans.spi.project.ActionProvider;
import org.netbeans.spi.project.LookupProvider;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;

/**
 * Adds support for the "Run File (Shift-F6)" Action for BeanShell scripts to J2SE projects.
 *
 * @author Thomas Werner
 */
@LookupProvider.Registration(projectType = "org-netbeans-modules-java-j2seproject")
public class ProjectLookupProvider implements LookupProvider {

    @Override
    public Lookup createAdditionalLookup(Lookup lookup) {
        return Lookups.fixed(new BeanShellActionProvider(lookup.lookup(ActionProvider.class)));
    }

    private static class BeanShellActionProvider implements ActionProvider {

        private final ActionProvider wrappedActionProvider;

        public BeanShellActionProvider(ActionProvider wrapped) {
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

            final RunAction runAction = new RunAction(context.lookup(BeanShellDataObject.class));
            runAction.actionPerformed(null);
        }

        @Override
        public boolean isActionEnabled(String command, Lookup context) throws IllegalArgumentException {
            final boolean wrappedResult = wrappedActionProvider.isActionEnabled(command, context);
            return wrappedResult || (null != context.lookup(BeanShellDataObject.class));
        }

    }

}
