/*
 * nbBeanShell -- a integration of BeanShell into the NetBeans IDE
 * Copyright (C) 2011 Thomas Werner
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
package de.bfg9000.beanshell.navigator;

import bsh.ParserConnector;
import java.awt.BorderLayout;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import org.netbeans.spi.navigator.NavigatorPanel;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.explorer.view.BeanTreeView;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 * A JPanel based component that can be used as NavigatorPanel (Navigator API). The panel displays the structure of a
 * BeanShell script currently opened in the editor.
 * 
 * @author Thomas Werner
 */
public class BeanShellNavigatorPanel extends JPanel implements NavigatorPanel, ExplorerManager.Provider {
    
    private static final long serialVersionUID = 1L;

    private final ExplorerManager manager;
    private final BeanTreeView beanTreeView;
    private final DocumentChangeListener documentListener;
    private final Lookup lookup;
    
    private Timer lookupTimer;
    private JTextComponent connectedTextComponent;
    
    public BeanShellNavigatorPanel() {
        setLayout(new BorderLayout());
        
        manager = new ExplorerManager();
        lookup = ExplorerUtils.createLookup(manager, getActionMap());        
        
        beanTreeView = new BeanTreeView();
        beanTreeView.setRootVisible(false);
        add(beanTreeView, BorderLayout.CENTER);
        
        documentListener = new DocumentChangeListener();
    }
    
    @Override
    public String getDisplayName() {
        return "DisplayName";
    }

    @Override
    public String getDisplayHint() {
        return "DisplayHint";
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public void panelActivated(Lookup lkp) { 
        if(null != connectedTextComponent)
            connectedTextComponent.getDocument().removeDocumentListener(documentListener);
        connectedTextComponent = org.netbeans.api.editor.EditorRegistry.lastFocusedComponent();
        if(null != connectedTextComponent) {
            connectedTextComponent.getDocument().addDocumentListener(documentListener);
            updateContent();
        } else { 
            startLookupTimer();
        }
        ExplorerUtils.activateActions(manager, true);
    }

    @Override
    public void panelDeactivated() { 
        stopLookupTimer();
            
        if(null != connectedTextComponent)
            connectedTextComponent.getDocument().removeDocumentListener(documentListener);        
        ExplorerUtils.activateActions(manager, false);
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }
    
    @Override
    public ExplorerManager getExplorerManager() {
        return manager;
    }
    
    private void updateContent() {
        final String script = null != connectedTextComponent ? connectedTextComponent.getText() : "";        
        final NodeFactory factory = new NodeFactory(new ParserConnector().parse(script), connectedTextComponent);
        manager.setRootContext(new AbstractNode(Children.create(factory, false)));
        beanTreeView.expandAll();
    }
    
    private void startLookupTimer() {
        if(null != lookupTimer)
            stopLookupTimer();
        
        lookupTimer = new Timer();
        lookupTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                connectedTextComponent = org.netbeans.api.editor.EditorRegistry.lastFocusedComponent();
                if(null != connectedTextComponent) {
                    lookupTimer.cancel();
                    connectedTextComponent.getDocument().addDocumentListener(documentListener);
                    updateContent();
                }
            }
        }, 500, 500);
    }
    
    private void stopLookupTimer() {
        if(null != lookupTimer) {
            lookupTimer.cancel();
            lookupTimer = null;
        }
    }
 
    /**
     * Listens for modifications of the current script and updates the nodes of the navigator.
     */
    private final class DocumentChangeListener implements DocumentListener {
        
        private volatile long lastUpdate = 0;
        private final Timer delayTimer = new Timer();
        
        @Override
        public void insertUpdate(DocumentEvent e) {
            modificationHappened();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            modificationHappened();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            modificationHappened();
        }
        
        private void modificationHappened() {            
            lastUpdate = System.currentTimeMillis();
            delayTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    if(System.currentTimeMillis() -lastUpdate >= 500)
                        SwingUtilities.invokeLater(new Runnable() {
                            @Override
                            public void run() {
                                updateContent();
                            }
                        });
                }
            }, 500);
        }
        
    }
    
}
