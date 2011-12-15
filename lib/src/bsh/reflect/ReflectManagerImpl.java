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
 *  Patrick Niemeyer (pat@pat.net)                                                                                     *
 *  Author of Learning Java, O'Reilly & Associates                                                                     *
 *  http://www.pat.net/~pat/                                                                                           *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh.reflect;

import bsh.ReflectManager;
import java.lang.reflect.AccessibleObject;

/**
 * This is the implementation of: ReflectManager - a dynamically loaded extension that supports extended reflection
 * features supported by JDK1.2 and greater.
 *
 * In particular it currently supports accessible method and field access supported by JDK1.2 and greater.
 */
public class ReflectManagerImpl extends ReflectManager {

    /**
     * Set a java.lang.reflect Field, Method, Constructor, or Array of accessible objects to accessible mode. If the
     * object is not an AccessibleObject then do nothing.
     *
     * @return true if the object was accessible or false if it was not.
     */
// Arrays incomplete... need to use the array setter
    public boolean setAccessible(Object obj) {
        if (obj instanceof AccessibleObject) {
            ((AccessibleObject) obj).setAccessible(true);
            return true;
        } else {
            return false;
        }
    }
}
