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

class BSHSwitchStatement
        extends SimpleNode
        implements ParserConstants {

    public BSHSwitchStatement(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        int numchild = jjtGetNumChildren();
        int child = 0;
        SimpleNode switchExp = ((SimpleNode) jjtGetChild(child++));
        Object switchVal = switchExp.eval(callstack, interpreter, null);

        /*
         * Note: this could be made clearer by adding an inner class for the cases and an object context for the child
         * traversal.
         */
        // first label
        BSHSwitchLabel label;
        Object node;
        ReturnControl returnControl = null;

        // get the first label
        if(child >= numchild) 
            throw new EvalError("Empty switch statement.", this, callstack);
        
        label = ((BSHSwitchLabel) jjtGetChild(child++));

        // while more labels or blocks and haven't hit return control
        while(child < numchild && returnControl == null) {
            // if label is default or equals switchVal
            if(label.isDefault || 
               primitiveEquals(switchVal, label.eval(callstack, interpreter, null), callstack, switchExp)) {
                // execute nodes, skipping labels, until a break or return
                while(child < numchild) {
                    node = jjtGetChild(child++);
                    if(node instanceof BSHSwitchLabel) {
                        continue;
                    }
                    // eval it
                    Object value = ((SimpleNode) node).eval(callstack, interpreter, null);

                    // should check to disallow continue here?
                    if(value instanceof ReturnControl) {
                        returnControl = (ReturnControl) value;
                        break;
                    }
                }
            } else {
                // skip nodes until next label
                while(child < numchild) {
                    node = jjtGetChild(child++);
                    if(node instanceof BSHSwitchLabel) {
                        label = (BSHSwitchLabel) node;
                        break;
                    }
                }
            }
        }

        if(returnControl != null && returnControl.kind == RETURN) 
            return returnControl;
        
        return Primitive.VOID;
    }

    /**
     * Helper method for testing equals on two primitive or boxable objects. yuck: factor this out into Primitive.java
     */
    private boolean primitiveEquals(Object switchVal, Object targetVal, CallStack callstack, SimpleNode switchExp)
                    throws EvalError {
        if(switchVal instanceof Primitive || targetVal instanceof Primitive) {
            try {
                // binaryOperation can return Primitive or wrapper type 
                Object result = Primitive.binaryOperation(switchVal, targetVal, ParserConstants.EQ);
                result = Primitive.unwrap(result);
                return result.equals(Boolean.TRUE);
            } catch(UtilEvalError e) {
                throw e.toEvalError("Switch value: " + switchExp.getText() + ": ", this, callstack);
            }
        } else {
            return switchVal.equals(targetVal);
        }
    }
}
