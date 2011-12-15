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
package de.bfg9000.beanshell.navigator;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.text.JTextComponent;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * This abstract node implements the common behaviour of the various nodes used in the Navigator.
 * 
 * @author Thomas Werner
 */
abstract class BeanShellNode extends AbstractNode {
    
    protected static final String PRFX = "/de/bfg9000/beanshell/icons/";
    
    private final JTextComponent connectedComponent;
    
    public BeanShellNode(Children children, JTextComponent connectedComponent) {
        super(children);
        this.connectedComponent = connectedComponent;
    }

    public BeanShellNode(Children children, Lookup lookup, JTextComponent connectedComponent) {
        super(children, lookup);
        this.connectedComponent = connectedComponent;
    }
    
    @Override
    public Action getPreferredAction() {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {                
                final String text = connectedComponent.getText();
                int cr = 0;
                int line = 1;
                int position = 0;
                while(position < text.length()) {
                    if(getLineNumber() == line) {
                        connectedComponent.setCaretPosition(position -cr);
                        return;
                    }

                    if(text.charAt(position) == '\n')
                        line++;

                    if(text.charAt(position) == '\r')
                        cr++;

                    position++;                                
                }
            }
        };
    }
    
    public abstract int getLineNumber();
    
    protected Image loadIcon(String path) {
        try {
            return ImageIO.read(getClass().getResource(path));
        } catch (IOException ex) {
            System.out.println(ex);
            return null;
        }
    }
    
}
