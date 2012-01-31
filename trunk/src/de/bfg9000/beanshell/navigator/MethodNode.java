/*
 * nbBeanShell -- a integration of BeanShell into the NetBeans IDE
 * Copyright (C) 2012 Thomas Werner
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.bfg9000.beanshell.navigator;

import bsh.BshMethodInfo;
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
        return loadIcon(method.getIconPath());
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
        return method.toString();
    }
    
}