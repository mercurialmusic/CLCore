package de.craftlancer.core.clipboard;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.Utils;
import de.craftlancer.core.util.ParticleUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import java.util.UUID;

public class Clipboard {
    
    private UUID owner;
    private Location location1;
    private Location location2;
    private World world;
    private BukkitTask runnable;
    private ClipboardManager manager;
    
    public Clipboard(ClipboardManager manager, UUID owner, World world) {
        this.owner = owner;
        this.manager = manager;
        this.world = world;
        
        new LambdaRunnable(() -> manager.removeClipboard(owner)).runTaskLater(CLCore.getInstance(), 6000);
    }
    
    public void remove() {
        if (runnable == null || runnable.isCancelled())
            return;
        
        runnable.cancel();
    }
    
    
    public void setlocation1(Location location1) {
        this.location1 = location1;
        
        spawnParticles();
    }
    
    public void setlocation2(Location location2) {
        this.location2 = location2;
        
        spawnParticles();
    }
    
    public Location getlocation1() {
        return location1;
    }
    
    public Location getlocation2() {
        return location2;
    }
    
    public World getWorld() {
        return world;
    }
    
    public BoundingBox toBoundingBox() {
        if (location1 == null || location2 == null)
            return null;
        
        return new BoundingBox(location1.getX(), location1.getY(), location1.getZ(),
                location2.getX(), location2.getY(), location2.getZ());
    }
    
    public UUID getOwner() {
        return owner;
    }
    
    private void spawnParticles() {
        if (runnable != null)
            runnable.cancel();
        
        Location pos1, pos2;
        
        if (location1 == null && location2 == null)
            return;
        
        if (location1 == null) {
            pos1 = location2.clone();
            pos2 = location2.clone();
        } else if (location2 == null) {
            pos1 = location1.clone();
            pos2 = location1.clone();
        } else {
            pos1 = location1;
            pos2 = location2;
        }
        
        if (pos1.getWorld() != pos2.getWorld())
            return;
        
        this.runnable = new BukkitRunnable() {
            
            private double minX = Math.min(pos1.getX(), pos2.getX());
            private double minY = Math.min(pos1.getY(), pos2.getY());
            private double minZ = Math.min(pos1.getZ(), pos2.getZ());
            private double maxX = Math.max(pos1.getX(), pos2.getX()) + 1;
            private double maxY = Math.max(pos1.getY(), pos2.getY()) + 1;
            private double maxZ = Math.max(pos1.getZ(), pos2.getZ()) + 1;
            
            private Location minLocation = new Location(pos1.getWorld(), minX, minY, minZ);
            private Location maxLocation = new Location(pos1.getWorld(), maxX, maxY, maxZ);
            
            @Override
            public void run() {
                if (!Utils.isChunkLoaded(pos1) || !Utils.isChunkLoaded(pos2))
                    return;
                
                ParticleUtil.spawnParticleRect(minLocation, maxLocation, Color.WHITE);
            }
        }.runTaskTimer(CLCore.getInstance(), 1, 20);
    }
}
