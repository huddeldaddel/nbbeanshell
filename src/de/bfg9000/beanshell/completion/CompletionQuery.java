/*
 * nbBeanShell -- a integration of BeanShell into the NetBeans IDE
 * Copyright (C) 2012 Thomas Werner
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later
 * version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with this library; if not, write to
 * the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.bfg9000.beanshell.completion;

import bsh.BshParserConnector;
import bsh.BshScriptInfo;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.StyledDocument;
import org.netbeans.spi.editor.completion.CompletionResultSet;
import org.netbeans.spi.editor.completion.support.AsyncCompletionQuery;

/**
 * Performs various preparation tasks and then calls the {@code CompletionQueryItemProvider}s to get the available code 
 * completion items for the current script.
 * 
 * @author Thomas Werner
 */
class CompletionQuery extends AsyncCompletionQuery {
    
    private static final Logger logger = Logger.getLogger(CompletionQuery.class.getName());
    
    private final List<CompletionQueryItemProvider> itemProviders;
    
    public CompletionQuery() {
        itemProviders = new LinkedList<CompletionQueryItemProvider>();
        itemProviders.add(new VariableItemProvider());
    }
    
    @Override
    protected void query(CompletionResultSet resultSet, Document doc, int caretOffset) {
        String filter = null;
        int startOffset = caretOffset - 1;
        int lineNumber = 0;
        int colNumber = 0;
        try {
            final StyledDocument bDoc = (StyledDocument) doc;
            final int lineStartOffset = getRowFirstNonWhite(bDoc, caretOffset);
            final char[] line = bDoc.getText(lineStartOffset, caretOffset -lineStartOffset).toCharArray();
            final int whiteOffset = indexOfWhite(line);
            filter = new String(line, whiteOffset +1, line.length -whiteOffset -1);
            startOffset = (whiteOffset > 0) ? lineStartOffset +whiteOffset +1 : lineStartOffset;
            lineNumber = doc.getDefaultRootElement().getElementIndex(caretOffset) +1;
            colNumber = 0 /* caretOffset -bDoc.getParagraphElement(caretOffset).getStartOffset() +1 */;
        } catch (BadLocationException ex) {
            logger.log(Level.WARNING, null, ex);
        }

        try {
            // Remove current line from script as it's almost always an incorrect statement, yet. This its kind of a 
            // hack and doesn't work in all situations - but still better than nothing
            final String script = removeLine(doc.getText(0, doc.getLength()), lineNumber -1);
            final BshScriptInfo scriptInfo = new BshParserConnector().parse(script);
            for(CompletionQueryItemProvider provider: itemProviders)
                resultSet.addAllItems(provider.getItems(scriptInfo, startOffset, caretOffset, filter, lineNumber, 
                                      colNumber));
        } catch(Exception ex) {
        } finally {
            resultSet.finish();
        }
    }

    private int getRowFirstNonWhite(StyledDocument doc, int offset) throws BadLocationException {
        Element lineElement = doc.getParagraphElement(offset);
        int start = lineElement.getStartOffset();
        while(start +1 < lineElement.getEndOffset()) {
            try {
                if(doc.getText(start, 1).charAt(0) != ' ')
                    break;                    
            } catch (BadLocationException ex) {
                final String msg = new StringBuilder().append("calling getText(").append(start).append(", ")
                                                      .append(start +1).append(") on doc of length: ")
                                                      .append(doc.getLength()).toString();
                throw (BadLocationException) new BadLocationException(msg, start).initCause(ex);
            }
            start++;
        }
        return start;
    }

    private int indexOfWhite(char[] line) {
        int i = line.length;
        while(--i > -1) {
            final char c = line[i];
            if(Character.isWhitespace(c))
                return i;
        }
        return -1;
    }

    private String removeLine(String input, int lineToRemove) {
        final String lines[] = input.split("\\r?\\n");
        final StringBuilder result = new StringBuilder();
        for(int i=0; i<lines.length; i++)
            result.append(i != lineToRemove ? lines[i] : "").append("\n");
        return result.toString();
    }
    
}
