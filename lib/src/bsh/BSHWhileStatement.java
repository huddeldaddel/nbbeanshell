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
 * This class handles both while(){} statements and do{}while() statements.
 */
class BSHWhileStatement extends SimpleNode implements ParserConstants {

    public boolean isDoStatement;

    BSHWhileStatement(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, DebuggerContext dContext) throws EvalError {
        int numChild = jjtGetNumChildren();

        // Order of body and condition is swapped for do / while
        SimpleNode condExp, body = null;

        if(isDoStatement) {
            condExp = (SimpleNode) jjtGetChild(1);
            body = (SimpleNode) jjtGetChild(0);
        } else {
            condExp = (SimpleNode) jjtGetChild(0);
            if(numChild > 1) // has body, else just for side effects
                body = (SimpleNode) jjtGetChild(1);
        }

        boolean doOnceFlag = isDoStatement;
        while(doOnceFlag || BSHIfStatement.evaluateCondition(condExp, callstack, interpreter)) {
            if(body == null) // no body?
                continue;

            Object ret = body.eval(callstack, interpreter, null);

            boolean breakout = false;
            if(ret instanceof ReturnControl) {
                switch(((ReturnControl) ret).kind) {
                    case RETURN:
                        return ret;

                    case CONTINUE:
                        continue;

                    case BREAK:
                        breakout = true;
                        break;
                }
            }
            if(breakout)
                break;

            doOnceFlag = false;
        }

        return Primitive.VOID;
    }
}
