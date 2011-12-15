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

import bsh.Interpreter;
import java.awt.*;

/**
 * Misc utilities for the bsh.util package. Nothing in the core language (bsh package) should depend on this. Note: that
 * promise is currently broken... fix it.
 */
public class Util {
    /*
     * public static ConsoleInterface makeConsole() { if ( bsh.Capabilities.haveSwing() ) return new JConsole(); else
     * return new AWTConsole(); }
     */

    static Window splashScreen;
    /*
     * This could live in the desktop script. However we'd like to get it on the screen as quickly as possible.
     */

    public static void startSplashScreen() {
        int width = 275, height = 148;
        Window win = new Window(new Frame());
        win.pack();
        BshCanvas can = new BshCanvas();
        can.setSize(width, height); // why is this necessary?
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension dim = tk.getScreenSize();
        win.setBounds(
                dim.width / 2 - width / 2, dim.height / 2 - height / 2, width, height);
        win.add("Center", can);
        Image img = tk.getImage(
                Interpreter.class.getResource("/bsh/util/lib/splash.gif"));
        MediaTracker mt = new MediaTracker(can);
        mt.addImage(img, 0);
        try {
            mt.waitForAll();
        } catch (Exception e) {
        }
        Graphics gr = can.getBufferedGraphics();
        gr.drawImage(img, 0, 0, can);
        win.setVisible(true);
        win.toFront();
        splashScreen = win;
    }

    public static void endSplashScreen() {
        if (splashScreen != null) {
            splashScreen.dispose();
        }
    }
}
