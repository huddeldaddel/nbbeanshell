package bsh;

import java.util.Set;

/**
 *
 * @author Thomas Werner
 */
public interface BshInfo {
    
    public abstract int getLineNumber();
    public abstract Set<BshModifierInfo> getModifiers();
    public abstract String getName();

}
