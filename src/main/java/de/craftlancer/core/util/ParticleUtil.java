package de.craftlancer.core.util;

import java.util.function.Supplier;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.Vector;

import de.craftlancer.core.CLCore;

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
    
    public static void spawnParticleCircle(Location startingLocation, double radius) {
        spawnParticleCircle(startingLocation, radius, Color.WHITE);
    }
    
    public static void spawnParticleCircle(Location startingLocation, double radius, Color color) {
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
    
    public static void spawnSpinningParticleCircle(Supplier<Location> location, int numTicks, int particlesPerTick, long peroid, double radius, Color color) {
        Particle.DustOptions particle = new Particle.DustOptions(color, 1F);
        new SpinningParticle(location, particlesPerTick, numTicks, radius, particle).runTaskTimer(CLCore.getInstance(), 0, peroid);
    }
    
    private static class SpinningParticle extends BukkitRunnable {
        
        private final int particlesPerTick;
        private final int numIterations;
        private final double radius;
        private final Supplier<Location> location;
        private final Particle.DustOptions particle;
        private final double increment;
        
        private int iteration = 0;
        
        public SpinningParticle(Supplier<Location> location, int particlesPerTick, int numIterations, double radius, Particle.DustOptions particle) {
            this.location = location;
            this.particlesPerTick = particlesPerTick;
            this.numIterations = numIterations;
            this.radius = radius;
            this.particle = particle;
            
            this.increment = (2 * Math.PI) / (particlesPerTick * numIterations);
        }
        
        @Override
        public void run() {
            Location loc = location.get();
            
            for (int i = 0; i < particlesPerTick; i++) {
                double angle = (iteration * particlesPerTick + i) * increment;
                double x = (loc.getX()) + (radius * Math.cos(angle));
                double z = (loc.getZ()) + (radius * Math.sin(angle));
                
                loc.getWorld().spawnParticle(Particle.REDSTONE, new Location(loc.getWorld(), x, loc.getY(), z), 1, particle);
            }
            
            if (++iteration >= numIterations)
                this.cancel();
        }
    }
    
    public static Color getColorFromString(String string) {
        switch (string.toUpperCase()) {
            case "BLACK":
                return Color.BLACK;
            case "BLUE":
                return Color.BLUE;
            case "PURPLE":
                return Color.PURPLE;
            case "AQUA":
                return Color.AQUA;
            case "FUCHSIA":
                return Color.FUCHSIA;
            case "GRAY":
                return Color.GRAY;
            case "GREEN":
                return Color.GREEN;
            case "LIME":
                return Color.LIME;
            case "MAROON":
                return Color.MAROON;
            case "NAVY":
                return Color.NAVY;
            case "OLIVE":
                return Color.OLIVE;
            case "ORANGE":
                return Color.ORANGE;
            case "RED":
                return Color.RED;
            case "SILVER":
                return Color.SILVER;
            case "TEAL":
                return Color.TEAL;
            case "YELLOW":
                return Color.YELLOW;
            default:
                return Color.WHITE;
        }
    }
}
