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

class BSHUnaryExpression extends SimpleNode implements ParserConstants {

    public int kind;
    public boolean postfix = false;

    BSHUnaryExpression(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        SimpleNode node = (SimpleNode) jjtGetChild(0);

        // If this is a unary increment of decrement (either pre or postfix)
        // then we need an LHS to which to assign the result.  Otherwise
        // just do the unary operation for the value.
        try {
            if(kind == INCR || kind == DECR) {
                LHS lhs = ((BSHPrimaryExpression) node).toLHS(callstack, interpreter);
                return lhsUnaryOperation(lhs, interpreter.getStrictJava());
            } else {
                return unaryOperation(node.eval(callstack, interpreter, null), kind);
            }
        } catch(UtilEvalError e) {
            throw e.toEvalError(this, callstack);
        }
    }

    private Object lhsUnaryOperation(LHS lhs, boolean strictJava) throws UtilEvalError {
        if(Interpreter.DEBUG) {
            Interpreter.debug("lhsUnaryOperation");
        }
        Object prevalue, postvalue;
        prevalue = lhs.getValue();
        postvalue = unaryOperation(prevalue, kind);

        Object retVal;
        if(postfix) {
            retVal = prevalue;
        } else {
            retVal = postvalue;
        }

        lhs.assign(postvalue, strictJava);
        return retVal;
    }

    private Object unaryOperation(Object op, int kind) throws UtilEvalError {
        if(op instanceof Boolean || op instanceof Character || op instanceof Number) 
            return primitiveWrapperUnaryOperation(op, kind);

        if(!(op instanceof Primitive))
            throw new UtilEvalError("Unary operation " + tokenImage[kind] + " inappropriate for object");

        return Primitive.unaryOperation((Primitive) op, kind);
    }

    private Object primitiveWrapperUnaryOperation(Object val, int kind) throws UtilEvalError {
        Class operandType = val.getClass();
        Object operand = Primitive.promoteToInteger(val);

        if(operand instanceof Boolean) 
            return Boolean.valueOf(Primitive.booleanUnaryOperation((Boolean) operand, kind));
        
        if(operand instanceof Integer) {
            int result = Primitive.intUnaryOperation((Integer) operand, kind);

            // ++ and -- must be cast back the original type
            if(kind == INCR || kind == DECR) {
                if(operandType == Byte.TYPE)
                    return new Byte((byte) result);
                
                if(operandType == Short.TYPE) 
                    return new Short((short) result);
                
                if(operandType == Character.TYPE) 
                    return new Character((char) result);
            }

            return new Integer(result);
        } 
        
        if(operand instanceof Long)
            return new Long(Primitive.longUnaryOperation((Long) operand, kind));
        
        if(operand instanceof Float)
            return new Float(Primitive.floatUnaryOperation((Float) operand, kind));
        
        if(operand instanceof Double)
            return new Double(Primitive.doubleUnaryOperation((Double) operand, kind));
        
        throw new InterpreterError("An error occurred.  Please call technical support.");
    }
}
