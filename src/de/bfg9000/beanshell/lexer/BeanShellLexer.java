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

import de.bfg9000.beanshell.jcclexer.JavaCharStream;
import de.bfg9000.beanshell.jcclexer.ParserTokenManager;
import de.bfg9000.beanshell.jcclexer.Token;
import org.netbeans.spi.lexer.Lexer;
import org.netbeans.spi.lexer.LexerRestartInfo;
import org.netbeans.spi.lexer.TokenFactory;

/**
 * The BeanShell Script lexer
 * 
 * @author Thomas Werner
 */
public class BeanShellLexer implements Lexer<BeanShellTokenId> {

    private LexerRestartInfo<BeanShellTokenId> info;
    private ParserTokenManager bshParserTokenManager;

    BeanShellLexer(LexerRestartInfo<BeanShellTokenId> info) {
        this.info = info;
        JavaCharStream stream = new JavaCharStream(info.input());
        bshParserTokenManager = new ParserTokenManager(stream);
    }

    @Override
    public org.netbeans.api.lexer.Token<BeanShellTokenId> nextToken () {
        try {
            Token token = bshParserTokenManager.getNextToken();
            if(info.input().readLength () < 1) return null;
            return info.tokenFactory().createToken(BeanShellLanguageHierarchy.getToken(token.kind));
        } catch(Throwable t) {
            // In case of invalid input: ignore the current token
            return TokenFactory.SKIP_TOKEN;
        }
    }

    @Override
    public Object state () {
        return null;
    }

    @Override
    public void release () { }

}
