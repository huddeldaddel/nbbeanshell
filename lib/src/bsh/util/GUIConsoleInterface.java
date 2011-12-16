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
package bsh.util;

import bsh.ConsoleInterface;
import java.awt.Color;

/**
 * Additional capabilities of an interactive console for BeanShell. Althought this is called "GUIConsoleInterface" it
 * might just as well be used by a more sophisticated text-only command line. <p> Note: we may want to express the
 * command line history, editing, and cut & paste functionality here as well at some point.
 */
public interface GUIConsoleInterface extends ConsoleInterface {

    public void print(Object o, Color color);

    public void setNameCompletion(NameCompletion nc);

    /**
     * e.g. the wait cursor
     */
    public void setWaitFeedback(boolean on);
}
