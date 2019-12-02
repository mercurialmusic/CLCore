package de.craftlancer.core.structure;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;

import de.craftlancer.core.Utils;

public class RectangleArea implements Area {
    
    private Point2D corner1;
    private Point2D corner2;
        
    public RectangleArea(Point2D corner1, Point2D corner2) {
        this.corner1 = corner1;
        this.corner2 = corner2;
    }
    
    public void setFirstCorner(Point2D point) {
        this.corner1 = point;
    }
    
    public void setSecondCorner(Point2D point) {
        this.corner2 = point;
    }
    
    @Override
    public boolean isWithin(Location loc) {
        return Utils.isBetween(loc.getBlockX(), corner1.getX(), corner2.getX()) && Utils.isBetween(loc.getBlockZ(), corner1.getY(), corner2.getY());
    }
    
    @Override
    public boolean isWithin(Entity entity) {
        return isWithin(entity.getLocation());
    }
    
    @Override
    public boolean isWithin(Block block) {
        return isWithin(block.getLocation());
    }
    
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("corner1", corner1);
        map.put("corner2", corner2);
        
        return map;
    }
    
    public static RectangleArea deserialize(Map<String, Object> args) {
        return new RectangleArea((Point2D) args.get("corner1"), (Point2D) args.get("corner2"));
    }
}
