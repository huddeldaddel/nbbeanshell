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

import java.util.EventObject;

/**
 *
 * @author Thomas Werner
 */
public class DebugEvent extends EventObject {
    
    public static enum Type {
        Stopped,
        Finished;
    }
    
    private final Type type;
    private final int line;
    private final String file;
    
    /**
     * Creates a new instance of the DebugEvent class.
     * @param source the debugger that fired the event
     */
    public DebugEvent(Debugger source, Type type, int line, String file) {
        super(source);
        this.type = type;
        this.line = line;
        this.file = file;
    }
    
    @Override
    public Debugger getSource() {
        return (Debugger) super.getSource();
    }

    public String getFile() {
        return file;
    }

    public int getLine() {
        return line;
    }
    
    public Type getType() {
        return type;
    }
    
}
