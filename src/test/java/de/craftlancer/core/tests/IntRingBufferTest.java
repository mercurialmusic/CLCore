package de.craftlancer.core.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import de.craftlancer.core.IntRingBuffer;

public class IntRingBufferTest {
    
    @Test
    public void test() {
        IntRingBuffer buffer = new IntRingBuffer(5);
        
        assertTrue(buffer.stream().allMatch(a -> a == 0));
        
        buffer.push(1);
        buffer.push(2);
        buffer.push(3);
        
        assertArrayEquals(new int[] { 0, 0, 1, 2, 3 }, buffer.stream().toArray());

        buffer.push(1);
        buffer.push(2);
        buffer.push(3);

        assertArrayEquals(new int[] { 2, 3, 1, 2, 3 }, buffer.stream().toArray());
    }
    
}
