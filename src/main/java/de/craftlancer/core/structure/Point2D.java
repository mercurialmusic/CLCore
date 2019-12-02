package de.craftlancer.core.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class Point2D implements ConfigurationSerializable {
    private final int x;
    private final int y;
    
    public Point2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("x", getX());
        map.put("y", getY());
        
        return map;
    }
    
    public static Point2D deserialize(Map<String, Object> map) {
        return new Point2D(NumberConversions.toInt(map.get("x")), NumberConversions.toInt(map.get("y")));
    }
}
