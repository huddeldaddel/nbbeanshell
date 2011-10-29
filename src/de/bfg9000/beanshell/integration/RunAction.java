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
package de.bfg9000.beanshell.integration;

import bsh.Interpreter;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Writer;

import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

/**
 * Action that runs the current script file.
 *
 * @author Thomas Werner
 */
public final class RunAction implements ActionListener {

    private static final long serialVersionUID = -4861693586138919610L;

    private final BeanShellDataObject context;

    public RunAction(BeanShellDataObject context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        final InputOutput io = IOProvider.getDefault().getIO("BeanShell", false);
        io.select();

        try {
            io.getOut().reset();

            Interpreter interpreter = new Interpreter();
            interpreter.setErr(new PrintStream(new WriterOutputStream(io.getErr())));
            interpreter.setOut(new PrintStream(new WriterOutputStream(io.getOut())));
            interpreter.eval(context.getPrimaryFile().asText());
        } catch(Exception ex) {
            ex.printStackTrace(io.getErr());
        } finally {
            io.getErr().close();
            io.getOut().close();
        }
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
