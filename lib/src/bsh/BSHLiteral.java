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

class BSHLiteral extends SimpleNode {

    public Object value;

    BSHLiteral(int id) {
        super(id);
    }

    @Override
    public Object eval(CallStack callstack, Interpreter interpreter, Object resumeStatus) throws EvalError {
        if(value == null) {
            throw new InterpreterError("Null in bsh literal: " + value);
        }

        return value;
    }

    private char getEscapeChar(char ch) {
        switch(ch) {
            case 'b':
                ch = '\b';
                break;

            case 't':
                ch = '\t';
                break;

            case 'n':
                ch = '\n';
                break;

            case 'f':
                ch = '\f';
                break;

            case 'r':
                ch = '\r';
                break;

            // do nothing - ch already contains correct character
            case '"':
            case '\'':
            case '\\':
                break;
        }

        return ch;
    }

    public void charSetup(String str) {
        char ch = str.charAt(0);
        if(ch == '\\') {
            // get next character
            ch = str.charAt(1);

            if(Character.isDigit(ch)) {
                ch = (char) Integer.parseInt(str.substring(1), 8);
            } else {
                ch = getEscapeChar(ch);
            }
        }

        value = new Primitive(new Character(ch).charValue());
    }

    void stringSetup(String str) {
        StringBuilder buffer = new StringBuilder();
        for(int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if(ch == '\\') {
                // get next character
                ch = str.charAt(++i);

                if(Character.isDigit(ch)) {
                    int endPos = i;

                    // check the next two characters
                    while(endPos < i + 2) {
                        if(Character.isDigit(str.charAt(endPos + 1))) {
                            endPos++;
                        } else {
                            break;
                        }
                    }

                    ch = (char) Integer.parseInt(str.substring(i, endPos + 1), 8);
                    i = endPos;
                } else {
                    ch = getEscapeChar(ch);
                }
            }

            buffer.append(ch);
        }

        value = buffer.toString().intern();
    }
}
