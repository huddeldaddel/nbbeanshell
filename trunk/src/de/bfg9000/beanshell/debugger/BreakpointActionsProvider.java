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

import java.util.Collections;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.netbeans.api.debugger.ActionsManager;
import org.netbeans.api.debugger.Breakpoint;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.openide.filesystems.FileObject;
import org.openide.text.Line;

/**
 *
 * @author Thomas Werner
 */
public class BreakpointActionsProvider extends ActionsProviderSupport {

    private final static Set<Object> ACTIONS = Collections.singleton(ActionsManager.ACTION_TOGGLE_BREAKPOINT);

    public BreakpointActionsProvider() {
        setEnabled(ActionsManager.ACTION_TOGGLE_BREAKPOINT, true);
    }

    @Override
    public void doAction(Object action) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                FileObject fo = DebuggerUtils.getFileInEditor();
                if(!DebuggerUtils.isBeanShellSource(fo))
                    return;
                
                Line line = DebuggerUtils.getCurrentLine();

                Breakpoint[] breakpoints = DebuggerManager.getDebuggerManager().getBreakpoints();
                int i=0;
                int k = breakpoints.length;

                // remove the breakpoint if it exists
                for(i = 0; i < k; i++)
                    if((breakpoints[i] instanceof BeanShellBreakpoint) && 
                       (((BeanShellBreakpoint) breakpoints[i]).getLine() != null) && 
                       ((BeanShellBreakpoint) breakpoints[i]).getLine().equals(line)) {                        
                        BeanShellBreakpoint bp = DebuggerUtils.getBreakpointAtLine();
                        BreakpointContainer.Instance.toggeBreakpoint(bp.getLine().getLineNumber()+1,bp.getFileObject());
                        DebuggerManager.getDebuggerManager().removeBreakpoint(breakpoints[i]);
                        break;
                    }
                
                //otherwise create a new breakpoint
                if(i == k) { 
                    BeanShellBreakpoint bp = DebuggerUtils.getBreakpointAtLine();
                    BreakpointContainer.Instance.toggeBreakpoint(bp.getLine().getLineNumber()+1,bp.getFileObject());
                    DebuggerManager.getDebuggerManager().addBreakpoint(bp);
                }
            }
        });

    }

    @Override
    public Set getActions() {
        return ACTIONS;
    }
    
}
