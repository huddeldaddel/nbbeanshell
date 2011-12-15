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
package bsh;

public class DelayedEvalBshMethod extends BshMethod {

    String returnTypeDescriptor;
    BSHReturnType returnTypeNode;
    String[] paramTypeDescriptors;
    BSHFormalParameters paramTypesNode;
    // used for the delayed evaluation...
    transient CallStack callstack;
    transient Interpreter interpreter;

    /**
     * This constructor is used in class generation. It supplies String type descriptors for return and parameter class
     * types and allows delay of the evaluation of those types until they are requested. It does this by holding BSHType
     * nodes, as well as an evaluation callstack, and interpreter which are called when the class types are requested.
     */
    /*
     * Note: technically I think we could get by passing in only the current namespace or perhaps BshClassManager here
     * instead of CallStack and Interpreter. However let's just play it safe in case of future changes - anywhere you
     * eval a node you need these.
     */
    DelayedEvalBshMethod(
            String name,
            String returnTypeDescriptor, BSHReturnType returnTypeNode,
            String[] paramNames,
            String[] paramTypeDescriptors, BSHFormalParameters paramTypesNode,
            BSHBlock methodBody,
            NameSpace declaringNameSpace, Modifiers modifiers,
            CallStack callstack, Interpreter interpreter) {
        super(name, null/*
                 * returnType
                 */, paramNames, null/*
                 * paramTypes
                 */,
                methodBody, declaringNameSpace, modifiers);

        this.returnTypeDescriptor = returnTypeDescriptor;
        this.returnTypeNode = returnTypeNode;
        this.paramTypeDescriptors = paramTypeDescriptors;
        this.paramTypesNode = paramTypesNode;
        this.callstack = callstack;
        this.interpreter = interpreter;
    }

    public String getReturnTypeDescriptor() {
        return returnTypeDescriptor;
    }

    @Override
    public Class getReturnType() {
        if (returnTypeNode == null) {
            return null;
        }

        // BSHType will cache the type for us
        try {
            return returnTypeNode.evalReturnType(callstack, interpreter);
        } catch (EvalError e) {
            throw new InterpreterError("can't eval return type: " + e);
        }
    }

    public String[] getParamTypeDescriptors() {
        return paramTypeDescriptors;
    }

    @Override
    public Class[] getParameterTypes() {
        // BSHFormalParameters will cache the type for us
        try {
            return (Class[]) paramTypesNode.eval(callstack, interpreter);
        } catch (EvalError e) {
            throw new InterpreterError("can't eval param types: " + e);
        }
    }
}
