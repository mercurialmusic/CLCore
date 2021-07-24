package de.craftlancer.core.worldguard;

import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import de.craftlancer.core.menu.Menu;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RegionConfiguration implements ConfigurationSerializable {
    
    private String region;
    private Map<MovementType, List<String>> sounds;
    private Menu menu;
    
    public RegionConfiguration(String region) {
        this.region = region;
    }
    
    public RegionConfiguration(Map<String, Object> map) {
        this.region = (String) map.get("region");
        this.sounds = ((Map<String, List<String>>) map.get("sounds")).entrySet().stream()
                .collect(Collectors.toMap(e -> MovementType.valueOf(e.getKey()), Map.Entry::getValue));
    }
    
    @Override
    public @Nonnull
    Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("region", region);
        map.put("sounds", sounds.entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().name(), Map.Entry::getValue)));
        
        return map;
    }
    
    public void display(Player player) {
        if (menu == null)
            createInventory();
        
        player.openInventory(menu.getInventory());
    }
    
    private void createInventory() {
    
    }
    
    public String getRegion() {
        return region;
    }
    
    public Map<MovementType, List<String>> getSounds() {
        return sounds;
    }
    
    public void onRegionEnter(Player player, ProtectedRegion region) {
    
    }
    
    public void onRegionLeave(Player player, ProtectedRegion region) {
    
    }
    
    public enum MovementType {
        ENTER,
        EXIT
    }
}
