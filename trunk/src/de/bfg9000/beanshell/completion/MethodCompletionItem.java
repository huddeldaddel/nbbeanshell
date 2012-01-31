/*
 * nbBeanShell -- a integration of BeanShell into the NetBeans IDE
 * Copyright (C) 2012 Thomas Werner
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
package de.bfg9000.beanshell.completion;

import bsh.BshMethodInfo;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;
import javax.swing.text.StyledDocument;
import org.netbeans.api.editor.completion.Completion;
import org.netbeans.spi.editor.completion.CompletionItem;
import org.netbeans.spi.editor.completion.CompletionTask;
import org.netbeans.spi.editor.completion.support.CompletionUtilities;

/**
 *
 * @author Thomas Werner
 */
class MethodCompletionItem implements CompletionItem {

    private static final Color fieldColor = Color.decode("0x0000B2");
    
    private final BshMethodInfo method;
    private final int caretOffset;
    private final int dotOffset;
    private final String methodString;
    private final ImageIcon methodIcon;

    public MethodCompletionItem(BshMethodInfo method, int dotOffset, int caretOffset) {
        this.method = method;
        this.dotOffset = dotOffset;
        this.caretOffset = caretOffset;

        Image image = null;
        try {
            image = ImageIO.read(getClass().getResource(method.getIconPath()));
        } catch (IOException ex) { }
        methodIcon = new ImageIcon(image);
        methodString = method.toString();        
    }
    
    @Override
    public void defaultAction(JTextComponent component) { 
        try {
            final StyledDocument doc = (StyledDocument) component.getDocument();
            doc.remove(dotOffset, caretOffset-dotOffset);
            doc.insertString(caretOffset, methodString, null);            
            Completion.get().hideAll();
        } catch(Exception ex) { }
    }

    @Override
    public void processKeyEvent(KeyEvent evt) { }

    @Override
    public int getPreferredWidth(Graphics g, Font defaultFont) {
        return CompletionUtilities.getPreferredWidth(methodString, null, g, defaultFont);
    }

    @Override
    public void render(Graphics g, Font defaultFont, Color defaultColor, Color backgroundColor, int width, int height, 
                       boolean selected) {
        CompletionUtilities.renderHtml(methodIcon, methodString, null, g, defaultFont,
                                       (selected ? Color.white : fieldColor), width, height, selected);
    }

    @Override
    public CompletionTask createDocumentationTask() {
        return null;
    }

    @Override
    public CompletionTask createToolTipTask() {
        return null;
    }

    @Override
    public boolean instantSubstitution(JTextComponent component) {
        return false;
    }

    @Override
    public int getSortPriority() {
        return 0;
    }

    @Override
    public CharSequence getSortText() {
        return methodString;
    }

    @Override
    public CharSequence getInsertPrefix() {
        return methodString;
    }
    
}
