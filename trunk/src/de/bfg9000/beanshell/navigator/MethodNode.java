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

import bsh.BshMethodInfo;
import bsh.BshModifierInfo;
import bsh.BshParameterInfo;
import bsh.ParserConnector;
import java.awt.Image;
import javax.swing.text.JTextComponent;
import org.openide.nodes.Children;

/**
 * Node that represents a method.
 * 
 * @author Thomas Werner
 */
class MethodNode extends BeanShellNode {    
    
    private final BshMethodInfo method;

    public MethodNode(BshMethodInfo method, Children children, JTextComponent connectedComponent) {
        super(children, connectedComponent);
        this.method = method;
        
        setDisplayName(buildDisplayName());
    }
    
    @Override
    public Image getIcon(int type) {
        if(method.isClass())
            return loadIcon(PRFX +"class.png");
                
        if(method.getModifiers().contains(BshModifierInfo.Private)) {
            return loadIcon(PRFX +(isStatic(method) ? "methodStPrivate.png" : "methodPrivate.png"));
        } else if(method.getModifiers().contains(BshModifierInfo.Protected)) {
            return loadIcon(PRFX +(isStatic(method) ? "methodStProtected.png" : "methodProtected.png"));
        } else if(method.getModifiers().contains(BshModifierInfo.Public)) {
            return loadIcon(PRFX +(isStatic(method) ? "methodStPublic.png" : "methodPublic.png"));
        } else {
            return loadIcon(PRFX +(isStatic(method) ? "methodStPackage.png" : "methodPackage.png"));
        }
    }
    
    @Override
    public Image getOpenedIcon(int type) {
        return getIcon(type);
    }
    
    @Override
    public int getLineNumber() {
        return method.getLineNumber();
    }
    
    private String buildDisplayName() {
        if(method.isClass())
            return method.getName();
        
        final StringBuilder builder = new StringBuilder();
        builder.append(method.getName())
               .append("(");
        
        boolean first = true;
        for(BshParameterInfo pInfo: method.getParameters()) {
            if(!first) 
                builder.append(", ");
            first = false;
            
            if(!pInfo.getType().equals(ParserConnector.LOOSE_TYPE))
                builder.append(pInfo.getType()).append(" ");
            builder.append(pInfo.getName());
        }
        builder.append(")");
        
        if(!(method.isClass() || "void".equals(method.getReturnType())))
            builder.append(" : ").append(method.getReturnType());
               
        return builder.toString();
    }
    
    private boolean isStatic(BshMethodInfo method) {
        return method.getModifiers().contains(BshModifierInfo.Static);
    }
    
}
