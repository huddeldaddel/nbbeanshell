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

class BSHImportDeclaration extends SimpleNode
{
	public boolean importPackage;
	public boolean staticImport;
	public boolean superImport;

	BSHImportDeclaration(int id) { super(id); }

	public Object eval( CallStack callstack, Interpreter interpreter) 
		throws EvalError
	{
		NameSpace namespace = callstack.top();
		if ( superImport )
			try {
				namespace.doSuperImport();
			} catch ( UtilEvalError e ) {
				throw e.toEvalError( this, callstack  );
			}
		else 
		{
			if ( staticImport )
			{
				if ( importPackage )
				{
					Class clas = ((BSHAmbiguousName)jjtGetChild(0)).toClass( 
						callstack, interpreter );
					namespace.importStatic( clas );
				} else
					throw new EvalError( 
						"static field imports not supported yet", 
						this, callstack );
			} else 
			{
				String name = ((BSHAmbiguousName)jjtGetChild(0)).text;
				if ( importPackage )
					namespace.importPackage(name);
				else
					namespace.importClass(name);
			}
		}

        return Primitive.VOID;
	}
}

