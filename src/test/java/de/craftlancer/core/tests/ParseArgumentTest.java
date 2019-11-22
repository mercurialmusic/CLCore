package de.craftlancer.core.tests;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import de.craftlancer.core.command.CommandHandler;

public class ParseArgumentTest
{    
    @Test
    public void test() {
        String s1 = "set \"Hallo Welt\"";
        String[] s1res = { "set", "Hallo Welt" };
        
        String s2 = "set \"Hallo \"Welt";
        String[] s2res = { "set", "\"Hallo", "\"Welt" };
        
        String s3 = "set \"Hallo\" Welt";
        String[] s3res = { "set", "Hallo", "Welt" };
        
        String s4 = "set \"Hallo \"Welt\"\"";
        String[] s4res = { "set", "Hallo \"Welt\"" };
        
        String s5 = "set \"Hallo \"schöne\" Welt\"";
        String[] s5res = { "set", "Hallo \"schöne\" Welt" };
                
        String s6 = "set Hallo Welt";
        String[] s6res = { "set", "Hallo", "Welt" };
        
        String s7 = "set \"Hallo\" \"Welt\"";
        String[] s7res = { "set", "Hallo", "Welt" };
        
        String s8 = "set Ha\"llo\" \"Wel\"t";
        String[] s8res = { "set", "Ha\"llo\"", "\"Wel\"t" };
        
        String s9 = "set \"Wel\"t Ha\"llo\"";
        String[] s9res = { "set", "Wel\"t Ha\"llo" };
        
        String s10 = "set \"Hallo Welt";
        String[] s10res = { "set", "\"Hallo", "Welt" };
        
        String s11 = "set \"Hallo \"sehr schöne\" Welt\"";
        String[] s11res = { "set", "Hallo \"sehr schöne\" Welt" };

        String s12 = "set \"Hallo \"sehr schöne\" Welt\"\"\"";
        String[] s12res = { "set", "Hallo \"sehr schöne\" Welt\"\"" };

        assertArrayEquals(s1res, CommandHandler.parseArgumentStrings(s1.split(" ")));
        assertArrayEquals(s2res, CommandHandler.parseArgumentStrings(s2.split(" ")));
        assertArrayEquals(s3res, CommandHandler.parseArgumentStrings(s3.split(" ")));
        assertArrayEquals(s4res, CommandHandler.parseArgumentStrings(s4.split(" ")));
        assertArrayEquals(s5res, CommandHandler.parseArgumentStrings(s5.split(" ")));
        assertArrayEquals(s6res, CommandHandler.parseArgumentStrings(s6.split(" ")));
        assertArrayEquals(s7res, CommandHandler.parseArgumentStrings(s7.split(" ")));
        assertArrayEquals(s8res, CommandHandler.parseArgumentStrings(s8.split(" ")));
        assertArrayEquals(s9res, CommandHandler.parseArgumentStrings(s9.split(" ")));
        assertArrayEquals(s10res, CommandHandler.parseArgumentStrings(s10.split(" ")));
        assertArrayEquals(s11res, CommandHandler.parseArgumentStrings(s11.split(" ")));
        assertArrayEquals(s12res, CommandHandler.parseArgumentStrings(s12.split(" ")));
    }
}
