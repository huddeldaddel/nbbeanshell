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

class BSHPrimaryExpression extends SimpleNode {

    BSHPrimaryExpression(int id) {
        super(id);
    }

    /**
     * Evaluate to a value object.
     */
    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        if(interpreter instanceof Debugger) {
            final Debugger debugger = (Debugger) interpreter;
            if(debugger.isBreakpoint(this)) {
                debugger.parkNode(this, null);
                return null;
            }
        }
        
        return eval(false, callstack, interpreter);
    }

    /**
     * Evaluate to a value object.
     */
    public LHS toLHS(CallStack callstack, Interpreter interpreter) throws EvalError {
        Object obj = eval(true, callstack, interpreter);

        if (!(obj instanceof LHS)) {
            throw new EvalError("Can't assign to:", this, callstack);
        } else {
            return (LHS) obj;
        }
    }

    /*
     * Our children are a prefix expression and any number of suffixes. <p>
     *
     * We don't eval() any nodes until the suffixes have had an opportunity to work through them. This lets the suffixes
     * decide how to interpret an ambiguous name (e.g. for the .class operation).
     */
    private Object eval(boolean toLHS, CallStack callstack, Interpreter interpreter) throws EvalError {
        Object obj = jjtGetChild(0);
        int numChildren = jjtGetNumChildren();

        for (int i = 1; i < numChildren; i++) {
            obj = ((BSHPrimarySuffix) jjtGetChild(i)).doSuffix(obj, toLHS, callstack, interpreter);
        }

        /*
         * If the result is a Node eval() it to an object or LHS (as determined by toLHS)
         */
        if (obj instanceof SimpleNode) {
            if (obj instanceof BSHAmbiguousName) {
                if (toLHS) {
                    obj = ((BSHAmbiguousName) obj).toLHS(callstack, interpreter);
                } else {
                    obj = ((BSHAmbiguousName) obj).toObject(callstack, interpreter);
                }
            } else // Some arbitrary kind of node
            if (toLHS) {// is this right?
                throw new EvalError("Can't assign to prefix.", this, callstack);
            } else {
                obj = ((SimpleNode) obj).eval(callstack, interpreter, null);
            }
        }

        // return LHS or value object as determined by toLHS
        if (obj instanceof LHS) {
            if (toLHS) {
                return obj;
            } else {
                try {
                    return ((LHS) obj).getValue();
                } catch (UtilEvalError e) {
                    throw e.toEvalError(this, callstack);
                }
            }
        } else {
            return obj;
        }
    }
}
