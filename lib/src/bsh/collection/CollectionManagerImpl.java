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
package bsh.collection;

import bsh.BshIterator;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * Dynamically loaded extension supporting post 1.1 collections iterator.
 *
 * @author Pat Niemeyer
 */
public class CollectionManagerImpl extends bsh.CollectionManager {

    public BshIterator getBshIterator(Object obj)
            throws IllegalArgumentException {
        if (obj instanceof Collection || obj instanceof Iterator) {
            return new CollectionIterator(obj);
        } else {
            return new bsh.CollectionManager.BasicBshIterator(obj);
        }
    }

    public boolean isMap(Object obj) {
        if (obj instanceof Map) {
            return true;
        } else {
            return super.isMap(obj);
        }
    }

    public Object getFromMap(Object map, Object key) {
        // Hashtable implements Map
        return ((Map) map).get(key);
    }

    /*
     * Place the raw value into the map... should be unwrapped.
     */
    public Object putInMap(Object map, Object key, Object value) {
        // Hashtable implements Map
        return ((Map) map).put(key, value);
    }
}
