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
import org.netbeans.api.debugger.*;

/**
 *
 * @author Thomas Werner
 */
public class DebuggerManagerListener extends DebuggerManagerAdapter {
    
    @Override
    public void breakpointAdded(Breakpoint brkpnt) {
        super.breakpointAdded(brkpnt);
        System.out.println("breakpointAdded: " +brkpnt);
    }

    @Override
    public void breakpointRemoved(Breakpoint brkpnt) {
        super.breakpointRemoved(brkpnt);
        System.out.println("breakpointRemoved: " +brkpnt);
    }

    @Override
    public void initWatches() {
        super.initWatches();
        System.out.println("initWatches");
    }

    @Override
    public void watchAdded(Watch watch) {
        super.watchAdded(watch);
        System.out.println("watchAdded: " +watch);
    }

    @Override
    public void watchRemoved(Watch watch) {
        super.watchRemoved(watch);
        System.out.println("watchRemoved: " +watch);
    }

    @Override
    public void sessionAdded(Session sn) {
        super.sessionAdded(sn);
        System.out.println("sessionAdded: " +sn);
    }

    @Override
    public void sessionRemoved(Session sn) {
        super.sessionRemoved(sn);
        System.out.println("sessionRemoved: " +sn);
    }

    @Override
    public void engineAdded(DebuggerEngine de) {
        super.engineAdded(de);
        System.out.println("engineAdded: " +de);
    }

    @Override
    public void engineRemoved(DebuggerEngine de) {
        super.engineRemoved(de);
        System.out.println("engineRemoved: " +de);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        System.out.println("propertyChange: " +evt);
    }
    
}
