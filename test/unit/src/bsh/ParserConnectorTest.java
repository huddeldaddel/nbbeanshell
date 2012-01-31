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
package bsh;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 *
 * @author wernert
 */
public class ParserConnectorTest {
    
    /**
     * public 
     * void
     * printText(String text) {
     *     print(text);
     * }
     */
    @Test
    public void testJavaStyleMethodWithParameter() throws Exception {
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse("public\nvoid\nprintText(String text) {\nprint(text);\n}");
        
        assertEquals(1, sInfo.getMethods().size());
        
        final BshMethodInfo mInfo = sInfo.getMethods().get(0);
        assertEquals("printText", mInfo.getName());
        assertEquals(1, mInfo.getLineNumber());
        assertEquals("void", mInfo.getReturnType());
        assertEquals(1, mInfo.getModifiers().size());
        assertEquals(true, mInfo.getModifiers().contains(BshModifierInfo.Public));
        assertEquals(1, mInfo.getParameters().size());
        
        final BshParameterInfo pInfo = mInfo.getParameters().get(0);
        assertEquals("text", pInfo.getName());
        assertEquals("String", pInfo.getType());
    }
    
    /**
     * public boolean isTest() {
     *     return true;
     * }
     */
    @Test
    public void testJavaStyleMethodWithoutParameter() throws Exception {
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse("public\nboolean\nisTest() {\nreturn true;\n}");
        
        assertEquals(1, sInfo.getMethods().size());
        
        final BshMethodInfo mInfo = sInfo.getMethods().get(0);
        assertEquals("isTest", mInfo.getName());
        assertEquals(1, mInfo.getLineNumber());
        assertEquals("boolean", mInfo.getReturnType());
        assertEquals(1, mInfo.getModifiers().size());
        assertEquals(true, mInfo.getModifiers().contains(BshModifierInfo.Public));
        assertEquals(0, mInfo.getParameters().size());
    }
    
    /**
     * private static boolean doAThing(String a, boolean b, String muahah) {
     *     return false;
     * }
     */
    @Test
    public void testStaticMethodWithMultipleParameters() throws Exception {
        final BshParserConnector pConnector = new BshParserConnector();
        final String script = "private static boolean doAThing(String a, boolean b, String muahah) { return false; }";
        final BshScriptInfo sInfo = pConnector.parse(script);
        
        assertEquals(1, sInfo.getMethods().size());
        
        final BshMethodInfo mInfo = sInfo.getMethods().get(0);
        assertEquals("doAThing", mInfo.getName());
        assertEquals(1, mInfo.getLineNumber());
        assertEquals("boolean", mInfo.getReturnType());
        assertEquals(2, mInfo.getModifiers().size());
        assertEquals(true, mInfo.getModifiers().contains(BshModifierInfo.Private));
        assertEquals(true, mInfo.getModifiers().contains(BshModifierInfo.Static));
        assertEquals(3, mInfo.getParameters().size());
        
        assertEquals("a", mInfo.getParameters().get(0).getName());
        assertEquals("String", mInfo.getParameters().get(0).getType());
        assertEquals("b", mInfo.getParameters().get(1).getName());
        assertEquals("boolean", mInfo.getParameters().get(1).getType());
        assertEquals("muahah", mInfo.getParameters().get(2).getName());
        assertEquals("String", mInfo.getParameters().get(2).getType());
    }
    
    /**
     * final String doSomething() {
     *     print("foo");
     *     x=5;
     *     bar() {
     *         print("bar");
     *     }
     *     return "foobar";
     * }
     */
    @Test
    public void testMethodWithInnerMethod() throws Exception {
        final StringBuilder script = new StringBuilder();
        script.append("final String doSomething() {\n")
              .append("print(\"foo\");\n")
              .append("x=5;\n")
              .append("bar() {\n")
              .append("print(\"bar\");\n")
              .append("}\n")
              .append("return \"foobar\";\n")
              .append("}");
        
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse(script.toString());
        
        assertEquals(1, sInfo.getMethods().size());
        
        final BshMethodInfo mInfo = sInfo.getMethods().get(0);
        assertEquals("doSomething", mInfo.getName());
        assertEquals(1, mInfo.getLineNumber());
        assertEquals("String", mInfo.getReturnType());
        assertEquals(1, mInfo.getModifiers().size());
        assertEquals(true, mInfo.getModifiers().contains(BshModifierInfo.Final));
        assertEquals(0, mInfo.getParameters().size());
        assertEquals(1, mInfo.getMethods().size());
        
        final BshMethodInfo smInfo = mInfo.getMethods().get(0);
        assertEquals("bar", smInfo.getName());
        assertEquals(4, smInfo.getLineNumber());
        assertEquals("void", smInfo.getReturnType());
        assertEquals(0, smInfo.getModifiers().size());
        assertEquals(0, smInfo.getParameters().size());
        assertEquals(0, smInfo.getMethods().size());
    }
    
