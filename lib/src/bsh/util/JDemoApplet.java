/***********************************************************************************************************************
 *                                                                                                                     *
 *  This file is part of the BeanShell Java Scripting distribution.                                                    *
 *  Documentation and updates may be found at http://www.beanshell.org/                                                *
 *                                                                                                                     *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General  *
 *  Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option)  *
 *  any later version.                                                                                                 *
 *                                                                                                                     *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for    *
 *  more details.                                                                                                      *
 *                                                                                                                     *
 *  You should have received a copy of the GNU General Public License along with this program.                         *
 *  If not, see <http://www.gnu.org/licenses/>.                                                                        *
 *                                                                                                                     *
 *  Patrick Niemeyer (pat@pat.net)                                                                                     *
 *  Author of Learning Java, O'Reilly & Associates                                                                     *
 *  http://www.pat.net/~pat/                                                                                           *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh.util;

import bsh.EvalError;
import bsh.Interpreter;
import bsh.TargetError;
import java.awt.BorderLayout;
import javax.swing.JApplet;

/**
 * Run bsh as an applet for demo purposes.
 */
public class JDemoApplet extends JApplet {

    public void init() {
        String debug = getParameter("debug");
        if (debug != null && debug.equals("true")) {
            Interpreter.DEBUG = true;
        }

        String type = getParameter("type");
        if (type != null && type.equals("desktop")) // start the desktop
        {
            try {
                new Interpreter().eval("desktop()");
            } catch (TargetError te) {
                te.printStackTrace();
                System.out.println(te.getTarget());
                te.getTarget().printStackTrace();
            } catch (EvalError evalError) {
                System.out.println(evalError);
                evalError.printStackTrace();
            }
        } else {
            getContentPane().setLayout(new BorderLayout());
            JConsole console = new JConsole();
            getContentPane().add("Center", console);
            Interpreter interpreter = new Interpreter(console);
            new Thread(interpreter).start();
        }
    }
}
