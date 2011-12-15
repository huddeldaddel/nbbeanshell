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

import javax.swing.JEditorPane;
import javax.swing.text.Caret;
import javax.swing.text.StyledDocument;
import org.openide.cookies.EditorCookie;
import org.openide.cookies.LineCookie;
import org.openide.filesystems.FileObject;
import org.openide.loaders.DataObject;
import org.openide.nodes.Node;
import org.openide.text.Line;
import org.openide.text.NbDocument;
import org.openide.windows.TopComponent;

/**
 * Based on the class {@code org.sodbeans.debugger.DebuggerUtils} from the SodBeans project that has been written by 
 * Andreas Stefik. The code has been taken largely from jean-yves Mengant's org.netbeans.modules.python.debugger.Utils
 * in the python.debugger module.
 * 
 * @author Jean-Yves Mengant
 * @author Andreas Stefik
 * @author Thomas Werner
 */
public class DebuggerUtils {

    private final static String HOP_FILE_EXTENSION = "bsh";

    /**
     * Returns a Line object if it is valid for this particular debugger.
     * 
     * @return
     */
    public static Line getCurrentLine() {
        Node[] nodes = TopComponent.getRegistry().getCurrentNodes();
        if (nodes == null) {
            return null;
        }
        if (nodes.length != 1) {
            return null;
        }
        Node n = nodes[0];
        FileObject fo = n.getLookup().lookup(FileObject.class);
        if (fo == null) {
            DataObject dobj = n.getLookup().lookup(DataObject.class);
            if (dobj != null) {
                fo = dobj.getPrimaryFile();
            }
        }
        if (fo == null) {
            return null;
        }
        if (!isBeanShellSource(fo)) {
            return null;
        }
        LineCookie lineCookie = n.getCookie(LineCookie.class);
        if (lineCookie == null) {
            return null;
        }
        EditorCookie editorCookie = n.getCookie(EditorCookie.class);
        if (editorCookie == null) {
            return null;
        }
        JEditorPane jEditorPane = getEditorPane(editorCookie);
        if (jEditorPane == null) {
            return null;
        }
        StyledDocument document = editorCookie.getDocument();
        if (document == null) {
            return null;
        }
        Caret caret = jEditorPane.getCaret();
        if (caret == null) {
            return null;
        }
        int lineNumber = NbDocument.findLineNumber(document, caret.getDot());
        try {
            Line.Set lineSet = lineCookie.getLineSet();
            assert lineSet != null : lineCookie;
            return lineSet.getCurrent(lineNumber);
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    public static FileObject getFileInEditor() {
        Node[] nodes = TopComponent.getRegistry().getCurrentNodes();
        if (nodes == null) {
            return null;
        }
        if (nodes.length != 1) {
            return null;
        }
        Node n = nodes[0];
        FileObject fo = n.getLookup().lookup(FileObject.class);
        if (fo == null) {
            DataObject dobj = n.getLookup().lookup(DataObject.class);
            if (dobj != null) {
                fo = dobj.getPrimaryFile();
            }
        }
        return fo;
    }

    public static BeanShellBreakpoint getBreakpointAtLine() {
        Line line = getCurrentLine();
        FileObject fo = getFileInEditor();

        BeanShellBreakpoint breakpoint = new BeanShellBreakpoint(line, fo);
        return breakpoint;
    }

    /**
     * Determine whether a particular source file is that from a Hop project.
     *
     * @param fo
     * @return
     */
    public static boolean isBeanShellSource(FileObject fo) {
        return fo.getExt().equals(HOP_FILE_EXTENSION);
    }

    /**
     * Returns an editor pane.
     * 
     * @param editorCookie
     * @return
     */
    private static JEditorPane getEditorPane(EditorCookie editorCookie) {
        final JEditorPane[] op = editorCookie.getOpenedPanes();
        if((op == null) || (op.length < 1))
            return null;

        return op[0];
    }
}
