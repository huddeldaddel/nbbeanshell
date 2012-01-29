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
package de.bfg9000.beanshell.navigator;

import bsh.BshModifierInfo;
import bsh.BshVariableInfo;
import java.awt.Image;
import javax.swing.text.JTextComponent;
import org.openide.nodes.Children;

/**
 * Node that represents a variable.
 * 
 * @author Thomas Werner
 */
class VariableNode extends BeanShellNode {
    
    private final BshVariableInfo variable;

    public VariableNode(BshVariableInfo variable, JTextComponent connectedComponent) {
        super(Children.LEAF, connectedComponent);
        this.variable = variable;
        
        setDisplayName(buildDisplayName());
    }
    
    @Override
    public Image getIcon(int type) {
        if(variable.getModifiers().contains(BshModifierInfo.Private)) {
            return loadIcon(PRFX +(isStatic(variable) ? "variableStPrivate.png" : "variablePrivate.png"));
        } else if(variable.getModifiers().contains(BshModifierInfo.Protected)) {
            return loadIcon(PRFX +(isStatic(variable) ? "variableStProtected.png" : "variableProtected.png"));
        } else if(variable.getModifiers().contains(BshModifierInfo.Public)) {
            return loadIcon(PRFX +(isStatic(variable) ? "variableStPublic.png" : "variablePublic.png"));
        } else {
            return loadIcon(PRFX +(isStatic(variable) ? "variableStPackage.png" : "variablePackage.png"));
        }
    }
    
    @Override
    public int getLineNumber() {
        return variable.getLineNumber();
    }
    
     private String buildDisplayName() {
        final StringBuilder builder = new StringBuilder();
        builder.append(variable.getType())
               .append(" : ")
               .append(variable.getName());
        return builder.toString();
    }
     
    private boolean isStatic(BshVariableInfo variable) {
        return variable.getModifiers().contains(BshModifierInfo.Static);
    }
    
}
