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
import org.openide.nodes.Node;
import org.openide.util.Lookup;

/**
 * A JPanel based component that can be used as NavigatorPanel (Navigator API). The panel displays the structure of a
 * BeanShell script currently opened in the editor.
 * 
 * @author Thomas Werner
 */
public class BeanShellNavigatorPanel extends JPanel 
             implements NavigatorPanel, ExplorerManager.Provider, DocumentListener {

    private final ExplorerManager manager = new ExplorerManager();
    private final BeanTreeView beanTreeView = new BeanTreeView();
    private final Lookup lookup;
    
    private Timer lookupTimer;
    private JTextComponent connectedTextComponent;
    
    public BeanShellNavigatorPanel() {
        setLayout(new BorderLayout());
        add(beanTreeView, BorderLayout.CENTER);
        
        lookup = ExplorerUtils.createLookup(manager, getActionMap());        
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
            connectedTextComponent.getDocument().removeDocumentListener(this);
        connectedTextComponent = org.netbeans.api.editor.EditorRegistry.lastFocusedComponent();
        if(null != connectedTextComponent) {
            connectedTextComponent.getDocument().addDocumentListener(this);
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
            connectedTextComponent.getDocument().removeDocumentListener(this);        
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

    @Override
    public void insertUpdate(DocumentEvent e) {
        updateContent();
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        updateContent();
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        updateContent();
    }
    
    private void updateContent() {
        final String script = null != connectedTextComponent ? connectedTextComponent.getText() : "";
        try {
            beanTreeView.setRootVisible(false);
            manager.setRootContext(new RootNode(script, connectedTextComponent));
        } catch(Throwable ex) {
            beanTreeView.setRootVisible(true);
            manager.setRootContext(new ErrorNode());
        }
        expandRecursively(beanTreeView, manager.getRootContext());
    }

    private void expandRecursively(final BeanTreeView view, Node node) {
        view.expandNode(node);
        for(final Node n : node.getChildren().getNodes())
            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    expandRecursively(view, n);
                }
            });
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
                    connectedTextComponent.getDocument().addDocumentListener(BeanShellNavigatorPanel.this);
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
    
}
