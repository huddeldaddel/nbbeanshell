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
package de.bfg9000.beanshell.integration;

import java.io.IOException;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.cookies.SaveCookie;
import org.openide.windows.*;

/**
 * Abstract base class for script actions. This base class handles the connection to the progressbar and offers routines
 * to store the document currently opened in the editor.
 * 
 * @author Thomas Werner
 */
public abstract class AbstractAction implements Runnable {
    
    protected final BeanShellDataObject context;
    
    protected AbstractAction(BeanShellDataObject context) {
        this.context = context;
    }
    
    @Override
    public void run() {
        final ProgressHandle progress = ProgressHandleFactory.createHandle(getTaskName());
        progress.start();
        try {
            performAction(true);
        } finally {
            progress.finish();
        }
    }
    
    protected abstract void perform(InputOutput io) throws Throwable;
    
    protected abstract String getTaskName(); 
    
    protected void performAction(boolean writeToLog) {
        long beginTime = System.currentTimeMillis();
        final InputOutput io = IOProvider.getDefault().getIO("BeanShell", false);
        io.select();
        try {
            io.getOut().reset();
            perform(io);
            long endTime = System.currentTimeMillis();
            if(writeToLog)
                writeEndMessage(io.getOut(), true, endTime -beginTime);             
        } catch(Throwable ex) {
            io.getErr().println(ex.toString());            
            long endTime = System.currentTimeMillis();
            if(writeToLog)
                writeEndMessage(io.getOut(), false, endTime -beginTime);
        } finally {
            io.getErr().close();
            io.getOut().close();
        }
    }
    
    private void writeEndMessage(OutputWriter writer, boolean success, long time) {
        final String msg = success ? "Execution successful" : "Execution failure";
        if(time < 1000)
            writer.println(msg +" (" +time +" milliseconds)");
        else if(time < 5 * 60 *1000)
            writer.println(msg +" (" +(time /1000) +" seconds)");
        else 
            writer.println(msg +" (" +(time /1000 /60)  +" minutes and " + (time /1000 %60) +"seconds)");
    }
    
    protected void saveScriptDocument() throws IOException {
        if(context.isModified()) {
            final TopComponent tComponent = WindowManager.getDefault().getRegistry().getActivated();
            if(null != tComponent) {
                final SaveCookie sCookie = tComponent.getLookup().lookup(SaveCookie.class);
                if(null != sCookie)
                    sCookie.save();
            }
        }
    }
    
}
