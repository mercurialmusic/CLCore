package de.craftlancer.core;

import java.util.Arrays;
import java.util.stream.IntStream;

public class IntRingBuffer {
    private int[] playerBuffer;
    private int currentOffset = 0;
    private final int size;
    
    public IntRingBuffer(int size) {
        this.size = size;
        playerBuffer = new int[size];
    }
    
    public void push(int value) {
        playerBuffer[currentOffset] = value;
        increaseOffset();
    }
    
    private void increaseOffset() {
        currentOffset++;
        if (currentOffset >= size)
            currentOffset = 0;
    }
    
    public IntStream stream() {
        return Arrays.stream(playerBuffer);
    }
}
