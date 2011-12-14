package bsh;

import java.io.PrintStream;
import java.io.Reader;

/**
 * A basic interface for a debugger
 * 
 * @author Thomas Werner
 */
public class Debugger extends Interpreter {
    
    /**
     * Create a debugger for external use.
     */
    public Debugger() {
        super();
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
                     Interpreter parent, String sourceFileInfo) {
        super(in, out, err, interactive, namespace, parent, sourceFileInfo);
    }    
    
    /**
     * Spawn a non-interactive local interpreter to evaluate text in the specified namespace. Return value is the 
     * evaluated object (or corresponding primitive wrapper).
     * 
     * @param sourceFileInfo is for information purposes only.  It is used to display error messages (and in the future 
     *                       may be made available to the script).
     * @throws EvalError on script problems
     * @throws TargetError on unhandled exceptions from the script
     */
    //Note: we need a form of eval that passes the callstack through...
    /*
     * Can't this be combined with run()? run seems to have stuff in it for interactive vs. non-interactive...
     * compare them side by side and see what they do differently, aside from the exception handling.
     */
    public Object debug(Reader in, String sourceFileInfo) throws EvalError {
        Object retVal = null;

        /* 
         * Create non-interactive local interpreter for this namespace with source from the input stream and out/err 
         * same as this interpreter.
         */
        Debugger localInterpreter = new Debugger(in, out, err, false, globalNameSpace, this, sourceFileInfo);
	CallStack callstack = new CallStack(globalNameSpace);

        boolean eof = false;
        while(!eof) {
            SimpleNode node = null;
            try {
                eof = localInterpreter.Line();
                if(localInterpreter.get_jjtree().nodeArity() > 0) {
                    node = (SimpleNode)localInterpreter.get_jjtree().rootNode();
                    node.setSourceFile(sourceFileInfo);
                    
                    retVal = node.eval(callstack, localInterpreter);

                    // sanity check during development
                    if(callstack.depth() > 1)
                        throw new InterpreterError("Callstack growing: "+callstack);

                    if(retVal instanceof ReturnControl) {
                        retVal = ((ReturnControl)retVal).value;
                        break; // non-interactive, return control now
                    }

                    if(localInterpreter.showResults && retVal != Primitive.VOID)
                        println("<" + retVal + ">");
                }
            } catch(ParseException e) {
                if(DEBUG) // show extra "expecting..." info
                    error( e.getMessage(DEBUG) );

                // add the source file info and throw again
                e.setErrorSourceFile( sourceFileInfo );
                throw e;

            } catch ( InterpreterError e ) {
                e.printStackTrace();
                throw new EvalError("Sourced file: "+sourceFileInfo+" internal Error: " + e.getMessage(), node, callstack);
            } catch ( TargetError e ) {
                // failsafe, set the Line as the origin of the error.
                if(e.getNode()==null)
                    e.setNode( node );
                e.reThrow("Sourced file: "+sourceFileInfo);
            } catch ( EvalError e) {
                if(DEBUG)
                    e.printStackTrace();
                // failsafe, set the Line as the origin of the error.
                if(e.getNode()==null)
                    e.setNode( node );
                e.reThrow("Sourced file: "+sourceFileInfo);
            } catch(Exception e) {
                if(DEBUG)
                    e.printStackTrace();
                throw new EvalError("Sourced file: "+sourceFileInfo+" unknown error: " + e.getMessage(), node, callstack);
            } catch(TokenMgrError e) {
                throw new EvalError("Sourced file: "+sourceFileInfo+" Token Parsing Error: " + e.getMessage(), node, callstack );
            } finally {
                localInterpreter.get_jjtree().reset();

                // reinit the callstack
                if(callstack.depth() > 1 ) {
                    callstack.clear();
                    callstack.push(globalNameSpace);
                }
            }
        }
        
        return Primitive.unwrap(retVal);
    }
    
}
