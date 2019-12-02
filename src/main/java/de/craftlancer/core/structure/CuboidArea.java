package de.craftlancer.core.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import de.craftlancer.core.Utils;

public class CuboidArea implements Area {
    
    private Point3D corner1;
    private Point3D corner2;
    
    public CuboidArea(Point3D corner1, Point3D corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
    }
    
    public Point3D getFirstCorner() {
        return corner1;
    }
    
    public Point3D getSecondCorner() {
        return corner2;
    }
    
    public void setFirstCorner(Point3D point) {
        this.corner1 = point;
    }
    
    public void setSecondCorner(Point3D point) {
        this.corner2 = point;
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("corner1", corner1);
        map.put("corner2", corner2);
        
        return map;
    }
    
    public static CuboidArea deserialize(Map<String, Object> args) {
        return new CuboidArea((Point3D) args.get("corner1"), (Point3D) args.get("corner2"));
    }
    
    @Override
    public boolean isWithin(Location loc) {
        return Utils.isBetween(loc.getBlockX(), corner1.getX(), corner2.getX()) && 
               Utils.isBetween(loc.getBlockY(), corner1.getY(), corner2.getY()) && 
               Utils.isBetween(loc.getBlockZ(), corner1.getZ(), corner2.getZ());
    }
    
    @Override
    public boolean isWithin(Entity entity) {
        return isWithin(entity.getLocation());
    }
    
    @Override
    public boolean isWithin(Block block) {
        return isWithin(block.getLocation());
    }
    
}
