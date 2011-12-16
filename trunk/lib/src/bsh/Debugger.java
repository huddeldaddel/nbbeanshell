package bsh;

import java.io.PrintStream;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

/**
 * A debugger that executes BeanShell scripts node by node.
 * 
 * @author Thomas Werner
 */
public class Debugger extends Interpreter {
    
    private static enum CommandResult { Ok, Break, Cancel }
    
    private List<DebuggerListener> listeners = new LinkedList<DebuggerListener>();    
    private final BreakpointProvider breakpointProvider;
    
    private SimpleNode parkedNode;          // node to be handled next
    private Object nodeStatus = null;       // status of the parkedNode
    
    private boolean stepByStep = false;     // step through the code => all lines are breakpoints
    private SimpleNode lastNode = null;     // node that be just stopped at
    
    private Debugger localInterpreter;
    private CallStack callstack;
    private boolean endOfFile = false;
    
    /**
     * Create a debugger for external use.
     */
    public Debugger(BreakpointProvider breakpointProvider) {
        super();
        this.breakpointProvider = breakpointProvider;
    }
    
    /**
     * Creates a debugger for internal use.
     * 
     * @param namespace If namespace is non-null then this interpreter's root namespace will be set to the one provided.
     *                  If it is null a new one will be created for it.
     * @param parent The parent interpreter if this interpreter is a child of another. May be null. Children share a 
     *               BshClassManager with their parent instance.
     * @param sourceFileInfo An informative string holding the filename or other description of the source from which 
     *                       this interpreter is reading... used for debugging.  May be null.
     */
    private Debugger(Reader in, PrintStream out, PrintStream err, boolean interactive, NameSpace namespace,
                     Interpreter parent, String sourceFileInfo, BreakpointProvider breakpointProvider) {
        super(in, out, err, interactive, namespace, parent, sourceFileInfo);
        this.breakpointProvider = breakpointProvider;
    }    
    
    public void addDebuggerListener(DebuggerListener listener) {
        listeners.add(listener);
    }
    
    public void removeDebuggerListener(DebuggerListener listener) {
        listeners.remove(listener);
    }
    
    private void setDebuggerListeners(List<DebuggerListener> listeners) {
        this.listeners = listeners;
    }
    
    /**
     * Spawn a non-interactive local debugger to evaluate text in the specified namespace. Return value is the 
     * evaluated object (or corresponding primitive wrapper).
     * 
     * @param sourceFileInfo identifies the script in the BreakpointContainer
     * @throws EvalError on script problems
     * @throws TargetError on unhandled exceptions from the script
     */
    public void debug(Reader in, String sourceFileInfo) throws EvalError {
        this.sourceFileInfo = sourceFileInfo;
        
        localInterpreter = new Debugger(in, out, err, false, globalNameSpace, this, sourceFileInfo, breakpointProvider);
        localInterpreter.setDebuggerListeners(listeners);
	callstack = new CallStack(globalNameSpace);
        endOfFile = false;
        
        boolean stopped = false;
        while(!(endOfFile || stopped)) {
            SimpleNode currentNode = null;
            boolean executed = false;
            try {
                currentNode = getNextNode();
                if(currentNode != null) {
                    final CommandResult result = executeCommand(currentNode, null);
                    switch(result) {
                        case Break:
                            executed = false;
                            stopped = false;
                            fireStopped(currentNode.getLineNumber());
                            return;
                        case Cancel:
                            executed = true;
                            stopped = true;
                            break;
                        case Ok:
                            executed = true;
                            stopped = false;
                            break;
                    }
                }
            } catch(Throwable t) {
                handleException(currentNode, t);
            } finally {
                if(executed)
                    cleanUp();
            }
        }
        
        if(endOfFile || stopped)
            fireFinished();
    }    
    
