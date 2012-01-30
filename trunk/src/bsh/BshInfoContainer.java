/*
 * nbBeanShell -- a integration of BeanShell into the NetBeans IDE
 * Copyright (C) 2011 Thomas Werner
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any
 * later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package bsh;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Thomas Werner
 */
public abstract class BshInfoContainer {
    
    protected final List<BshMethodInfo> methods = new LinkedList<BshMethodInfo>();
    protected final List<BshVariableInfo> variables = new LinkedList<BshVariableInfo>();
    
    protected int beginLine;
    protected int beginColum;
    protected int endLine;
    protected int endColum;    
    
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

    public int getBeginColum() {
        return beginColum;
    }

    public void setBeginColum(int beginColum) {
        this.beginColum = beginColum;
    }

    public int getBeginLine() {
        return beginLine;
    }

    public void setBeginLine(int beginLine) {
        this.beginLine = beginLine;
    }

    public int getEndColum() {
        return endColum;
    }

    public void setEndColum(int endColum) {
        this.endColum = endColum;
    }

    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }
    
}
