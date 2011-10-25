/*
 * nbBeanShell -- a integration of BeanScript into the NetBeans IDE
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
package de.bfg9000.beanshell.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.TokenId;

/**
 *
 * @author Thomas Werner
 */
public class BeanShellTokenId implements TokenId {

    private static final Language<BeanShellTokenId> language = new BeanShellLanguageHierarchy().language();

    private final String name;
    private final String primaryCategory;
    private final int id;

    public static Language<BeanShellTokenId> getLanguage () {
        return language;
    }

    BeanShellTokenId(String name, String primaryCategory, int id) {
        this.name = name;
        this.primaryCategory = primaryCategory;
        this.id = id;
    }

    @Override
    public String primaryCategory () {
        return primaryCategory;
    }

    @Override
    public int ordinal () {
        return id;
    }

    @Override
    public String name () {
        return name;
    }

}
