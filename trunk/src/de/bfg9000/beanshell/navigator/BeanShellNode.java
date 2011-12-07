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

import java.awt.Image;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * This abstract node implements the common behaviour of the various nodes used in the Navigator.
 * 
 * @author Thomas Werner
 */
abstract class BeanShellNode extends AbstractNode {
    
    protected static final String PRFX = "/de/bfg9000/beanshell/icons/";
    
    public BeanShellNode(Children children) {
        super(children);
    }

    public BeanShellNode(Children children, Lookup lookup) {
        super(children, lookup);
    }
    
    public abstract int getLineNumber();
    
    protected Image loadIcon(String path) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            System.out.println(ex);
            return null;
        }
    }
    
}
