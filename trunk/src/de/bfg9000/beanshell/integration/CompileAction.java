package de.bfg9000.beanshell.integration;

import bsh.Parser;
import java.io.IOException;
import java.io.InputStream;
import org.openide.cookies.SaveCookie;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

/**
 * Action that parses the current script file. BeanShell scripts are not compiled. So this action just uses the Parser
 * to find syntax errors.
 *
 * @author Thomas Werner
 */
public class CompileAction {

    private final BeanShellDataObject context;

    public CompileAction(BeanShellDataObject context) {
        this.context = context;
    }

    public void perform() {
        final InputOutput io = IOProvider.getDefault().getIO("BeanShell", false);
        InputStream iStream = null;
        io.select();
        
        try {
            io.getOut().reset();
            
            if(context.isModified()) {
                final TopComponent tComponent = WindowManager.getDefault().getRegistry().getActivated();
                if(null != tComponent) {
                    final SaveCookie sCookie = tComponent.getLookup().lookup(SaveCookie.class);
                    if(null != sCookie)
                        sCookie.save();
                }
            }
            
            iStream = context.getPrimaryFile().getInputStream();
            final Parser parser = new Parser(iStream);
            parser.setRetainComments(true);
            
            io.getOut().println("Parsing BeanShell file: " +context.getPrimaryFile().getPath());
            try {
                while(!parser.Line())
                    io.getOut().println(parser.popNode());
                io.getOut().println("Finished parsing. No errors have been found!");
            } catch(Error err) {
                io.getErr().println(err.getMessage());
            }
            
        } catch(Exception ex) {
            ex.printStackTrace(io.getErr());
        } finally {
            io.getErr().close();
            io.getOut().close();
            
            try {
                iStream.close();
            } catch (IOException ex) { }
        }
    }
    
}
