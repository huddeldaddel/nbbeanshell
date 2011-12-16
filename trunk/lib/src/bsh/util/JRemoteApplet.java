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
 *  You should have received a copy of the GNU Lesser General Public License along with this program.                  *
 *  If not, see <http://www.gnu.org/licenses/>.                                                                        *
 *                                                                                                                     *
 *  Patrick Niemeyer (pat@pat.net)                                                                                     *
 *  Author of Learning Java, O'Reilly & Associates                                                                     *
 *  http://www.pat.net/~pat/                                                                                           *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Label;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import javax.swing.JApplet;

/**
 * A lightweight console applet for remote display of a Beanshell session.
 */
public class JRemoteApplet extends JApplet {

    OutputStream out;
    InputStream in;

    public void init() {
        getContentPane().setLayout(new BorderLayout());

        try {
            URL base = getDocumentBase();

            // connect to session server on port (httpd + 1)
            Socket s = new Socket(base.getHost(), base.getPort() + 1);
            out = s.getOutputStream();
            in = s.getInputStream();
        } catch (IOException e) {
            getContentPane().add("Center",
                    new Label("Remote Connection Failed", Label.CENTER));
            return;
        }

        Component console = new JConsole(in, out);
        getContentPane().add("Center", console);
    }
}
