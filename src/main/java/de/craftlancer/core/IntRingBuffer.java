package de.craftlancer.core;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class IntRingBuffer {
    private int[] buffer;
    private int currentOffset = 0;
    private final int size;
    
    public IntRingBuffer(int size) {
        this.size = size;
        buffer = new int[size];
    }
    
    public IntRingBuffer(int size, int... values) {
        this(size);
        push(values);
    }
    
    public IntRingBuffer(int size, List<Integer> values) {
        this(size);
        push(values);
    }
    
    public void push(int... values) {
        Arrays.stream(values).forEach(this::push);
    }
    
    public void push(List<Integer> values) {
        values.forEach(this::push);
    }
    
    public void push(int value) {
        buffer[currentOffset] = value;
        increaseOffset();
    }
    
    public int get(int index) {
        if(Math.abs(index) >= size)
            throw new IllegalArgumentException("Tried to access IntRingBuffer index that's larger than the buffer.");
        
        return buffer[(size + index + currentOffset) % size];
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
