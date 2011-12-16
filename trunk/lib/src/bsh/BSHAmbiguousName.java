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

class BSHAmbiguousName extends SimpleNode {

    public String text;

    BSHAmbiguousName(int id) {
        super(id);
    }

    public Name getName(NameSpace namespace) {
        return namespace.getNameResolver(text);
    }

    public Object toObject(CallStack callstack, Interpreter interpreter) throws EvalError {
        return toObject(callstack, interpreter, false);
    }

    Object toObject(CallStack callstack, Interpreter interpreter, boolean forceClass) throws EvalError {
        try {
            return getName(callstack.top()).toObject(callstack, interpreter, forceClass);
        } catch(UtilEvalError e) {
            //e.printStackTrace();
            throw e.toEvalError(this, callstack);
        }
    }

    public Class toClass(CallStack callstack, Interpreter interpreter) throws EvalError {
        try {
            return getName(callstack.top()).toClass();
        } catch(ClassNotFoundException e) {
            throw new EvalError(e.getMessage(), this, callstack);
        } catch(UtilEvalError e2) {
            // ClassPathException is a type of UtilEvalError
            throw e2.toEvalError(this, callstack);
        }
    }

    public LHS toLHS(CallStack callstack, Interpreter interpreter) throws EvalError {
        try {
            return getName(callstack.top()).toLHS(callstack, interpreter);
        } catch(UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }

    /*
     * The interpretation of an ambiguous name is context sensitive. We disallow a generic eval( ).
     */
    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        throw new InterpreterError("Don't know how to eval an ambiguous name! Use toObject() if you want an object.");
    }

    @Override
    public String toString() {
        return "AmbigousName: " + text;
    }
}
