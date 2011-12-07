/*
 * nbBeanShell -- a integration of BeanShell into the NetBeans IDE
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
package de.bfg9000.beanshell.integration;

import bsh.Interpreter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;
import org.openide.windows.InputOutput;

/**
 * Action that runs the current script file.
 *
 * @author Thomas Werner
 */
class RunAction extends AbstractAction {

    public RunAction(BeanShellDataObject context) {
        super(context);
    }

    @Override
    protected boolean perform(InputOutput io) {        
        try {
            saveScriptDocument();
                        
            Interpreter interpreter = new Interpreter();
            interpreter.setErr(new PrintStream(new WriterOutputStream(io.getErr())));
            interpreter.setOut(new PrintStream(new WriterOutputStream(io.getOut())));
            interpreter.eval(context.getPrimaryFile().asText());
            return true;
        } catch(Exception ex) {
            ex.printStackTrace(io.getErr());
            return false;
        }
    }    
    
    @Override
    protected String getTaskName() {
        return "Run " +context.getPrimaryFile().getName();
    }
    
    /**
     * Adapter that connects a Writer to an OutputStream.
     */
    private static final class WriterOutputStream extends OutputStream {

        private final Writer writer;

        public WriterOutputStream(Writer writer) {
            this.writer = writer;
        }

        @Override
        public void write(int b) throws IOException {
            write(new byte[] {(byte) b}, 0, 1);
        }

        @Override
        public void write(byte b[], int off, int len) throws IOException {
            writer.write(new String(b, off, len));
        }

        @Override
        public void flush() throws IOException {
            writer.flush();
        }

        @Override
        public void close() throws IOException {
            writer.close();
        }
    }

}
