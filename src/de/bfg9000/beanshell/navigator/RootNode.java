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

import bsh.ParseException;
import bsh.ParserConnector;
import javax.swing.text.JTextComponent;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 * The root node that will be used (but not displayed) in the BeanShellNavigatorPanel.
 * 
 * @author Thomas Werner
 */
class RootNode extends AbstractNode {

    public RootNode(String scriptContent, JTextComponent connectedComponent) throws ParseException {
        super(Children.create(new NodeFactory(new ParserConnector().parse(scriptContent), connectedComponent), false));        
    }

}
