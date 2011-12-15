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

import bsh.Parser;
import java.io.IOException;
import java.io.InputStream;
import org.openide.windows.InputOutput;

/**
 * Action that parses the current script file. BeanShell scripts are not compiled. So this action just uses the Parser
 * to find syntax errors.
 *
 * @author Thomas Werner
 */
class CompileAction extends AbstractAction {

    public CompileAction(BeanShellDataObject context) {
        super(context);
    }

    @Override
    protected void perform(InputOutput io) {
        InputStream iStream = null;
        
        try {
            saveScriptDocument();
            
            iStream = context.getPrimaryFile().getInputStream();
            final Parser parser = new Parser(iStream);
            parser.setRetainComments(true);
            
            io.getOut().println("Parsing BeanShell file: " +context.getPrimaryFile().getPath());
            try {
                while(!parser.Line())
                    io.getOut().println(parser.popNode());
                io.getOut().println("Finished parsing. No errors have been found!");
            } catch(Error err) {
                io.getErr().println(err.getMessage());
            }
        } catch(Exception ex) {
            ex.printStackTrace(io.getErr());
        } finally {
            try {
                iStream.close();
            } catch (IOException ex) { }
        }
    }
    
    @Override
    protected String getTaskName() {
        return "Compile " +context.getPrimaryFile().getName();
    }
    
}
