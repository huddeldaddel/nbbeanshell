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

class BSHReturnType extends SimpleNode {

    public boolean isVoid;

    BSHReturnType(int id) {
        super(id);
    }

    BSHType getTypeNode() {
        return (BSHType) jjtGetChild(0);
    }

    public String getTypeDescriptor(CallStack callstack, Interpreter interpreter, String defaultPackage) {
        if(isVoid) 
            return "V";
        
        return getTypeNode().getTypeDescriptor(callstack, interpreter, defaultPackage);
    }

    public Class evalReturnType(CallStack callstack, Interpreter interpreter) throws EvalError {
        if(isVoid)
            return Void.TYPE;
        return getTypeNode().getType(callstack, interpreter);
    }
}
