package de.craftlancer.core;

public enum Direction
{
    NORTH(2),
    WEST(1),
    SOUTH(0),
    EAST(3);
    
    private int facing;
    
    private Direction(int facing)
    {
        this.facing = facing;
    }
    
    public int getIntValue()
    {
        return facing;
    }
}
