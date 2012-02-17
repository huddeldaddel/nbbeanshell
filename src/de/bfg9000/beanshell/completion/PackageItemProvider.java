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

import bsh.BshScriptInfo;
import java.util.LinkedList;
import java.util.List;
import org.netbeans.spi.editor.completion.CompletionItem;

/**
 * Provides {@code CompletionItem}s for packages / fully qualified class names.
 * 
 * @author Thomas Werner
 */
public class PackageItemProvider implements CompletionQueryItemProvider {

    @Override
    public List<? extends CompletionItem> getItems(BshScriptInfo scriptInfo, int startOffset, int caretOffset, 
                                          String filter, int line, int column) {
        final List<CompletionItem> result = new LinkedList<CompletionItem>();
        if(!filter.isEmpty()) {
            
            
            
            // - tries to find packages that start with the given filter
            // - only the root package will be shown - j shows java - not java.io....
            // - if a package contains classes then these classes will be shown before nested packages
        }
        return result;
    }
    
}
