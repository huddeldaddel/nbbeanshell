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
package de.bfg9000.beanshell.debugger;

import bsh.DebugEvent;
import bsh.DebuggerListener;
import bsh.EvalError;
import de.bfg9000.beanshell.integration.AbstractAction;
import de.bfg9000.beanshell.util.WriterOutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.HashSet;
import java.util.Set;
import javax.swing.SwingUtilities;
import org.netbeans.api.debugger.DebuggerInfo;
import org.netbeans.api.debugger.DebuggerManager;
import org.netbeans.spi.debugger.ActionsProviderSupport;
import org.netbeans.spi.debugger.ContextProvider;
import org.netbeans.spi.debugger.DebuggerEngineProvider;
import org.netbeans.spi.debugger.SessionProvider;
import org.openide.windows.InputOutput;

/**
 * This class houses the primary implementation of the BeanShell debugger. Based on a tutorial written by Andreas 
 * Stefik (http://netbeans.dzone.com/how-reuse-netbeans-debugger).
 * 
 * @author Thomas Werner
 */
public class Debugger extends ActionsProviderSupport implements DebuggerListener {
    
    /** Action constant for Step Over Action. */
    public static final Object ACTION_STEP_OVER = "stepOver";
    /** Action constant for breakpoint hit action. */
    public static final Object ACTION_RUN_INTO_METHOD = "runIntoMethod";
    /** Action constant for Step Into Action. */
    public static final Object ACTION_STEP_INTO = "stepInto";
        

    /** Action constant for Step Out Action. */
    public static final Object ACTION_STEP_OUT = "stepOut";
    /** Action constant for Step Operation Action. */
    public static final Object ACTION_STEP_OPERATION = "stepOperation";
    /** Action constant for Continue Action. */
    public static final Object ACTION_CONTINUE = "continue";
    /** Action constant for Continue Action. */
    public static final Object ACTION_REWIND = "rewind";
    /** Action constant for Start Action. */
    public static final Object ACTION_START = "start";
    /** Action constant for Kill Action. */
    public static final Object ACTION_KILL = "kill";
    /** Action constant for Make Caller Current Action. */
    public static final Object ACTION_MAKE_CALLER_CURRENT = "makeCallerCurrent";
    /** Action constant for Make Callee Current Action. */
    public static final Object ACTION_MAKE_CALLEE_CURRENT = "makeCalleeCurrent";
    /** Action constant for Pause Action. */
    public static final Object ACTION_PAUSE = "pause";
    /** Action constant for Run to Cursor Action. */
    public static final Object ACTION_RUN_TO_CURSOR = "runToCursor";
    /** Action constant for Run to Cursor Action. */
    public static final Object ACTION_RUN_BACK_TO_CURSOR = "runBackToCursor";
    /** Action constant for Pop Topmost Call Action. */
    public static final Object ACTION_POP_TOPMOST_CALL = "popTopmostCall";
    /** Action constant for Fix Action. */
    public static final Object ACTION_FIX = "fix";
    /** Action constant for Restart Action. */
    public static final Object ACTION_RESTART = "restart";
    /** Action constant for Toggle Breakpoint Action. */
    public static final Object ACTION_TOGGLE_BREAKPOINT = "toggleBreakpoint";

    private final EngineProvider engineProvider;
    private final bsh.Debugger debugger = new bsh.Debugger(BreakpointContainer.Instance);
    private final ProgramCounterAnnotationUpdater pcUpdater = new ProgramCounterAnnotationUpdater();
    private static final Set actions = new HashSet();

    public static final String BEANSHELL_DEBUGGER_INFO = "BeanShellDebuggerInfo";
    public static final String BEANSHELL_SESSION = "BeanShellSession";

