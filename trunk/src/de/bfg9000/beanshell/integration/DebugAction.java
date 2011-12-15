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

import bsh.Interpreter;
import de.bfg9000.beanshell.debugger.Debugger;
import de.bfg9000.beanshell.util.WriterOutputStream;
import java.io.PrintStream;
import org.openide.windows.InputOutput;

/**
 * Action that starts the BeanShell debugger.
 * 
 * @author Thomas Werner
 */
public class DebugAction extends AbstractAction {

    public DebugAction(BeanShellDataObject context) {
        super(context);
    }
    
    @Override
    protected void perform(InputOutput io) throws Throwable {
        try {
            saveScriptDocument();
            Debugger.startDebugger();
        } catch(Exception ex) {
            ex.printStackTrace(io.getErr());
        }        
    }

    @Override
    protected String getTaskName() {
        return "Debug " +context.getPrimaryFile().getName();
    }
    
}
