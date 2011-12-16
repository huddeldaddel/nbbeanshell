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

import bsh.Interpreter;
import bsh.NameSpace;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * BeanShell remote session server. Starts instances of bsh for client connections. Note: the sessiond effectively maps
 * all connections to the same interpreter (shared namespace).
 */
public class Sessiond extends Thread {

    private ServerSocket ss;
    NameSpace globalNameSpace;

    /*
     * public static void main(String argv[]) throws IOException { new Sessiond( Integer.parseInt(argv[0])).start(); }
     */
    public Sessiond(NameSpace globalNameSpace, int port) throws IOException {
        ss = new ServerSocket(port);
        this.globalNameSpace = globalNameSpace;
    }

    public void run() {
        try {
            while (true) {
                new SessiondConnection(globalNameSpace, ss.accept()).start();
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

class SessiondConnection extends Thread {

    NameSpace globalNameSpace;
    Socket client;

    SessiondConnection(NameSpace globalNameSpace, Socket client) {
        this.client = client;
        this.globalNameSpace = globalNameSpace;
    }

    public void run() {
        try {
            InputStream in = client.getInputStream();
            PrintStream out = new PrintStream(client.getOutputStream());
            Interpreter i = new Interpreter(
                    new InputStreamReader(in), out, out, true, globalNameSpace);
            i.setExitOnEOF(false); // don't exit interp
            i.run();
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}
