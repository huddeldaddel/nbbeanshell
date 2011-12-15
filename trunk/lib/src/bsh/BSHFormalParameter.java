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

/**
	A formal parameter declaration.
	For loose variable declaration type is null.
*/
class BSHFormalParameter extends SimpleNode
{
	public static final Class UNTYPED = null;
	public String name;
	// unsafe caching of type here
	public Class type;

	BSHFormalParameter(int id) { super(id); }

	public String getTypeDescriptor( 
		CallStack callstack, Interpreter interpreter, String defaultPackage ) 
	{
		if ( jjtGetNumChildren() > 0 )
			return ((BSHType)jjtGetChild(0)).getTypeDescriptor( 
				callstack, interpreter, defaultPackage );
		else
			// this will probably not get used
			return "Ljava/lang/Object;";  // Object type
	}

	/**
		Evaluate the type.
	*/
	public Object eval( CallStack callstack, Interpreter interpreter) 
		throws EvalError
	{
		if ( jjtGetNumChildren() > 0 )
			type = ((BSHType)jjtGetChild(0)).getType( callstack, interpreter );
		else
			type = UNTYPED;

		return type;
	}
}

