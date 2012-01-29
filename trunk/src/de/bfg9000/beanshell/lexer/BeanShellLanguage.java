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
package de.bfg9000.beanshell.lexer;

import org.netbeans.api.lexer.Language;
import org.netbeans.modules.csl.spi.DefaultLanguageConfig;
import org.netbeans.modules.csl.spi.LanguageRegistration;

/**
 * Language configuration for BeanShell Script. 
 *
 * @author Thomas Werner
 */
@LanguageRegistration(mimeType="text/x-beanshell")
public class BeanShellLanguage extends DefaultLanguageConfig {

    @Override
    public Language<BeanShellTokenId> getLexerLanguage() {
        return BeanShellTokenId.getLanguage();
    }

    @Override
    public String getDisplayName() {
        return "BeanShell Script";
    }

    @Override
    public String getLineCommentPrefix() {
        return "//";
    }

    @Override
    public String getPreferredExtension() {
        return "bsh";
    }

    @Override
    public boolean isIdentifierChar(char c) {
        return Character.isJavaIdentifierPart(c);
    }

}
