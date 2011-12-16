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
 * Implementation of the for(;;) statement.
 */
class BSHForStatement extends SimpleNode implements ParserConstants {

    public boolean hasForInit;
    public boolean hasExpression;
    public boolean hasForUpdate;
    private SimpleNode forInit;
    private SimpleNode expression;
    private SimpleNode forUpdate;
    private SimpleNode statement;
    private boolean parsed;

    BSHForStatement(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        int i = 0;
        if(hasForInit) 
            forInit = ((SimpleNode) jjtGetChild(i++));
        
        if(hasExpression) 
            expression = ((SimpleNode) jjtGetChild(i++));
        
        if(hasForUpdate) 
            forUpdate = ((SimpleNode) jjtGetChild(i++));
        
        if(i < jjtGetNumChildren()) // should normally be
            statement = ((SimpleNode) jjtGetChild(i));

        NameSpace enclosingNameSpace = callstack.top();
        BlockNameSpace forNameSpace = new BlockNameSpace(enclosingNameSpace);

        /*
         * Note: some interesting things are going on here.
         *
         * 1) We swap instead of push... The primary mode of operation acts like we are in the enclosing namespace...
         * (super must be preserved, etc.)
         *
         * 2) We do *not* call the body block eval with the namespace override. Instead we allow it to create a second
         * subordinate BlockNameSpace child of the forNameSpace. Variable propogation still works through the chain, but
         * the block's child cleans the state between iteration. (which is correct Java behavior... see forscope4.bsh)
         */

        // put forNameSpace it on the top of the stack
        // Note: it's important that there is only one exit point from this
        // method so that we can swap back the namespace.
        callstack.swap(forNameSpace);

        // Do the for init
        if(hasForInit) {
            forInit.eval(callstack, interpreter, null);
        }

        Object returnControl = Primitive.VOID;
        while(true) {
            if(hasExpression) {
                boolean cond = BSHIfStatement.evaluateCondition(
                        expression, callstack, interpreter);

                if(!cond) {
                    break;
                }
            }

            boolean breakout = false; // switch eats a multi-level break here?
            if(statement != null) // not empty statement
            {
                // do *not* invoke special override for block... (see above)
                Object ret = statement.eval(callstack, interpreter, null);

                if(ret instanceof ReturnControl) {
                    switch(((ReturnControl) ret).kind) {
                        case RETURN:
                            returnControl = ret;
                            breakout = true;
                            break;

                        case CONTINUE:
                            break;

                        case BREAK:
                            breakout = true;
                            break;
                    }
                }
            }

            if(breakout) {
                break;
            }

            if(hasForUpdate)
                forUpdate.eval(callstack, interpreter, null);
        }

        callstack.swap(enclosingNameSpace);  // put it back
        return returnControl;
    }
}
