package de.craftlancer.core.tests;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.craftlancer.core.Utils;

public class UtilsTests
{
    @Test
    public void testArrayContains()
    {
        String[] strArray = { "Hallo", "Welt" };
        String str1 = "Hallo";
        String str2 = "hallo";
        
        Integer[] intArray = { 1, 2, 3, 4, 5, 11111 };
        Integer int1 = 1;
        Integer int2 = 6;
        
        TestClass obj1 = new TestClass(1);
        TestClass[] objArray = { obj1, new TestClass(2), new TestClass(3), new TestClass(4) };
        TestClass obj2 = new TestClass(5);
        
        assertTrue(Utils.arrayContains(strArray, str1));
        assertFalse(Utils.arrayContains(strArray, str2));
        
        assertTrue(Utils.arrayContains(intArray, int1));
        assertFalse(Utils.arrayContains(intArray, int2));
        
        assertTrue(Utils.arrayContains(objArray, obj1));
        assertFalse(Utils.arrayContains(objArray, obj2));
    }
}

class TestClass
{
    private int random;
    
    public TestClass(int i)
    {
        random = i;
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + random;
        return result;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof TestClass))
            return false;
        TestClass other = (TestClass) obj;
        if (random != other.random)
            return false;
        return true;
    }
}
