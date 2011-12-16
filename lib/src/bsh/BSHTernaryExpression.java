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
 * This class needs logic to prevent the right hand side of boolean logical expressions from being naively evaluated...
 * e.g. for "foo && bar" bar should not be evaluated in the case where foo is true.
 */
class BSHTernaryExpression extends SimpleNode {

    BSHTernaryExpression(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        final SimpleNode cond = (SimpleNode) jjtGetChild(0);
        final SimpleNode evalTrue = (SimpleNode) jjtGetChild(1);
        final SimpleNode evalFalse = (SimpleNode) jjtGetChild(2);

        if(BSHIfStatement.evaluateCondition(cond, callstack, interpreter))
            return evalTrue.eval(callstack, interpreter, null);
        
        return evalFalse.eval(callstack, interpreter, null);
    }
}
