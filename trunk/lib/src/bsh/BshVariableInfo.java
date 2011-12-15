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
 *  You should have received a copy of the GNU General Public License along with this program.                         *
 *  If not, see <http://www.gnu.org/licenses/>.                                                                        *
 *                                                                                                                     *
 *  Thomas Werner                                                                                                      *
 *  http://www.xing.com/profile/Thomas_Werner108                                                                       *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Thomas Werner
 */
public class BshVariableInfo {
    
    private Set<BshModifierInfo> modifiers;
    private String name;
    private String type;
    private int lineNumber;

    public BshVariableInfo() {
        modifiers = new HashSet<BshModifierInfo>();
    }
    
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    public Set<BshModifierInfo> getModifiers() {
        return Collections.unmodifiableSet(modifiers);
    }

    public void addModifier(BshModifierInfo modifier) {
        modifiers.add(modifier);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
}
