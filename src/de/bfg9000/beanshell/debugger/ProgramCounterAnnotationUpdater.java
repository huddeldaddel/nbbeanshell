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
import javax.swing.JEditorPane;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import org.netbeans.modules.editor.NbEditorUtilities;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.EditorCookie.Observable;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.loaders.DataObjectNotFoundException;
import org.openide.text.Line;

/**
 * This class updates the debugger annotation associated with the program counter. In short, when the user is debugging
 * a program, this class ensures that the highlighted line shows up at the appropriate place. Based on the class 
 * {@code org.sodbeans.debugger.hop.ProgramCounterAnnotationUpdater} from the SodBeans project that has been written by 
 * Andreas Stefik.
 * 
 * @author Thomas Werner
 */
public class ProgramCounterAnnotationUpdater implements DebuggerListener {
    
    private static LineAnnotation annotation = new LineAnnotation();    

    /**
     * Does the actual updating of the line in the event dispatch thread.
     *
     * @param line
     */
    private void update(final Line line) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                annotation.attach(line);
            }
        });

    }

    /**
     * Removes the program counter line, indicating that the debugger has stopped.
     */
    public void removeAnnotation() {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                annotation.detach();
            }
        });
    }

    /**
     * Opens an editor on the screen.
     *
     * TODO: If opening a system file, make it open in a read only editor.
     * 
     * @param dataObj
     */
    private void openEditor(DataObject dataObj) {
        final Observable ec = dataObj.getCookie(Observable.class);
        if (ec != null) {
            javax.swing.SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    ec.open();
                }
            });
        }
    }

    private void openEditorAndJump(final DataObject dataObj, final int line) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    EditorCookie ck = dataObj.getCookie(EditorCookie.class);
                    final Observable ec = dataObj.getCookie(Observable.class);
                    if (ck != null && ec != null) {
                        ec.open();
                        //open the document
                        JEditorPane[] p = ck.getOpenedPanes();
                        if (p.length > 0) {
                            //Need to do this since we're disabling the window system's
                            //auto focus mechanism
                            p[0].requestFocus();
                            if (dataObj != null) {
                                LineCookie lc = dataObj.getCookie(LineCookie.class);
                                if (lc == null) {
                                    return;
                                }
                                Line l = lc.getLineSet().getOriginal(line);
                                l.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS);
                            }
                        }

                        //highlight the line
                        StyledDocument document = ck.getDocument();
                        if(document != null) {
                            Element e = document.getDefaultRootElement();
                            if (e != null && line != -1) {
                                e = e.getElement(line);
                                final int startOfLine = e.getStartOffset();
                                Line myLine = NbEditorUtilities.getLine(document, startOfLine, false);
                                update(myLine);
                            }
                        }
                    }
                } catch (Exception exception) {
                }
            }
        });
    }

    /**
     * Forces the editor to jump to the line in question, useful for debugging.
     *
     * @param dataObj
     * @param line
     */
    private void jumpToLine(final DataObject dataObj, final int line) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    EditorCookie ck = dataObj.getCookie(EditorCookie.class);
                    if (ck != null) {
                        //ck.openDocument();
                        JEditorPane[] p = ck.getOpenedPanes();
                        if (p.length > 0) {
                            //Need to do this since we're disabling the window system's
                            //auto focus mechanism
                            p[0].requestFocus();
                            if (dataObj != null) {
                                LineCookie lc = dataObj.getCookie(LineCookie.class);
                                if (lc == null) {
                                    return;
                                }
                                Line l = lc.getLineSet().getOriginal(line);
                                l.show(Line.ShowOpenType.OPEN, Line.ShowVisibilityType.FOCUS);
                            }
                        }
                    }
                } catch (Exception exception) {
                }
            }
        });
    }

    /**
     * Updates the program counter.
     */
    @Override
    public void debuggerStopped(DebugEvent de) {
        if(DebugEvent.Type.Finished == de.getType()) {
            removeAnnotation();
            return;
        }
        
        final FileObject fo = DebuggerUtils.getFileInEditor();
        if(fo == null)
            return;
        
        DataObject dataObject = null;
        try { // if this is not the file, open it
            dataObject = DataObject.find(fo);
        } catch (DataObjectNotFoundException exception) { }

        try {
            EditorCookie ck = dataObject.getCookie(EditorCookie.class);
            if(ck != null) {
                StyledDocument document = ck.getDocument();
                int lineNumber = de.getLine() -1;
                if(document != null) {
                    Element e = document.getDefaultRootElement();
                    if(e != null && lineNumber != -1) {
                        e = e.getElement(lineNumber);
                        final int startOfLine = e.getStartOffset();
                        update(NbEditorUtilities.getLine(document, startOfLine, false));
                        jumpToLine(dataObject, lineNumber);
                    } else if(dataObject != null) {
                        openEditor(dataObject);
                    }
                } else if(dataObject != null) {
                    openEditorAndJump(dataObject, lineNumber);
                }
            }
        } catch (Exception exception) { }
    }

}
