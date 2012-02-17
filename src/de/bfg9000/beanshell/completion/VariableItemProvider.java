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

import bsh.BshInfoContainer;
import bsh.BshMethodInfo;
import bsh.BshScriptInfo;
import bsh.BshVariableInfo;
import java.util.*;
import org.netbeans.spi.editor.completion.CompletionItem;

/**
 * Provides {@code CompletionItem}s for the variables that are available at the current position of the script.
 * 
 * @author Thomas Werner
 */
class VariableItemProvider implements CompletionQueryItemProvider {

    @Override
    public List<? extends CompletionItem> getItems(BshScriptInfo scriptInfo, int startOffset, int caretOffset, 
                                          String filter, int line, int column) {

        final List<BshVariableInfo> varInfoList = new LinkedList<BshVariableInfo>();
        handleInfoContainer(varInfoList, scriptInfo, line, column);
        Collections.sort(varInfoList, new BshVariableInfo.Comparator());
        
        final String lowerCaseFilter = filter.toLowerCase();
        final List<VariableCompletionItem> result = new ArrayList<VariableCompletionItem>(varInfoList.size());
        for(BshVariableInfo varInfo: varInfoList)
            if(filter.isEmpty() || varInfo.getName().toLowerCase().startsWith(lowerCaseFilter))
                result.add(new VariableCompletionItem(varInfo, startOffset, caretOffset));
        
        return result;
    }
    
    private void handleInfoContainer(List<BshVariableInfo> list, BshInfoContainer container, int line, int col) {
        final boolean afterStart= (container.getBeginLine() < line) || 
                                  ((container.getBeginLine() == line) && (container.getBeginColum() <= col));
        final boolean beforeEnd = (container.getEndLine() > line) || 
                                  ((container.getEndLine() == line) && (container.getEndColum() >= col));
        
        if(afterStart && beforeEnd) {
            for(BshVariableInfo varInfo: container.getVariables()) {
                // Deeper nested variable declarations override earlier once
                final Iterator<BshVariableInfo> iterator = list.iterator();
                while(iterator.hasNext()) 
                    if(iterator.next().getName().equals(varInfo.getName()))
                        iterator.remove();
                list.add(varInfo);
            }
            for(BshMethodInfo method: container.getMethods())
                handleInfoContainer(list, method, line, col);
        }
    }
    
}
