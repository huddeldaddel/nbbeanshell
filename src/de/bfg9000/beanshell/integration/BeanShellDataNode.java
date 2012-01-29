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
package de.bfg9000.beanshell.integration;

import org.openide.loaders.DataNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;
import org.openide.util.lookup.ProxyLookup;

/**
 * A DataNode for BeanShell scripts.
 *
 * @author Thomas Werner
 */
public class BeanShellDataNode extends DataNode {

    public BeanShellDataNode(BeanShellDataObject dataObject) {
        super(dataObject, Children.LEAF, new ProxyLookup(dataObject.getLookup(), Lookups.fixed(dataObject)));
    }

}
