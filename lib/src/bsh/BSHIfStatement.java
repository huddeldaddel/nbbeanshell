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

class BSHIfStatement extends SimpleNode {

    BSHIfStatement(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter) throws EvalError {
        Object ret = null;

        if(evaluateCondition((SimpleNode) jjtGetChild(0), callstack, interpreter)) {   // if
            ret = ((SimpleNode) jjtGetChild(1)).eval(callstack, interpreter);
        } else if(jjtGetNumChildren() > 2) {                                           // else
            ret = ((SimpleNode) jjtGetChild(2)).eval(callstack, interpreter);
        }

        if(ret instanceof ReturnControl) {
            return ret;
        }

        return Primitive.VOID;
    }

    public static boolean evaluateCondition(SimpleNode condExp, CallStack callstack, Interpreter interpreter)
                          throws EvalError {
        Object obj = condExp.eval(callstack, interpreter);
        if(obj instanceof Primitive) {
            if(obj == Primitive.VOID) 
                throw new EvalError("Condition evaluates to void type", condExp, callstack);
            obj = ((Primitive) obj).getValue();
        }

        if(obj instanceof Boolean)
            return ((Boolean) obj).booleanValue();

        throw new EvalError("Condition must evaluate to a Boolean or boolean.", condExp, callstack);
    }
}
