package de.craftlancer.core.worldguard.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.craftlancer.core.util.AbstractCancellableEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author Weby &amp; Anrza (info@raidstone.net)
 * @version 1.0.0
 * @since 2/24/19
 */
public class RegionLeftEvent extends AbstractCancellableEvent {
    
    private final UUID uuid;
    private final ProtectedRegion region;
    private final String regionName;
    
    
    /**
     * This even is fired whenever a region is left.
     * It may be fired multiple times per tick, if several
     * regions are left at the same time.
     *
     * @param playerUUID The UUID of the player leaving the region.
     * @param region     WorldGuard's ProtectedRegion region.
     */
    public RegionLeftEvent(UUID playerUUID, @Nonnull ProtectedRegion region) {
        this.uuid = playerUUID;
        this.region = region;
        this.regionName = region.getId();
    }
    
    @Nonnull
    public UUID getUUID() {
        return uuid;
    }
    
    @Nullable
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
    
    @Nonnull
    public String getRegionName() {
        return regionName;
    }
    
    @Nonnull
    public ProtectedRegion getRegion() {
        return region;
    }
}
