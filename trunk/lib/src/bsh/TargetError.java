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

import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

/**
 * TargetError is an EvalError that wraps an exception thrown by the script (or by code called from the script).
 * TargetErrors indicate exceptions which can be caught within the script itself, whereas a general EvalError indicates
 * that the script cannot be evaluated further for some reason.
 *
 * If the exception is caught within the script it is automatically unwrapped, so the code looks like normal Java code.
 * If the TargetError is thrown from the eval() or interpreter.eval() method it may be caught and unwrapped to determine
 * what exception was thrown.
 */
public class TargetError extends EvalError {

    Throwable target;
    boolean inNativeCode;

    public TargetError(
            String msg, Throwable t, SimpleNode node, CallStack callstack,
            boolean inNativeCode) {
        super(msg, node, callstack);
        target = t;
        this.inNativeCode = inNativeCode;
    }

    public TargetError(Throwable t, SimpleNode node, CallStack callstack) {
        this("TargetError", t, node, callstack, false);
    }

    public Throwable getTarget() {
        // check for easy mistake
        if (target instanceof InvocationTargetException) {
            return ((InvocationTargetException) target).getTargetException();
        } else {
            return target;
        }
    }

    public String toString() {
        return super.toString()
                + "\nTarget exception: "
                + printTargetError(target);
    }

    public void printStackTrace() {
        printStackTrace(false, System.err);
    }

    public void printStackTrace(PrintStream out) {
        printStackTrace(false, out);
    }

    public void printStackTrace(boolean debug, PrintStream out) {
        if (debug) {
            super.printStackTrace(out);
            out.println("--- Target Stack Trace ---");
        }
        target.printStackTrace(out);
    }

    /**
     * Generate a printable string showing the wrapped target exception. If the proxy mechanism is available, allow the
     * extended print to check for UndeclaredThrowableException and print that embedded error.
     */
    public String printTargetError(Throwable t) {
        String s = target.toString();

        if (Capabilities.canGenerateInterfaces()) {
            s += "\n" + xPrintTargetError(t);
        }

        return s;
    }

    /**
     * Extended form of print target error. This indirection is used to print UndeclaredThrowableExceptions which are
     * possible when the proxy mechanism is available.
     *
     * We are shielded from compile problems by using a bsh script. This is acceptable here because we're not in a
     * critical path... Otherwise we'd need yet another dynamically loaded module just for this.
     */
    public String xPrintTargetError(Throwable t) {
        String getTarget =
                "import java.lang.reflect.UndeclaredThrowableException;"
                + "String result=\"\";"
                + "while ( target instanceof UndeclaredThrowableException ) {"
                + "	target=target.getUndeclaredThrowable(); "
                + "	result+=\"Nested: \"+target.toString();"
                + "}"
                + "return result;";
        Interpreter i = new Interpreter();
        try {
            i.set("target", t);
            return (String) i.eval(getTarget);
        } catch (EvalError e) {
            throw new InterpreterError("xprintarget: " + e.toString());
        }
    }

    /**
     * Return true if the TargetError was generated from native code. e.g. if the script called into a compiled java
     * class which threw the excpetion. We distinguish so that we can print the stack trace for the native code case...
     * the stack trace would not be useful if the exception was generated by the script. e.g. if the script explicitly
     * threw an exception... (the stack trace would simply point to the bsh internals which generated the exception).
     */
    public boolean inNativeCode() {
        return inNativeCode;
    }
}
