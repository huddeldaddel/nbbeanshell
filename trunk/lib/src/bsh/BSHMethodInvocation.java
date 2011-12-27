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

import java.lang.reflect.InvocationTargetException;

class BSHMethodInvocation extends SimpleNode {

    BSHMethodInvocation(int id) {
        super(id);
    }

    BSHAmbiguousName getNameNode() {
        return (BSHAmbiguousName) jjtGetChild(0);
    }

    BSHArguments getArgsNode() {
        return (BSHArguments) jjtGetChild(1);
    }

    /**
     * Evaluate the method invocation with the specified callstack and interpreter
     */
    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, DebuggerContext dContext) throws EvalError {
        NameSpace namespace = callstack.top();
        BSHAmbiguousName nameNode = getNameNode();

        // Do not evaluate methods this() or super() in class instance space
        // (i.e. inside a constructor)
        if(namespace.getParent() != null && namespace.getParent().isClass
                && (nameNode.text.equals("super") || nameNode.text.equals("this"))) {
            return Primitive.VOID;
        }

        Name name = nameNode.getName(namespace);
        Object[] args = getArgsNode().getArguments(callstack, interpreter);

        // This try/catch block is replicated is BSHPrimarySuffix... need to
        // factor out common functionality...
        // Move to Reflect?
        try {
            return name.invokeMethod(interpreter, args, callstack, this);
        } catch(ReflectError e) {
            throw new EvalError(
                    "Error in method invocation: " + e.getMessage(),
                    this, callstack);
        } catch(InvocationTargetException e) {
            String msg = "Method Invocation " + name;
            Throwable te = e.getTargetException();

            /*
             * Try to squeltch the native code stack trace if the exception was caused by a reflective call back into
             * the bsh interpreter (e.g. eval() or source()
             */
            boolean isNative = true;
            if(te instanceof EvalError) {
                if(te instanceof TargetError) {
                    isNative = ((TargetError) te).inNativeCode();
                } else {
                    isNative = false;
                }
            }

            throw new TargetError(msg, te, this, callstack, isNative);
        } catch(UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }
}
