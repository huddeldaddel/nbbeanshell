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
package bsh;

import java.io.FilterReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * This is a quick hack to turn empty lines entered interactively on the command line into ';\n' empty lines for the
 * interpreter. It's just more pleasant to be able to hit return on an empty line and see the prompt reappear.
 *
 * This is *not* used when text is sourced from a file non-interactively.
 */
class CommandLineReader extends FilterReader {

    public CommandLineReader(Reader in) {
        super(in);
    }
    static final int normal = 0,
            lastCharNL = 1,
            sentSemi = 2;
    int state = lastCharNL;

    @Override
    public int read() throws IOException {
        int b;

        if (state == sentSemi) {
            state = lastCharNL;
            return '\n';
        }

        // skip CR
        while ((b = in.read()) == '\r');

        if (b == '\n') {
            if (state == lastCharNL) {
                b = ';';
                state = sentSemi;
            } else {
                state = lastCharNL;
            }
        } else {
            state = normal;
        }

        return b;
    }

    /**
     * This is a degenerate implementation. I don't know how to keep this from blocking if we try to read more than one
     * char... There is no available() for Readers ??
     */
    @Override
    public int read(char buff[], int off, int len) throws IOException {
        int b = read();
        if (b == -1) {
            return -1;  // EOF, not zero read apparently
        } else {
            buff[off] = (char) b;
            return 1;
        }
    }

    // Test it
    public static void main(String[] args) throws Exception {
        Reader in = new CommandLineReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println(in.read());
        }

    }
}
