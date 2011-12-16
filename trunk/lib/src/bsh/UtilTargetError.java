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

/**
 * UtilTargetError is an error corresponding to a TargetError but thrown by a utility or other class that does not have
 * the caller context (Node) available to it. See UtilEvalError for an explanation of the difference between
 * UtilEvalError and EvalError. <p>
 *
 * @see UtilEvalError
 */
public class UtilTargetError extends UtilEvalError {

    public Throwable t;

    public UtilTargetError(String message, Throwable t) {
        super(message);
        this.t = t;
    }

    public UtilTargetError(Throwable t) {
        this(null, t);
    }

    /**
     * Override toEvalError to throw TargetError type.
     */
    public EvalError toEvalError(
            String msg, SimpleNode node, CallStack callstack) {
        if (msg == null) {
            msg = getMessage();
        } else {
            msg = msg + ": " + getMessage();
        }

        return new TargetError(msg, t, node, callstack, false);
    }
}
