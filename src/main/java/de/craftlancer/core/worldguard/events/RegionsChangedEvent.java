package de.craftlancer.core.worldguard.events;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.craftlancer.core.util.AbstractCancellableEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Weby &amp; Anrza (info@raidstone.net)
 * @version 1.0.0
 * @since 12/12/2019
 */
public class RegionsChangedEvent extends AbstractCancellableEvent {
    private final UUID uuid;
    private final Set<ProtectedRegion> previousRegions = new HashSet<>();
    private final Set<ProtectedRegion> currentRegions = new HashSet<>();
    private final Set<String> previousRegionsNames = new HashSet<>();
    private final Set<String> currentRegionsNames = new HashSet<>();
    
    /**
     * This even is fired whenever a region is entered.
     * It may be fired multiple times per tick, if several
     * regions are entered at the same time.
     *
     * @param playerUUID The UUID of the player entering the region.
     * @param previous   Set of WorldGuard's ProtectedRegion the player was in before this event
     * @param current    Set of WorldGUard's ProtectedRegion the player is currently in
     */
    public RegionsChangedEvent(UUID playerUUID, @Nonnull Set<ProtectedRegion> previous, @Nonnull Set<ProtectedRegion> current) {
        this.uuid = playerUUID;
        previousRegions.addAll(previous);
        currentRegions.addAll(current);
        
        for (ProtectedRegion r : current) {
            currentRegionsNames.add(r.getId());
        }
        
        for (ProtectedRegion r : previous) {
            previousRegionsNames.add(r.getId());
        }
        
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
    public Set<String> getCurrentRegionsNames() {
        return currentRegionsNames;
    }
    
    @Nonnull
    public Set<String> getPreviousRegionsNames() {
        return previousRegionsNames;
    }
    
    @Nonnull
    public Set<ProtectedRegion> getCurrentRegions() {
        return currentRegions;
    }
    
    @Nonnull
    public Set<ProtectedRegion> getPreviousRegions() {
        return previousRegions;
    }
}
