package de.craftlancer.core.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.util.NumberConversions;

public class Point3D implements ConfigurationSerializable {
    private final int x;
    private final int y;
    private final int z;
    
    public Point3D(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public Point3D(Location location) {
        this(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    public int getX() {
        return x;
    }
    
    public int getY() {
        return y;
    }
    
    public int getZ() {
        return z;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("x", getX());
        map.put("y", getY());
        map.put("z", getZ());
        
        return map;
    }
    
    public static Point3D deserialize(Map<String, Object> map) {
        return new Point3D(NumberConversions.toInt(map.get("x")), NumberConversions.toInt(map.get("y")), NumberConversions.toInt(map.get("z")));
    }
}
