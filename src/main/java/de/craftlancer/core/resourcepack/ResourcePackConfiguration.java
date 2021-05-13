package de.craftlancer.core.resourcepack;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResourcePackConfiguration implements ConfigurationSerializable {
    
    private UUID owner;
    private boolean enabled = true;
    private long delay = 0;
    
    public ResourcePackConfiguration(UUID owner) {
        this.owner = owner;
    }
    
    public ResourcePackConfiguration(Map<String, Object> map) {
        this.owner = UUID.fromString((String) map.get("owner"));
        this.enabled = (boolean) map.get("enabled");
        this.delay = Long.parseLong((String) map.get("delay"));
    }
    
    @Override
    public @Nonnull
    Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("owner", owner.toString());
        map.put("enabled", enabled);
        map.put("delay", String.valueOf(delay));
        
        return map;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public long getDelay() {
        return delay;
    }
    
    public UUID getOwner() {
        return owner;
    }
    
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public void setDelay(long delay) {
        this.delay = delay;
    }
}
