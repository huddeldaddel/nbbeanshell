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
package de.bfg9000.beanshell.debugger;

import org.netbeans.api.debugger.Breakpoint;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;

/**
 *
 * @author Thomas Werner
 */
public class BeanShellBreakpoint extends Breakpoint {
    
    private boolean enabled = true;
    private Line line;
    private FileObject file;

    public BeanShellBreakpoint(Line line, FileObject fo) {
        this.line = line;
        file = fo;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void disable() { 
        enabled = false;
    }

    @Override
    public void enable() { 
        enabled = true;
    }

    /**
     * @return a representation of what line this breakpoint is on.
     */
    public Line getLine() {
        return line;
    }

    /**
     * @return a representation of what file this breakpoint is in.
     */
    public FileObject getFileObject() {
        return file;
    }
    
}
