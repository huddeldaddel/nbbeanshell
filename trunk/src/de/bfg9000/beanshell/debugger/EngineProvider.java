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

import org.netbeans.api.debugger.DebuggerEngine;
import org.netbeans.spi.debugger.DebuggerEngineProvider;

/**
 *
 * @author Thomas Werner
 */
public class EngineProvider extends DebuggerEngineProvider {

    private DebuggerEngine.Destructor destructor;

    @Override
    public String[] getLanguages() {
        return new String[] {"BeanShell"};
    }

    @Override
    public String getEngineTypeID() {
        return "BeanShellDebuggerEngine";
    }

    @Override
    public Object[] getServices() {
        return new Object[]{};
    }

    @Override
    public void setDestructor (DebuggerEngine.Destructor destructor) {
        this.destructor = destructor;
    }

    public DebuggerEngine.Destructor getDestructor () {
        return destructor;
    }
    
}
