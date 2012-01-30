package bsh;

import java.util.Set;

/**
 *
 * @author Thomas Werner
 */
public interface BshInfo {
    
    public static final String PRFX = "/de/bfg9000/beanshell/icons/";
    
    public abstract int getLineNumber();
    public abstract Set<BshModifierInfo> getModifiers();
    public abstract String getName();
    public abstract String getIconPath();
    
}
