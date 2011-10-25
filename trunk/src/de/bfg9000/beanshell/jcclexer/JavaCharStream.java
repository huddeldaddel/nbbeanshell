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
package de.bfg9000.beanshell.jcclexer;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import org.netbeans.spi.lexer.LexerInput;

public class JavaCharStream {

    private LexerInput input;

    static boolean staticFlag;

    public JavaCharStream (LexerInput input) {
        this.input = input;
    }

    JavaCharStream(Reader stream, int i, int i0) {
        throw new UnsupportedOperationException ("Not yet implemented");
    }

    JavaCharStream(InputStream stream, String encoding, int i, int i0) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException ("Not yet implemented");
    }

    char BeginToken() throws IOException {
        return readChar();
    }

    String GetImage () {
        return input.readText ().toString ();
    }

     public char[] GetSuffix (int len) {
        if (len > input.readLength ())
            throw new IllegalArgumentException ();
        return input.readText (input.readLength () - len, input.readLength ()).toString ().toCharArray ();
     }

    void ReInit (Reader stream, int i, int i0) {
        throw new UnsupportedOperationException ("Not yet implemented");
    }

    void ReInit (InputStream stream, String encoding, int i, int i0) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException ("Not yet implemented");
    }

    void backup (int i) {
        input.backup (i);
    }

    int getBeginColumn () {
        return 0;
    }

    int getBeginLine () {
        return 0;
    }

    int getEndColumn () {
        return 0;
    }

    int getEndLine () {
        return 0;
    }

    char readChar () throws IOException {
        final int result = input.read();
        if(result == LexerInput.EOF)
            throw new IOException("LexerInput EOF");

        return (char) result;
    }
}

