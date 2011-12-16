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
 */
class BSHClassDeclaration extends SimpleNode {

    /**
     * The class instance initializer method name. A BshMethod by this name is installed by the class delcaration into
     * the static class body namespace. It is called once to initialize the static members of the class space and each
     * time an instances is created to initialize the instance members.
     */
    static final String CLASSINITNAME = "_bshClassInit";
    String name;
    Modifiers modifiers;
    int numInterfaces;
    boolean extend;
    boolean isInterface;

    BSHClassDeclaration(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        int child = 0;

        // resolve superclass if any
        Class superClass = null;
        if(extend) {
            BSHAmbiguousName superNode = (BSHAmbiguousName) jjtGetChild(child++);
            superClass = superNode.toClass(callstack, interpreter);
        }

        // Get interfaces
        Class[] interfaces = new Class[numInterfaces];
        for(int i = 0; i < numInterfaces; i++) {
            BSHAmbiguousName node = (BSHAmbiguousName) jjtGetChild(child++);
            interfaces[i] = node.toClass(callstack, interpreter);
            if(!interfaces[i].isInterface()) 
                throw new EvalError("Type: " + node.text + " is not an interface!", this, callstack);
        }

        BSHBlock block;
        // Get the class body BSHBlock
        if(child < jjtGetNumChildren()) {
            block = (BSHBlock) jjtGetChild(child);
        } else {
            block = new BSHBlock(ParserTreeConstants.JJTBLOCK);
        }

        try {
            return ClassGenerator.getClassGenerator()
                                 .generateClass(name, modifiers, interfaces, superClass, block, isInterface,
                                                callstack, interpreter);
        } catch(UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }

    }

    @Override
    public String toString() {
        return "ClassDeclaration: " + name;
    }
}
