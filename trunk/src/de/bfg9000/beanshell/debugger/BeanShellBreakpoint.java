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

import org.netbeans.api.debugger.Breakpoint;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;

/**
 * A Breakpoint for BeanShell scripts. Based on the class {@code org.sodbeans.debugger.hop.HopBreakpoint} from the 
 * SodBeans project that has been written by "jojobubu".
 * 
 * @see http://sourceforge.net/projects/sodbeans/ 
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
