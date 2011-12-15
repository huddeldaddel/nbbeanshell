/*********************************************************************************************************************** 
 *  nbBeanShell -- a integration of BeanShell into the NetBeans IDE.                                                    *
 *  Copyright (C) 2011 Thomas Werner                                                                                   *
 *                                                                                                                     *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public  *
 *  License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any     *
 *  later version.                                                                                                     *
 *                                                                                                                     *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more       *
 *  details.                                                                                                           *
 *                                                                                                                     *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see            *
 *  <http://www.gnu.org/licenses/>.                                                                                    *
 **********************************************************************************************************************/
package de.bfg9000.beanshell.debugger;

import bsh.BreakpointProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.openide.filesystems.FileObject;

/**
 * A (singleton) container that contains all BeanShell breakpoints.
 * 
 * @author Thomas Werner
 */
public enum BreakpointContainer implements BreakpointProvider {
    
    Instance;
    
    private final Map<String, Set<Integer>> breakpoints = new HashMap<String, Set<Integer>>();
    
    public void toggeBreakpoint(int lineNumber, FileObject file) {
        Set<Integer> lines = breakpoints.get(file);
        if(null == lines) {
            lines = new TreeSet<Integer>();
            lines.add(lineNumber);
            breakpoints.put(file.getPath(), lines);
        } else if(lines.contains(lineNumber)) {
            lines.remove(lineNumber);
        } else {
            lines.add(lineNumber);
        }
    }
    
    @Override
    public boolean isBreakpoint(int lineNumber, String filePath) {
        final Set<Integer> lines = breakpoints.get(filePath);
        return (null != lines) && lines.contains(lineNumber);
    }
    
}