    /**
     * Fired when a node has been reached - but not executed yet. We will execute it and stop at the next node.
     * 
     * @throws EvalError on script problems
     * @throws TargetError on unhandled exceptions from the script
     */
    public void stepOver() throws EvalError {
        final SimpleNode node = localInterpreter.parkedNode;
        final Object resumeStatus = localInterpreter.nodeStatus;
        
        localInterpreter.parkedNode = null;
        localInterpreter.nodeStatus = null;
        
        final CommandResult result = executeCommand(node, resumeStatus);
        
//        try {
//            if(node != null) {
//                boolean stopped = false;
//                if(!executeCommand(node) || endOfFile) {
//                    stopped = true;
//                    fireFinished();
//                }
//                cleanUp();
//            
//                if(!stopped) {
//                    node = getNextNode();
//                    if(null != node) {
//                        fireStopped(node.getLineNumber());
//                    } else
//                        fireFinished();
//                }
//            }
//        } catch(Throwable t) {
//            handleException(node, t);
//        }
    }
    
    //- Interface used by nodes - will be called in localInterpreter ---------------------------------------------------
    
    void parkNode(SimpleNode nodeToPark, Object nodeStatus) {
        this.parkedNode = nodeToPark;
        this.nodeStatus = nodeStatus;
        this.lastNode = nodeToPark;
        fireStopped(parkedNode.getLineNumber());
    }
    
    boolean isBreakpoint(SimpleNode node) {
        return (node != lastNode) && 
               (stepByStep || breakpointProvider.isBreakpoint(node.getLineNumber(), node.getSourceFile()));
    }
    
    //- End of interface used by nodes ---------------------------------------------------------------------------------
    
    /**
     * @return {@code false} if the script is to be stopped
     * @throws EvalError on script problems
     * @throws TargetError on unhandled exceptions from the script 
     */
    private CommandResult executeCommand(SimpleNode node, Object nodeStatus) throws EvalError {
        final Object returnValue = node.eval(callstack, localInterpreter, nodeStatus);
        if(localInterpreter.parkedNode == node)
            return CommandResult.Break;
        return (returnValue instanceof ReturnControl) ? CommandResult.Cancel : CommandResult.Ok;
    }    
    
    private void fireFinished() {
        final DebugEvent event = new DebugEvent(this, DebugEvent.Type.Finished, 0, sourceFileInfo);
        for(DebuggerListener listener: listeners)
            listener.debuggerStopped(event);
    }
    
    private void fireStopped(int line) {
        final DebugEvent event = new DebugEvent(this, DebugEvent.Type.Stopped, line, sourceFileInfo);
        for(DebuggerListener listener: listeners)
            listener.debuggerStopped(event);
    }
    
    private SimpleNode getNextNode() throws EvalError {
        SimpleNode result = null;
        try {
            endOfFile = localInterpreter.Line();
            if(localInterpreter.get_jjtree().nodeArity() > 0) {
                result = (SimpleNode)localInterpreter.get_jjtree().rootNode();
                result.setSourceFile(sourceFileInfo);
            }
        } catch(ParseException ex) {
            ex.setErrorSourceFile(sourceFileInfo);
            throw ex;
        }
        return result;
    }
    
    private void handleException(SimpleNode node, Throwable throwable) throws EvalError {
        if(throwable instanceof InterpreterError) {
            throwable.printStackTrace(err);
            throw new EvalError(sourceFileInfo+": internal Error: " + throwable.getMessage(), node, callstack);
        }
        
        if(throwable instanceof TargetError) {
            TargetError e = (TargetError) throwable;
            if(e.getNode()==null)
                e.setNode(node);
            e.reThrow(sourceFileInfo);
        }
        
        if(throwable instanceof EvalError) {
            EvalError e = (EvalError) throwable;
            if(e.getNode()==null)
                e.setNode(node);
            e.reThrow(sourceFileInfo);
        }
        
        if(throwable instanceof Exception) 
            throw new EvalError(sourceFileInfo+": unknown error: " + throwable.getMessage(), node, callstack);
        
        if(throwable instanceof TokenMgrError) 
            throw new EvalError(sourceFileInfo+": Token Parsing Error: " + throwable.getMessage(), node, callstack);
    }
    
    private void cleanUp() {
        localInterpreter.get_jjtree().reset();
        if(callstack.depth() > 1) {
            callstack.clear();
            callstack.push(globalNameSpace);
        }
    }
    
}
