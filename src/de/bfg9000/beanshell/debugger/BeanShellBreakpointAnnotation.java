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
import org.netbeans.spi.debugger.ui.BreakpointAnnotation;
import org.openide.text.Annotatable;

/**
 * Provides a default annotation for showing breakpoints in an editor. Based on the class 
 * {@code org.sodbeans.debugger.hop.HopBreakpointAnnotation} from the SodBeans project that has been written by Andreas
 * Stefik.
 * 
 * @see http://sourceforge.net/projects/sodbeans/ 
 * @author Thomas Werner
 */
public class BeanShellBreakpointAnnotation extends BreakpointAnnotation{

    private final BeanShellBreakpoint breakpoint;

    public BeanShellBreakpointAnnotation(final Annotatable a, final BeanShellBreakpoint b) {
        breakpoint = b;
        attach(a);
    }
    
    @Override
    public String getAnnotationType() {
        return "Breakpoint";
    }

    @Override
    public String getShortDescription() {
        return "Breakpoint";
    }

    @Override
    public Breakpoint getBreakpoint() {
        return breakpoint;
    }
    
}
