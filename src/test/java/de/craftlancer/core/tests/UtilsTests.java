package de.craftlancer.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;

import org.junit.Test;

import de.craftlancer.core.Utils;

public class UtilsTests
{
    
    @Test
    public void testIsInt()
    {
        String s1 = "42"; // the truth
        String s2 = "1234567890"; // contains every valid char
        String s3 = "123a"; // numbers with invalid chars
        String s4 = "Hallo Welt"; // only invalid chars
        String s5 = "50000000000000000000000000"; // int overflow
        
        assertTrue(Utils.isInt(s1));
        assertTrue(Utils.isInt(s2));
        assertFalse(Utils.isInt(s3));
        assertFalse(Utils.isInt(s4));
        assertFalse(Utils.isInt(s5));
    }
    
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
    
    @Test
    public void parsePoint()
    {
        String str1 = "11 11 world";
        Point p1 = new Point(11, 11);
        String world1 = "world";
        
        String str2 = "-11 11 1world";
        Point p2 = new Point(-11, 11);
        String world2 = "1world";
        
        String str3 = "11 -11 world1";
        Point p3 = new Point(11, -11);
        String world3 = "world1";
        
        String str4 = "-11 -11 1world1";
        Point p4 = new Point(-11, -11);
        String world4 = "1world1";
        
        assertEquals(Utils.parsePointString(str1), p1);
        assertEquals(Utils.parsePointString(str2), p2);
        assertEquals(Utils.parsePointString(str3), p3);
        assertEquals(Utils.parsePointString(str4), p4);
        
        assertEquals(Utils.parsePointWorld(str1), world1);
        assertEquals(Utils.parsePointWorld(str2), world2);
        assertEquals(Utils.parsePointWorld(str3), world3);
        assertEquals(Utils.parsePointWorld(str4), world4);
    }
}

class TestClass
{
    
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
    
    private int random;
    
    public TestClass(int i)
    {
        random = i;
    }
    
    protected int getRandomSeed()
    {
        return random;
    }
}
