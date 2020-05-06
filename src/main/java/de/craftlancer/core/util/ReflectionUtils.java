package de.craftlancer.core.util;

import java.lang.reflect.Method;

public class ReflectionUtils {
    
    public static Method getMethod(Class<?> clazz, String string) {
        for (Method m : clazz.getMethods())
            if (m.getName().equals(string))
                return m;
            
        return null;
    }
    
}
