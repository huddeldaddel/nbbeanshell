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

import javax.swing.JComponent;
import org.netbeans.spi.debugger.ui.BreakpointType;

/**
 * Based on the class {@code org.sodbeans.debugger.hop.HopLineBreakpointType} from the SodBeans project that has been 
 * written by Andreas Stefik.
 * 
 * @see http://sourceforge.net/projects/sodbeans/ 
 * @author Thomas Werner
 */
public class BeanShellLineBreakpointType extends BreakpointType {

    @Override
    public String getCategoryDisplayName() {
        return "BeanShell";
    }

    @Override
    public JComponent getCustomizer() {
        return null;
    }

    @Override
    public String getTypeDisplayName() {
        return "BeanShellSourceLine";
      }

    @Override
    public boolean isDefault() {
        return true;
    }

}
