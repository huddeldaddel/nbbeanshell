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

import java.util.*;

/**
 *
 * @author Thomas Werner
 */
public class BshMethodInfo extends BshInfoContainer implements BshInfo {
   
    /**
     * Comparator for BshMethodInfo objects.
     */
    public static final class Comparator implements java.util.Comparator<BshMethodInfo> {
        
        @Override
        public int compare(BshMethodInfo o1, BshMethodInfo o2) {
            return o1.getName().compareTo(o2.getName());
        }
        
    }
    
    private Set<BshModifierInfo> modifiers;
    private String name;
    private List<BshParameterInfo> parameters;
    private String returnType;    
    private int lineNumber;    
    private boolean clazz;
    private boolean constructor;
    private String superClass;
    private List<String> interfaces;
    private boolean interfaze;
    
    public BshMethodInfo() {
        modifiers = new HashSet<BshModifierInfo>();
        parameters = new LinkedList<BshParameterInfo>();
        interfaces = new LinkedList<String>();
        superClass = "Object";
    }

    @Override
    public int getLineNumber() {
        return lineNumber;
    }

    public void setLineNumber(int lineNumber) {
        this.lineNumber = lineNumber;
    }

    @Override
    public Set<BshModifierInfo> getModifiers() {
        return modifiers;
    }

    public void addModifiers(Collection<BshModifierInfo> modifiers) {
        this.modifiers.addAll(modifiers);
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BshParameterInfo> getParameters() {
        return parameters;
    }

    public void addParameters(Collection<BshParameterInfo> parameters) {
        this.parameters.addAll(parameters);
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    public boolean isClass() {
        return clazz;
    }

    public void setClass(boolean clazz) {
        this.clazz = clazz;
    }

    public boolean isConstructor() {
        return constructor;
    }

    public void setConstructor(boolean constructor) {
        this.constructor = constructor;
    }    
    
    public String getSuperClass() {
        return superClass; 
    }
    
    public void setSuperClass(String superClass) {
        this.superClass = superClass;
    }
    
    public List<String> getInterfaces() {
        final List<String> result = new ArrayList<String>(interfaces);
        final Iterator<String> iterator = result.iterator();
        while(iterator.hasNext())
            if(iterator.next().equals(superClass))
                iterator.remove();
        return result;
    }

    public void addInterfaces(Collection<String> interfaces) {
        this.interfaces.addAll(interfaces);
    }
    
    public boolean isInterface() {
        return interfaze;
    }
    
    public void setInterface(boolean isInterface) {
        interfaze = isInterface;
    }

    @Override
    public String getIconPath() {
        final boolean isStatic = modifiers.contains(BshModifierInfo.Static);
        
        if(isInterface())
            return PRFX +"interface.png";
        
        if(isClass())
            return PRFX +"class.png";
        
        if(isConstructor()) {
            if(modifiers.contains(BshModifierInfo.Private)) {
                return PRFX +"constructorPrivate.png";
            } else if(modifiers.contains(BshModifierInfo.Protected)) {
                return PRFX +"constructorProtected.png";
            } else if(modifiers.contains(BshModifierInfo.Public)) {
                return PRFX +"constructorPublic.png";
            } else {
                return PRFX +"constructorPackage.png";
            }
        }
        
        if(modifiers.contains(BshModifierInfo.Private)) {
            return PRFX +(isStatic ? "methodStPrivate.png" : "methodPrivate.png");
        } else if(modifiers.contains(BshModifierInfo.Protected)) {
            return PRFX +(isStatic ? "methodStProtected.png" : "methodProtected.png");
        } else if(modifiers.contains(BshModifierInfo.Public)) {
            return PRFX +(isStatic ? "methodStPublic.png" : "methodPublic.png");
        } else {
            return PRFX +(isStatic ? "methodStPackage.png" : "methodPackage.png");
        }
    }
    
    @Override
    public String toString() {
        if(isClass())
            return buildClassName();
        
        return buildMethodName();
    }
    
    private String buildClassName() {
        final StringBuilder builder = new StringBuilder(name);
        
        if("Object".equals(getSuperClass()) && getInterfaces().isEmpty())
            return builder.toString();
        
        builder.append(" :: ");
        if(!"Object".equals(getSuperClass())) {
            builder.append(getSuperClass());
            if(!getInterfaces().isEmpty())
                builder.append(" : ");
        }
        
        for(int i=0; i<getInterfaces().size(); i++) {
            if(i != 0)
                builder.append(", ");
            builder.append(getInterfaces().get(i));
        }
        
        return builder.toString();               
    }
    
    private String buildMethodName() {
        final StringBuilder builder = new StringBuilder(name);
        builder.append("(");
        
        boolean first = true;
        for(BshParameterInfo pInfo: getParameters()) {
            if(!first) 
                builder.append(", ");
            first = false;
            
            if(!pInfo.getType().equals(ParserConnector.LOOSE_TYPE))
                builder.append(pInfo.getType()).append(" ");
            builder.append(pInfo.getName());
        }
        builder.append(")");
        
        if(!(isClass() || "void".equals(getReturnType())))
            builder.append(" : ").append(getReturnType());
               
        return builder.toString();
    }
    
}
