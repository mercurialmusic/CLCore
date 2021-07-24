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
 * @since 2/24/19
 */
public class RegionsLeftEvent extends AbstractCancellableEvent {
    
    private final UUID uuid;
    private final Set<ProtectedRegion> regions;
    private final Set<String> regionsNames;
    
    /**
     * This even is fired whenever one or several regions are left.
     *
     * @param playerUUID The UUID of the player leaving the regions.
     * @param regions    Set of WorldGuard's ProtectedRegion regions.
     */
    public RegionsLeftEvent(UUID playerUUID, @Nullable Set<ProtectedRegion> regions) {
        this.uuid = playerUUID;
        this.regionsNames = new HashSet<>();
        this.regions = new HashSet<>();
        
        if (regions != null) {
            this.regions.addAll(regions);
            for (ProtectedRegion region : regions) {
                this.regionsNames.add(region.getId());
            }
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
    public Set<ProtectedRegion> getRegions() {
        return regions;
    }
    
    @Nonnull
    public Set<String> getRegionsNames() {
        return regionsNames;
    }
}
