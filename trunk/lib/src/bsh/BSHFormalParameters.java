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

class BSHFormalParameters extends SimpleNode {

    private String[] paramNames;
    /**
     * For loose type parameters the paramTypes are null.
     */
    // unsafe caching of types
    Class[] paramTypes;
    int numArgs;
    String[] typeDescriptors;

    BSHFormalParameters(int id) {
        super(id);
    }

    void insureParsed() {
        if(paramNames != null) {
            return;
        }

        this.numArgs = jjtGetNumChildren();
        String[] paramNames = new String[numArgs];

        for(int i = 0; i < numArgs; i++) {
            BSHFormalParameter param = (BSHFormalParameter) jjtGetChild(i);
            paramNames[i] = param.name;
        }

        this.paramNames = paramNames;
    }

    public String[] getParamNames() {
        insureParsed();
        return paramNames;
    }

    public String[] getTypeDescriptors(
            CallStack callstack, Interpreter interpreter, String defaultPackage) {
        if(typeDescriptors != null) {
            return typeDescriptors;
        }

        insureParsed();
        String[] typeDesc = new String[numArgs];

        for(int i = 0; i < numArgs; i++) {
            BSHFormalParameter param = (BSHFormalParameter) jjtGetChild(i);
            typeDesc[i] = param.getTypeDescriptor(
                    callstack, interpreter, defaultPackage);
        }

        this.typeDescriptors = typeDesc;
        return typeDesc;
    }

    /**
     * Evaluate the types. Note that type resolution does not require the interpreter instance.
     */
    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        if(paramTypes != null) 
            return paramTypes;

        insureParsed();
        Class[] paramTypes = new Class[numArgs];

        for(int i = 0; i < numArgs; i++) {
            BSHFormalParameter param = (BSHFormalParameter) jjtGetChild(i);
            paramTypes[i] = (Class) param.eval(callstack, interpreter, null);
        }

        this.paramTypes = paramTypes;

        return paramTypes;
    }
}
