/*********************************************************************************************************************** 
 *  nbBeanShell -- a integration of BeanShell into the NetBeans IDE.                                                    *
 *  Copyright (C) 2011 Thomas Werner                                                                                   *
 *                                                                                                                     *
 *  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public  *
 *  License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any     *
 *  later version.                                                                                                     *
 *                                                                                                                     *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 *  warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more       *
 *  details.                                                                                                           *
 *                                                                                                                     *
 *  You should have received a copy of the GNU General Public License along with this program.  If not, see            *
 *  <http://www.gnu.org/licenses/>.                                                                                    *
 **********************************************************************************************************************/
package de.bfg9000.beanshell.navigator;

import bsh.BshInfoContainer;
import bsh.BshMethodInfo;
import bsh.BshVariableInfo;
import java.util.*;
import javax.swing.text.JTextComponent;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author Thomas Werner
 */
class NodeFactory extends ChildFactory {

    private final BshInfoContainer bshInfoContainer;
    private final JTextComponent connectedComponent;
    
    public NodeFactory(BshInfoContainer bshInfoContainer, JTextComponent connectedComponent) {
        this.bshInfoContainer = bshInfoContainer;
        this.connectedComponent = connectedComponent;
    }
    
    @Override
    protected boolean createKeys(List toPopulate) {
        final List<BshMethodInfo> classes = bshInfoContainer.getClasses();
        final List<BshMethodInfo> methods = bshInfoContainer.getMethods();
        final List<BshVariableInfo> variables = bshInfoContainer.getVariables();        
        
        // Filter methods (to remove classes), then sort and add them
        final List<BshMethodInfo> methodsToAdd = new LinkedList<BshMethodInfo>(methods);
        final Iterator<BshMethodInfo> iterator = methodsToAdd.iterator();
        while(iterator.hasNext())
            if(classes.contains(iterator.next()))
                iterator.remove();
        Collections.sort(methodsToAdd, new BshMethodInfoComparator());
        toPopulate.addAll(methodsToAdd);
        
        // Sort variables and add them
        if((!(bshInfoContainer instanceof BshMethodInfo)) || ((BshMethodInfo) bshInfoContainer).isClass()) {
            Collections.sort(variables, new BshVariableInfoComparator());
            toPopulate.addAll(variables);
        }
        
        // Sort classes and add them
        Collections.sort(classes, new BshMethodInfoComparator());
        toPopulate.addAll(classes);
        
        return true;
    }
    
    @Override
    protected Node createNodeForKey(Object key) {
        if(key instanceof BshMethodInfo) {
            final ChildFactory factory = new NodeFactory((BshMethodInfo) key, connectedComponent);
            return new MethodNode((BshMethodInfo) key, Children.create(factory, false), connectedComponent);
        }
        
        if(key instanceof BshVariableInfo)
            return new VariableNode((BshVariableInfo) key, connectedComponent);
        
        return null;
    }
    
    /**
     * Comparator for BshMethodInfo objects.
     */
    private static final class BshMethodInfoComparator implements Comparator<BshMethodInfo> {
        
        @Override
        public int compare(BshMethodInfo o1, BshMethodInfo o2) {
            return o1.getName().compareTo(o2.getName());
        }
        
    }
    
    /**
     * Comparator for BshVariableInfo objects.
     */
    private static final class BshVariableInfoComparator implements Comparator<BshVariableInfo> {

        @Override
        public int compare(BshVariableInfo o1, BshVariableInfo o2) {
            return o1.getName().compareTo(o2.getName());
        }
        
    }
    
}
