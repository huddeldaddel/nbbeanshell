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
 *  Thomas Werner                                                                                                      *
 *  http://www.xing.com/profile/Thomas_Werner108                                                                       *
 *                                                                                                                     *
 **********************************************************************************************************************/
package bsh;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Thomas Werner
 */
public abstract class BshInfoContainer {
    
    protected List<BshMethodInfo> methods = new LinkedList<BshMethodInfo>();
    protected List<BshVariableInfo> variables = new LinkedList<BshVariableInfo>();
    
    public List<BshMethodInfo> getMethods() {
        return methods;
    }

    public void addMethods(Collection<BshMethodInfo> methodInfos) {
        methods.addAll(methodInfos);
    }

    public List<BshVariableInfo> getVariables() {
        return variables;
    }

    public void addVariables(Collection<BshVariableInfo> variableInfos) {
        variables.addAll(variableInfos);
    }
    
    public List<BshMethodInfo> getClasses() {
        final List<BshMethodInfo> result = new LinkedList<BshMethodInfo>();
        for(BshMethodInfo method: methods)
            if(method.isClass())
                result.add(method);
        return result;
    }
    
}
