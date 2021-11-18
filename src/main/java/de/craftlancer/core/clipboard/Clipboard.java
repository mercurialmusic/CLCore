package de.craftlancer.core.clipboard;

import de.craftlancer.clapi.clcore.clipboard.AbstractClipboard;
import de.craftlancer.core.CLCore;
import de.craftlancer.core.Utils;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import de.craftlancer.core.util.ParticleUtil;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Clipboard implements AbstractClipboard {
    
    private static final long TIMEOUT = 1 * 60 * 1000;
    
    private UUID owner;
    private Location location1;
    private Location location2;
    private World world;
    private BukkitTask particleTask;
    private long lastEdit;
    private Runnable remove;
    
    public Clipboard(UUID owner, World world, Runnable remove) {
        this.owner = owner;
        this.world = world;
        this.lastEdit = System.currentTimeMillis();
        this.remove = remove;
    }
    
    @Override
    public void remove() {
        if (particleTask == null || particleTask.isCancelled())
            return;
        
        particleTask.cancel();
    }
    
    
    @Override
    public void setLocation1(Location location1) {
        this.location1 = location1.clone();
        this.lastEdit = System.currentTimeMillis();
        
        spawnParticles();
    }
    
    @Override
    public void setLocation2(Location location2) {
        this.location2 = location2.clone();
        this.lastEdit = System.currentTimeMillis();
        
        spawnParticles();
    }
    
    @Override
    public Location getLocation1() {
        return location1.clone();
    }
    
    @Override
    public Location getLocation2() {
        return location2.clone();
    }
    
    @Override
    public World getWorld() {
        return world;
    }
    
    /**
     * @return true if both locations are not null.
     */
    @Override
    public boolean hasTwoPoints() {
        return location1 != null && location2 != null;
    }
    
    @Override
    public double getVolume() {
        return getHeight() * getLength() * getWidth();
    }
    
    /**
     * Gets the height in the Y direction
     */
    @Override
    public double getHeight() {
        return Math.abs(location1.getY() - location2.getY());
    }
    
    /**
     * Gets the length in the X direction
     */
    @Override
    public double getLength() {
        return Math.abs(location1.getX() - location2.getX());
    }
    
    /**
     * Gets the width in the Z direction
     */
    @Override
    public double getWidth() {
        return Math.abs(location1.getZ() - location2.getZ());
    }
    
    /**
     * Gets a list of all blocks in the clipboard.
     */
    @Override
    public List<Block> toBlockList() {
        List<Block> blocks = new ArrayList<>();
        BoundingBox box = toBoundingBox();
        
        for (int x = (int) box.getMinX(); x <= (int) box.getMaxX(); x++)
            for (int y = (int) box.getMinY(); y <= (int) box.getMaxY(); y++)
                for (int z = (int) box.getMinZ(); z <= (int) box.getMaxZ(); z++)
                    blocks.add(world.getBlockAt(x, y, z));
        
        return blocks;
    }
    
    @Override
    public BoundingBox toBoundingBox() {
        if (location1 == null || location2 == null)
            return null;
        
        return new BoundingBox(location1.getX(), location1.getY(), location1.getZ(),
                location2.getX(), location2.getY(), location2.getZ());
    }
    
    @Override
    public UUID getOwner() {
        return owner;
    }
    
    private void messagePlayer() {
        Player player = Bukkit.getPlayer(owner);
        if (player != null)
            MessageUtil.sendMessage(ClipboardManager.getInstance(),
                    player,
                    MessageLevel.INFO,
                    "Your clipboard has not been edited for " + TIMEOUT / (60 * 1000) + " minutes, and has expired.");
    }
    
    private void spawnParticles() {
        if (particleTask != null)
            particleTask.cancel();
        
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
        
        this.particleTask = new BukkitRunnable() {
            
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
                if (System.currentTimeMillis() - lastEdit >= TIMEOUT) {
                    remove.run();
                    messagePlayer();
                    cancel();
                }
                
                if (!Utils.isChunkLoaded(pos1) || !Utils.isChunkLoaded(pos2))
                    return;
                
                ParticleUtil.spawnParticleRect(minLocation, maxLocation, Color.WHITE);
            }
        }.runTaskTimer(CLCore.getInstance(), 1, 20);
    }
}