    public Debugger(ContextProvider contextProvider) {
        // if we not enable the actions, our debugger will show them as greyed out by default, in both the menus and the 
        // toolbar.
        engineProvider = (EngineProvider) contextProvider.lookupFirst(null, DebuggerEngineProvider.class);
        for(Object action: actions)
            setEnabled(action, true);
        
        debugger.addDebuggerListener(this);
        debugger.addDebuggerListener(pcUpdater);
    }

    /**
     * Make an array of actions for convenience.
     */
    static {
        actions.add(ACTION_RUN_BACK_TO_CURSOR);
        actions.add(ACTION_REWIND);
        actions.add(ACTION_KILL);
        actions.add(ACTION_PAUSE);
        actions.add(ACTION_CONTINUE);
        actions.add(ACTION_START);
        actions.add(ACTION_STEP_INTO);
        actions.add(ACTION_STEP_OVER);
        actions.add(ACTION_RUN_TO_CURSOR);
    }

    /**
     * This method starts the debugger. Don't worry about creating a similar stopDebugger method, as this is taken care
     * of by our set of defined actions. This method literally starts the debugger, by passing a DebuggerInfo instance
     * to NetBeans DebuggerManager class.
     */
    public static void startDebugger() {
        final Object[] services = new Object[]{new BeanShellSessionProvider(), new BeanShellDebuggerCookie()};
        DebuggerManager.getDebuggerManager().startDebugging(DebuggerInfo.create(BEANSHELL_DEBUGGER_INFO, services));
    }

    /**
     * This is where we implement (or delegate), the implementation of our debugger. In other words, this is where we 
     * tell our debugger implementation to step over, into, stop, or to take other custom operations.
     * 
     * @param action
     */
    @Override
    public void doAction(Object action) {
        if(action == ACTION_RUN_BACK_TO_CURSOR) {
        } else if(action == ACTION_REWIND) {
        } else if(action == ACTION_KILL) {
            engineProvider.getDestructor().killEngine();
            pcUpdater.removeAnnotation();
        } else if(action == ACTION_PAUSE) {
        } else if(action == ACTION_CONTINUE) {
        } else if(action == ACTION_START) {
            new ScriptRunner().performAction(false);
        } else if(action == ACTION_STEP_INTO) {
        } else if(action == ACTION_STEP_OVER) {
            try {
                debugger.stepOver();
            } catch(EvalError ee) {
                System.out.println(ee);
            }
        } else if(action == ACTION_RUN_TO_CURSOR) {
        }
        
        System.out.println("The debugger took the action: " + action);
    }

    @Override
    public Set<Object> getActions() {
        return actions;
    }

    @Override
    public void debuggerStopped(DebugEvent de) {
        if(de.getType() == DebugEvent.Type.Finished)
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    doAction(ACTION_KILL);
                }
            });
    }
    
    private static final class BeanShellSessionProvider extends SessionProvider {
        
        @Override
        public String getSessionName() {
            return "BeanShell Script";
        }

        @Override
        public String getLocationName() {
            return "localhost";
        }

        @Override
        public String getTypeID() {
            return BEANSHELL_SESSION;
        }

        @Override
        public Object[] getServices() {
            return new Object[]{};
        }
        
    }
    
    private static final class BeanShellDebuggerCookie { }
    
    /**
     * Runs the script in an interpreter that is connected to this Debugger instance.
     */
    private final class ScriptRunner extends AbstractAction {

        public ScriptRunner() {
            super(null);
        }
                
        @Override
        protected void perform(InputOutput io) throws Throwable {
            String script = DebuggerUtils.getFileInEditor().asText();
            script = script.endsWith(";") ? script : script +";";
            
            debugger.setErr(new PrintStream(new WriterOutputStream(io.getErr())));
            debugger.setOut(new PrintStream(new WriterOutputStream(io.getOut())));
            debugger.debug(new StringReader(script), DebuggerUtils.getFileInEditor().getPath());
        }
        
        @Override
        protected void performAction(boolean writeToLog) {
            super.performAction(writeToLog);
        }
        
        @Override
        protected String getTaskName() {
            return "";
        }
            
    };
    
}
