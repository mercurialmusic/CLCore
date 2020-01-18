package de.craftlancer.core;

import java.util.Arrays;
import java.util.stream.IntStream;

public class IntRingBuffer {
    private int[] buffer;
    private int currentOffset = 0;
    private final int size;
    
    public IntRingBuffer(int size) {
        this.size = size;
        buffer = new int[size];
    }
    
    public void push(int value) {
        buffer[currentOffset] = value;
        increaseOffset();
    }
    
    private void increaseOffset() {
        currentOffset++;
        if (currentOffset >= size)
            currentOffset = 0;
    }
    
    public IntStream stream() {
        return IntStream.concat(Arrays.stream(buffer, currentOffset, size), Arrays.stream(buffer, 0, currentOffset));
    }
}