    /**
     * blonk(a, b) { print("kkkk"); }
     */
    @Test
    public void testMethodWithLooselyTypedParameters() throws Exception {
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse("blonk(a, b) { print(\"kkkk\"); }");
        
        assertEquals(1, sInfo.getMethods().size());
        
        final BshMethodInfo mInfo = sInfo.getMethods().get(0);
        assertEquals("blonk", mInfo.getName());
        assertEquals(1, mInfo.getLineNumber());
        assertEquals("void", mInfo.getReturnType());
        assertEquals(0, mInfo.getModifiers().size());
        assertEquals(2, mInfo.getParameters().size());
        
        assertEquals("a", mInfo.getParameters().get(0).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, mInfo.getParameters().get(0).getType());
        assertEquals("b", mInfo.getParameters().get(1).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, mInfo.getParameters().get(1).getType());
        
        assertEquals(0, mInfo.getMethods().size());
    }
    
    /**
     * blonkABlonk(a, b) { return a +b; }
     */
    @Test
    public void testMethodWithLooseReturnType() throws Exception {
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse("blonkABlonk(a, b) { return a +b; }");
        
        assertEquals(1, sInfo.getMethods().size());
        
        final BshMethodInfo mInfo = sInfo.getMethods().get(0);
        assertEquals("blonkABlonk", mInfo.getName());
        assertEquals(1, mInfo.getLineNumber());
        assertEquals(BshParserConnector.LOOSE_TYPE, mInfo.getReturnType());
        assertEquals(0, mInfo.getModifiers().size());
        assertEquals(2, mInfo.getParameters().size());
        
        assertEquals("a", mInfo.getParameters().get(0).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, mInfo.getParameters().get(0).getType());
        assertEquals("b", mInfo.getParameters().get(1).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, mInfo.getParameters().get(1).getType());
        
        assertEquals(0, mInfo.getMethods().size());
    }
    
    /**
     * FooObject() {
     *     print("new foo!");
     *     bar() {
     *         print("bar!");
     *     }
     *     return this;
     * }
     */
    @Test
    public void testScriptedClass() throws Exception {
        final StringBuilder script = new StringBuilder();
        script.append("FooObject() {\n")
              .append("print(\"new foo!\");\n")
              .append("bar() {\n")
              .append("print(\"bar\");\n")
              .append("}\n")
              .append("return this;\n")
              .append("}");
        
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse(script.toString());
                
        assertEquals(1, sInfo.getClasses().size());
        
        final BshMethodInfo cInfo = sInfo.getClasses().get(0);
        assertEquals("FooObject", cInfo.getName());
        assertEquals(1, cInfo.getLineNumber());
        assertEquals(0, cInfo.getParameters().size());
        assertEquals(0, cInfo.getModifiers().size());
        assertEquals(1, cInfo.getMethods().size());
        
        final BshMethodInfo smInfo = cInfo.getMethods().get(0);
        assertEquals("bar", smInfo.getName());
        assertEquals(3, smInfo.getLineNumber());
        assertEquals("void", smInfo.getReturnType());
        assertEquals(0, smInfo.getModifiers().size());
        assertEquals(0, smInfo.getParameters().size());
        assertEquals(0, smInfo.getMethods().size());
    }
    
