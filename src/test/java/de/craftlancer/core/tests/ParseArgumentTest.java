package de.craftlancer.core.tests;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;

import de.craftlancer.core.command.CommandHandler;

public class ParseArgumentTest
{
    @Test
    public void test()
    {
        String s1 = "set \"Na hallo Pong\"";
        String[] s1res = new String[] { "set", "Na hallo Pong" };
        
        String s2 = "set \"Na\" \"hallo Pong\"";
        String[] s2res = new String[] { "set", "\"Na\"", "hallo Pong" };
        
        String s3 = "set \"Na hallo \"Pong\"\"";
        String[] s3res = new String[] { "set", "Na hallo \"Pong\"" };
        
        String s4 = "set \"Na \"hallo Ping\" Pong\"";
        String[] s4res = new String[] { "set", "Na \"hallo Ping\" Pong" };
        
        String s5 = "set \"Na \"hallo Ping\"\" \"Pong";
        String[] s5res = null;
        
        String s6 = "set \"Na \"hallo Ping\"\" Pong\"";
        String[] s6res = new String[] { "set", "Na \"hallo Ping\"", "Pong\"" };
        
        assertArrayEquals(s1res, CommandHandler.parseArgumentStrings(s1.split(" ")));
        assertArrayEquals(s2res, CommandHandler.parseArgumentStrings(s2.split(" ")));
        assertArrayEquals(s3res, CommandHandler.parseArgumentStrings(s3.split(" ")));
        assertArrayEquals(s4res, CommandHandler.parseArgumentStrings(s4.split(" ")));
        assertArrayEquals(s5res, CommandHandler.parseArgumentStrings(s5.split(" ")));
        assertArrayEquals(s6res, CommandHandler.parseArgumentStrings(s6.split(" ")));
    }
    
}
