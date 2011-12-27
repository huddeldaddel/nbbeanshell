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
 * Implement casts.
 *
 * I think it should be possible to simplify some of the code here by using the Types.getAssignableForm() method, but I
 * haven't looked into it.
 */
class BSHCastExpression extends SimpleNode {

    public BSHCastExpression(int id) {
        super(id);
    }

    /**
     * @return the result of the cast.
     */
    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, DebuggerContext dContext) throws EvalError {
        NameSpace namespace = callstack.top();
        Class toType = ((BSHType) jjtGetChild(0)).getType(
                callstack, interpreter);
        SimpleNode expression = (SimpleNode) jjtGetChild(1);

        // evaluate the expression
        Object fromValue = expression.eval(callstack, interpreter, null);
        Class fromType = fromValue.getClass();

        // TODO: need to add isJavaCastable() test for strictJava
        // (as opposed to isJavaAssignable())
        try {
            return Types.castObject(fromValue, toType, Types.CAST);
        } catch(UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }
}
