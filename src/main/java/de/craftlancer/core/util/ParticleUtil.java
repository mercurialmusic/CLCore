package de.craftlancer.core.util;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

public class ParticleUtil {
    private ParticleUtil() {
    }
    
    public static void spawnParticleSingleBeam(Location startingLocation) {
        spawnSingleParticleBeam(startingLocation, Color.WHITE);
    }
    
    public static void spawnSingleParticleBeam(Location startingLocation, Color color) {
        Location center = startingLocation.clone();
        Particle.DustOptions particle = new Particle.DustOptions(color, 1F);
        center.setX(center.getX() + 0.5);
        center.setZ(center.getZ() + 0.5);
        for (double i = startingLocation.getY(); i < startingLocation.getY() + 1; i += 0.05) {
            center.setY(i);
            
            center.getWorld().spawnParticle(Particle.REDSTONE, center, 1, particle);
        }
    }
    
    public static void spawnParticleCircle(Location startingLocation) {
        spawnParticleCircle(startingLocation, 1);
    }
    
    public static void spawnParticleCircle(Location startingLocation, int radius) {
        spawnParticleCircle(startingLocation, radius, Color.WHITE);
    }
    
    public static void spawnParticleCircle(Location startingLocation, int radius, Color color) {
        World world = startingLocation.getWorld();
        Particle.DustOptions particle = new Particle.DustOptions(color, 1F);
        
        double increment = (2 * Math.PI) / 150;
        for (int i = 0; i < 150; i++) {
            double angle = i * increment;
            double x = (startingLocation.getX() + 0.5) + (radius * Math.cos(angle));
            double z = (startingLocation.getZ() + 0.5) + (radius * Math.sin(angle));
            world.spawnParticle(Particle.REDSTONE, new Location(world, x, startingLocation.getY(), z), 1, particle);
        }
    }
    
    public static void spawnParticleRect(Location start, Location end) {
        spawnParticleRect(start, end, Color.WHITE);
    }
    
    public static void spawnParticleRect(Location pos1, Location pos2, Color color) {
        if (pos1.getWorld() != pos2.getWorld())
            return;
        
        World w = pos1.getWorld();
        Particle.DustOptions particle = new Particle.DustOptions(color, 1F);
        BoundingBox bb = new BoundingBox(pos1.getX(), pos1.getY(), pos1.getZ(), pos2.getX(), pos2.getY(), pos2.getZ());
        
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMinZ()), new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMinZ()), particle);
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMinZ()), new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMinZ()), particle);
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMaxZ()), new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMaxZ()), particle);
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMaxZ()), new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()), particle);
        
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMinZ()), new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMinZ()), particle);
        spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMinZ()), new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMinZ()), particle);
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMaxZ()), new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMaxZ()), particle);
        spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMaxZ()), new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()), particle);
        
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMinY(), bb.getMinZ()), new Location(w, bb.getMinX(), bb.getMinY(), bb.getMaxZ()), particle);
        spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMinZ()), new Location(w, bb.getMaxX(), bb.getMinY(), bb.getMaxZ()), particle);
        spawnParticleLine(new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMinZ()), new Location(w, bb.getMinX(), bb.getMaxY(), bb.getMaxZ()), particle);
        spawnParticleLine(new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMinZ()), new Location(w, bb.getMaxX(), bb.getMaxY(), bb.getMaxZ()), particle);
    }
    
    private static void spawnParticleLine(Location start, Location end, Particle.DustOptions particle) {
        double step = 0.1D;
        
        Vector direction = end.toVector().subtract(start.toVector());
        double numParticles = (direction.length() / step);
        direction.multiply(1D / numParticles);
        
        Location current = start.clone();
        for (int i = 0; i < numParticles; i++) {
            current.getWorld().spawnParticle(Particle.REDSTONE, current, 1, particle);
            current.add(direction);
        }
    }
}
