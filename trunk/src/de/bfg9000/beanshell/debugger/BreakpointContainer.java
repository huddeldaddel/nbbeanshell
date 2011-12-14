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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.openide.filesystems.FileObject;

/**
 *
 * @author Thomas Werner
 */
public enum BreakpointContainer {
    
    Instance;
    
    private final Map<FileObject, Set<Integer>> breakpoints = new HashMap<FileObject, Set<Integer>>();
    
    public void toggeBreakpoint(int lineNumber, FileObject file) {
        Set<Integer> lines = breakpoints.get(file);
        if(null == lines) {
            lines = new TreeSet<Integer>();
            lines.add(lineNumber);
            breakpoints.put(file, lines);
        } else if(lines.contains(lineNumber)) {
            lines.remove(lineNumber);
        } else {
            lines.add(lineNumber);
        }
    }
    
    public boolean isBreakpoint(int lineNumber, FileObject file) {
        final Set<Integer> lines = breakpoints.get(file);
        return (null != lines) && lines.contains(lineNumber);
    }
    
}
