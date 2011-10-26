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

import static de.bfg9000.beanshell.jcclexer.ParserConstants.*;
import java.util.*;
import org.netbeans.spi.lexer.*;

/**
 * LanguageHierarchy for BeanShell Script.
 * 
 * @author Thomas Werner
 */
public class BeanShellLanguageHierarchy extends LanguageHierarchy<BeanShellTokenId> {

    private static List<BeanShellTokenId> tokens;
    private static Map<Integer,BeanShellTokenId> idToToken;

    private static void init() {
        tokens = Arrays.<BeanShellTokenId> asList (new BeanShellTokenId[] {
            new BeanShellTokenId("EOF", "whitespace", EOF),
            new BeanShellTokenId("WHITESPACE", "whitespace", WHITESPACE),
            new BeanShellTokenId("NONPRINTABLE", "whitespace", NONPRINTABLE),
            new BeanShellTokenId("FORMAL_COMMENT", "comment", FORMAL_COMMENT),
            new BeanShellTokenId("SINGLE_LINE_COMMENT", "comment", SINGLE_LINE_COMMENT),
            new BeanShellTokenId("HASH_BANG_COMMENT", "comment", HASH_BANG_COMMENT),
            new BeanShellTokenId("MULTI_LINE_COMMENT", "comment", MULTI_LINE_COMMENT),
            new BeanShellTokenId("ABSTRACT", "keyword", ABSTRACT),
            new BeanShellTokenId("BOOLEAN", "keyword", BOOLEAN),
            new BeanShellTokenId("BREAK", "keyword", BREAK),
            new BeanShellTokenId("CLASS", "keyword", CLASS),
            new BeanShellTokenId("BYTE", "keyword", BYTE),
            new BeanShellTokenId("CASE", "keyword", CASE),
            new BeanShellTokenId("CATCH", "keyword", CATCH),
            new BeanShellTokenId("CHAR", "keyword", CHAR),
            new BeanShellTokenId("CONST", "keyword", CONST),
            new BeanShellTokenId("CONTINUE", "keyword", CONTINUE),
            new BeanShellTokenId("_DEFAULT", "keyword", _DEFAULT),
            new BeanShellTokenId("DO", "keyword", DO),
            new BeanShellTokenId("DOUBLE", "keyword", DOUBLE),
            new BeanShellTokenId("ELSE", "keyword", ELSE),
            new BeanShellTokenId("ENUM", "keyword", ENUM),
            new BeanShellTokenId("EXTENDS", "keyword", EXTENDS),
            new BeanShellTokenId("FALSE", "keyword", FALSE),
            new BeanShellTokenId("FINAL", "keyword", FINAL),
            new BeanShellTokenId("FINALLY", "keyword", FINALLY),
            new BeanShellTokenId("FLOAT", "keyword", FLOAT),
            new BeanShellTokenId("FOR", "keyword", FOR),
            new BeanShellTokenId("GOTO", "keyword", GOTO),
            new BeanShellTokenId("IF", "keyword", IF),
            new BeanShellTokenId("IMPLEMENTS", "keyword", IMPLEMENTS),
            new BeanShellTokenId("IMPORT", "keyword", IMPORT),
            new BeanShellTokenId("INSTANCEOF", "keyword", INSTANCEOF),
            new BeanShellTokenId("INT", "keyword", INT),
            new BeanShellTokenId("INTERFACE", "keyword", INTERFACE),
            new BeanShellTokenId("LONG", "keyword", LONG),
            new BeanShellTokenId("NATIVE", "keyword", NATIVE),
            new BeanShellTokenId("NEW", "keyword", NEW),
            new BeanShellTokenId("NULL", "keyword", NULL),
            new BeanShellTokenId("PACKAGE", "keyword", PACKAGE),
            new BeanShellTokenId("PRIVATE", "keyword", PRIVATE),
            new BeanShellTokenId("PROTECTED", "keyword", PROTECTED),
            new BeanShellTokenId("PUBLIC", "keyword", PUBLIC),
            new BeanShellTokenId("RETURN", "keyword", RETURN),
            new BeanShellTokenId("SHORT", "keyword", SHORT),
            new BeanShellTokenId("STATIC", "keyword", STATIC),
            new BeanShellTokenId("STRICTFP", "keyword", STRICTFP),
            new BeanShellTokenId("SWITCH", "keyword", SWITCH),
            new BeanShellTokenId("SYNCHRONIZED", "keyword", SYNCHRONIZED),
            new BeanShellTokenId("TRANSIENT", "keyword", TRANSIENT),
            new BeanShellTokenId("THROW", "keyword", THROW),
            new BeanShellTokenId("THROWS", "keyword", THROWS),
            new BeanShellTokenId("TRUE", "keyword", TRUE),
            new BeanShellTokenId("TRY", "keyword", TRY),
            new BeanShellTokenId("VOID", "keyword", VOID),
            new BeanShellTokenId("VOLATILE", "keyword", VOLATILE),
            new BeanShellTokenId("WHILE", "keyword", WHILE),
            new BeanShellTokenId("INTEGER_LITERAL", "literal", INTEGER_LITERAL),
            new BeanShellTokenId("DECIMAL_LITERAL", "literal", DECIMAL_LITERAL),
            new BeanShellTokenId("HEX_LITERAL", "literal", HEX_LITERAL),
            new BeanShellTokenId("OCTAL_LITERAL", "literal", OCTAL_LITERAL),
            new BeanShellTokenId("FLOATING_POINT_LITERAL", "literal", FLOATING_POINT_LITERAL),
            new BeanShellTokenId("EXPONENT", "number", EXPONENT),
            new BeanShellTokenId("CHARACTER_LITERAL", "literal", CHARACTER_LITERAL),
            new BeanShellTokenId("STRING_LITERAL", "literal", STRING_LITERAL),
            new BeanShellTokenId("IDENTIFIER", "identifier", IDENTIFIER),
            new BeanShellTokenId("LETTER", "literal", LETTER),
            new BeanShellTokenId("DIGIT", "literal", DIGIT),
            new BeanShellTokenId("LPAREN", "operator", LPAREN),
            new BeanShellTokenId("RPAREN", "operator", RPAREN),
            new BeanShellTokenId("LBRACE", "operator", LBRACE),
            new BeanShellTokenId("RBRACE", "operator", RBRACE),
            new BeanShellTokenId("LBRACKET", "operator", LBRACKET),
            new BeanShellTokenId("RBRACKET", "operator", RBRACKET),
            new BeanShellTokenId("SEMICOLON", "operator", SEMICOLON),
            new BeanShellTokenId("COMMA", "operator", COMMA),
            new BeanShellTokenId("DOT", "operator", DOT),
            new BeanShellTokenId("ASSIGN", "operator", ASSIGN),
            new BeanShellTokenId("GT", "operator", GT),
            new BeanShellTokenId("GTX", "operator", GTX),
            new BeanShellTokenId("LT", "operator", LT),
            new BeanShellTokenId("LTX", "operator", LTX),
            new BeanShellTokenId("BANG", "operator", BANG),
            new BeanShellTokenId("TILDE", "operator", TILDE),
            new BeanShellTokenId("HOOK", "operator", HOOK),
            new BeanShellTokenId("COLON", "operator", COLON),
            new BeanShellTokenId("EQ", "operator", EQ),
            new BeanShellTokenId("LE", "operator", LE),
            new BeanShellTokenId("LEX", "operator", LEX),
            new BeanShellTokenId("GE", "operator", GE),
            new BeanShellTokenId("GEX", "operator", GEX),
            new BeanShellTokenId("NE", "operator", NE),
            new BeanShellTokenId("BOOL_OR", "operator", BOOL_OR),
            new BeanShellTokenId("BOOL_ORX", "operator", BOOL_ORX),
            new BeanShellTokenId("BOOL_AND", "operator", BOOL_AND),
            new BeanShellTokenId("BOOL_ANDX", "operator", BOOL_ANDX),
            new BeanShellTokenId("INCR", "operator", INCR),
            new BeanShellTokenId("DECR", "operator", DECR),
            new BeanShellTokenId("PLUS", "operator", PLUS),
            new BeanShellTokenId("MINUS", "operator", MINUS),
            new BeanShellTokenId("STAR", "operator", STAR),
            new BeanShellTokenId("SLASH", "operator", SLASH),
            new BeanShellTokenId("BIT_AND", "operator", BIT_AND),
            new BeanShellTokenId("BIT_ANDX", "operator", BIT_ANDX),
            new BeanShellTokenId("BIT_OR", "operator", BIT_OR),
            new BeanShellTokenId("BIT_ORX", "operator", BIT_ORX),
            new BeanShellTokenId("XOR", "operator", XOR),
            new BeanShellTokenId("MOD", "operator", MOD),
            new BeanShellTokenId("LSHIFT", "operator", LSHIFT),
            new BeanShellTokenId("LSHIFTX", "operator", LSHIFTX),
            new BeanShellTokenId("RSIGNEDSHIFT", "operator", RSIGNEDSHIFT),
            new BeanShellTokenId("RSIGNEDSHIFTX", "operator", RSIGNEDSHIFTX),
            new BeanShellTokenId("RUNSIGNEDSHIFT", "operator", RUNSIGNEDSHIFT),
            new BeanShellTokenId("RUNSIGNEDSHIFTX", "operator", RUNSIGNEDSHIFTX),
            new BeanShellTokenId("PLUSASSIGN", "operator", PLUSASSIGN),
            new BeanShellTokenId("MINUSASSIGN", "operator", MINUSASSIGN),
            new BeanShellTokenId("STARASSIGN", "operator", STARASSIGN),
            new BeanShellTokenId("SLASHASSIGN", "operator", SLASHASSIGN),
            new BeanShellTokenId("ANDASSIGN", "operator", ANDASSIGN),
            new BeanShellTokenId("ANDASSIGNX", "operator", ANDASSIGNX),
            new BeanShellTokenId("ORASSIGN", "operator", ORASSIGN),
            new BeanShellTokenId("ORASSIGNX", "operator", ORASSIGNX),
            new BeanShellTokenId("XORASSIGN", "operator", XORASSIGN),
            new BeanShellTokenId("MODASSIGN", "operator", MODASSIGN),
            new BeanShellTokenId("LSHIFTASSIGN", "operator", LSHIFTASSIGN),
            new BeanShellTokenId("LSHIFTASSIGNX", "operator", LSHIFTASSIGNX),
            new BeanShellTokenId("RSIGNEDSHIFTASSIGN", "operator", RSIGNEDSHIFTASSIGN),
            new BeanShellTokenId("RSIGNEDSHIFTASSIGNX", "operator", RSIGNEDSHIFTASSIGNX),
            new BeanShellTokenId("RUNSIGNEDSHIFTASSIGN", "operator", RUNSIGNEDSHIFTASSIGN),
            new BeanShellTokenId("RUNSIGNEDSHIFTASSIGNX", "operator", RUNSIGNEDSHIFTASSIGNX)
        });
        idToToken = new HashMap<Integer, BeanShellTokenId> ();
        for(BeanShellTokenId token : tokens)
            idToToken.put(token.ordinal(), token);
    }

    static synchronized BeanShellTokenId getToken (int id) {
        if(idToToken == null)
            init();
        return idToToken.get(id);
    }

    @Override
    protected synchronized Collection<BeanShellTokenId> createTokenIds () {
        if(tokens == null)
            init();
        return tokens;
    }

    @Override
    protected synchronized Lexer<BeanShellTokenId> createLexer(LexerRestartInfo<BeanShellTokenId> info) {
        return new BeanShellLexer(info);
    }

    @Override
    protected String mimeType() {
        return "text/x-beanshell";
    }

}
