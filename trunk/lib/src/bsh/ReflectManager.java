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
 *  You should have received a copy of the GNU Lesser General Public License along with this program.                  *
 *  If not, see <http://www.gnu.org/licenses/>.                                                                        *
 *                                                                                                                     *
 *  Patrick Niemeyer (pat@pat.net)                                                                                     *
 *  Author of Learning Java, O'Reilly & Associates                                                                     *
 *  http://www.pat.net/~pat/                                                                                           *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh;

import bsh.Capabilities.Unavailable;

/**
 * ReflectManager is a dynamically loaded extension that supports extended reflection features supported by JDK1.2 and
 * greater.
 *
 * In particular it currently supports accessible method and field access supported by JDK1.2 and greater.
 */
public abstract class ReflectManager {

    private static ReflectManager rfm;

    /**
     * Return the singleton bsh ReflectManager.
     *
     * @throws Unavailable
     */
    public static ReflectManager getReflectManager()
            throws Unavailable {
        if (rfm == null) {
            Class clas;
            try {
                clas = Class.forName("bsh.reflect.ReflectManagerImpl");
                rfm = (ReflectManager) clas.newInstance();
            } catch (Exception e) {
                throw new Unavailable("Reflect Manager unavailable: " + e);
            }
        }

        return rfm;
    }

    /**
     * Reflect Manager Set Accessible. Convenience method to invoke the reflect manager.
     *
     * @throws Unavailable
     */
    public static boolean RMSetAccessible(Object obj)
            throws Unavailable {
        return getReflectManager().setAccessible(obj);
    }

    /**
     * Set a java.lang.reflect Field, Method, Constructor, or Array of accessible objects to accessible mode.
     *
     * @return true if the object was accessible or false if it was not.
     */
    public abstract boolean setAccessible(Object o);
}