    /**
     * FooObject() {
     *     final String s = " ";
     *     ht = new HashTable();
     *     i = 0;    
     *     d = new Date();
     *     i = 12;
     *     return this;
     * }
     */
    @Test
    public void testClassWithVariables() throws Exception {
        final StringBuilder script = new StringBuilder();
        script.append("FooObject() {\n")
              .append("final String s = \" \";\n")
              .append("ht = new HashTable();\n")
              .append("static protected int i = 0;\n")
              .append("d = new Date();\n")
              .append("i = 12;\n")
              .append("return this;\n")
              .append("}");
        
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse(script.toString());
        
        assertEquals(1, sInfo.getClasses().size());
        
        final BshMethodInfo cInfo = sInfo.getClasses().get(0);
        assertEquals("FooObject", cInfo.getName());
        assertEquals(1, cInfo.getLineNumber());
        assertEquals(0, cInfo.getParameters().size());
        assertEquals(0, cInfo.getModifiers().size());
        assertEquals(0, cInfo.getMethods().size());
        assertEquals(4, cInfo.getVariables().size());
        
        assertEquals(2, cInfo.getVariables().get(0).getLineNumber());
        assertEquals("s", cInfo.getVariables().get(0).getName());
        assertEquals("String", cInfo.getVariables().get(0).getType());
        assertEquals(1, cInfo.getVariables().get(0).getModifiers().size());
        assertEquals(true, cInfo.getVariables().get(0).getModifiers().contains(BshModifierInfo.Final));
        
        assertEquals(3, cInfo.getVariables().get(1).getLineNumber());
        assertEquals("ht", cInfo.getVariables().get(1).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, cInfo.getVariables().get(1).getType());
        
        assertEquals(4, cInfo.getVariables().get(2).getLineNumber());
        assertEquals("i", cInfo.getVariables().get(2).getName());
        assertEquals("int", cInfo.getVariables().get(2).getType());
        assertEquals(2, cInfo.getVariables().get(2).getModifiers().size());
        assertEquals(true, cInfo.getVariables().get(2).getModifiers().contains(BshModifierInfo.Static));
        assertEquals(true, cInfo.getVariables().get(2).getModifiers().contains(BshModifierInfo.Protected));
        
        assertEquals(5, cInfo.getVariables().get(3).getLineNumber());
        assertEquals("d", cInfo.getVariables().get(3).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, cInfo.getVariables().get(3).getType());        
    }
    
    /**
     * int j = 0;
     * print(j);
     */
    @Test
    public void testVariableInScript() throws Exception {
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse("int j = 0;\nprint(j);");
        
        assertEquals(0, sInfo.getClasses().size());
        assertEquals(0, sInfo.getMethods().size());
        assertEquals(1, sInfo.getVariables().size());
        
        assertEquals(1, sInfo.getVariables().get(0).getLineNumber());
        assertEquals("j", sInfo.getVariables().get(0).getName());
        assertEquals("int", sInfo.getVariables().get(0).getType());    
    }
    
    /**
     * int j = 0;
     * addOne() {
     *     j = j +1;
     *     b = 0;
     * }
     * addOne();
     * b = 5;
     * print(j);
     * print(b);
     */
    @Test
    public void testOuterVariableUsedInInnerMethod() throws Exception {
        final StringBuilder script = new StringBuilder();
        script.append("int j = 0;\n")
              .append("addOne() {\n")
              .append("j = j +1;\n")
              .append("b = 0;\n")
              .append("}\n")
              .append("addOne();\n")
              .append("b = 5;\n")
              .append("print(j);\n")
              .append("print(b);\n");
        
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse(script.toString());
        
        assertEquals(0, sInfo.getClasses().size());
        assertEquals(1, sInfo.getMethods().size());
        assertEquals(2, sInfo.getVariables().size());
        
        assertEquals(1, sInfo.getVariables().get(0).getLineNumber());
        assertEquals("j", sInfo.getVariables().get(0).getName());
        assertEquals("int", sInfo.getVariables().get(0).getType());
        
        assertEquals(7, sInfo.getVariables().get(1).getLineNumber());
        assertEquals("b", sInfo.getVariables().get(1).getName());
        assertEquals(BshParserConnector.LOOSE_TYPE, sInfo.getVariables().get(1).getType());        
    }
    
    /**
     * @see bsh/scripts/cascadedDeclaration.bsh
     */
    @Test
    public void testOuterVariableUsedInScriptedClass() throws Exception {
        final String script = "/bsh/scripts/cascadedDeclaration.bsh";
        final BshParserConnector pConnector = new BshParserConnector();
        final BshScriptInfo sInfo = pConnector.parse(getClass().getResourceAsStream(script));
        
        assertEquals(1, sInfo.getClasses().size());                                             // FooObject
        assertEquals(3, sInfo.getVariables().size());                                           // j, t, f        
        assertEquals(1, sInfo.getClasses().get(0).getVariables().size());                       // Only t counts
        assertEquals(1, sInfo.getClasses().get(0).getClasses().size());                         // BarObject
        assertEquals(1, sInfo.getClasses().get(0).getClasses().get(0).getVariables().size());   // Only j counts        
    }
    
}
