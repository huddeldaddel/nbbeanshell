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
package de.bfg9000.beanshell.debugger;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.api.debugger.DebuggerManagerAdapter;
import org.netbeans.spi.debugger.ui.BreakpointAnnotation;

/**
 * 
 * 
 * @author Thomas Werner
 */
public class BreakpointAnnotationListener extends DebuggerManagerAdapter implements PropertyChangeListener {
    
    private final HashMap<Breakpoint, BeanShellBreakpointAnnotation> breakpointToAnnotation;

    public BreakpointAnnotationListener() {
         breakpointToAnnotation = new HashMap<Breakpoint, BeanShellBreakpointAnnotation>();
    }
    
    @Override
    public String[] getProperties () {
        return new String[] {DebuggerManager.PROP_BREAKPOINTS};
    }

    /**
    * Called when some breakpoint is added.
    *
    * @param b breakpoint
    */
    @Override
    public void breakpointAdded (Breakpoint b) {
        if(b instanceof BeanShellBreakpoint)
            addAnnotation((BeanShellBreakpoint) b);                
    }

    /**
    * Called when some breakpoint is removed.
    *
    * @param breakpoint
    */
    @Override
    public void breakpointRemoved (Breakpoint b) {
        if(b instanceof BeanShellBreakpoint)
            removeAnnotation(b);        
    }

    /**
     * This method gets called when a bound property is changed.
     * @param evt A PropertyChangeEvent object describing the event source and the property that has changed.
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(!evt.getPropertyName().equals(Breakpoint.PROP_ENABLED)) 
            return;
        
        removeAnnotation((Breakpoint) evt.getSource ());
        addAnnotation((BeanShellBreakpoint) evt.getSource());
    }

    private void addAnnotation(BeanShellBreakpoint b) {
        breakpointToAnnotation.put(b, new BeanShellBreakpointAnnotation(b.getLine(), b));
        b.addPropertyChangeListener(Breakpoint.PROP_ENABLED, this);
    }

    private void removeAnnotation (Breakpoint b) {
        BreakpointAnnotation annotation = breakpointToAnnotation.remove (b);
        if(annotation == null) 
            return;

        annotation.detach();
        b.removePropertyChangeListener(Breakpoint.PROP_ENABLED, this);
    }
    
}
