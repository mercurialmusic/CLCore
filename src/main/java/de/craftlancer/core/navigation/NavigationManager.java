package de.craftlancer.core.navigation;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.util.MessageRegisterable;
import de.craftlancer.core.util.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Allows players to set a location to have a compass emoji be sent to guide them.
 */
public class NavigationManager implements MessageRegisterable, NavigationRegistrable {
    
    private final CLCore plugin;
    private Map<UUID, NavigationGoal> destinations = new HashMap<>();
    
    public NavigationManager(CLCore plugin) {
        this.plugin = plugin;
        
        File file = new File(plugin.getDataFolder(), "navigation.yml");
        
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            
            destinations = (config.getConfigurationSection("destinations").getValues(false))
                    .entrySet().stream().collect(Collectors.toMap(e -> UUID.fromString(e.getKey()), e -> (NavigationGoal) e.getValue()));
        }
        
        new LambdaRunnable(() -> Bukkit.getOnlinePlayers().stream().filter(p -> destinations.containsKey(p.getUniqueId()))
                .forEach((player) -> sendLocationUpdate(player, destinations.get(player.getUniqueId()))))
                .runTaskTimer(plugin, 20, 3);
        
        MessageUtil.register(this, new TextComponent("§8[§cNavigation§8]"));
    }
    
    public void save() {
        File file = new File(plugin.getDataFolder(), "navigation.yml");
        
        BukkitRunnable runnable = new LambdaRunnable(() -> {
            try {
                if (!file.exists())
                    file.createNewFile();
                
                YamlConfiguration config = new YamlConfiguration();
                
                config.set("destinations", destinations.entrySet().stream()
                        .collect(Collectors.toMap(e -> e.getKey().toString(), Map.Entry::getValue)));
                
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        if (plugin.isEnabled())
            runnable.runTaskAsynchronously(plugin);
        else
            runnable.run();
    }
    
    private void sendLocationUpdate(Player player, NavigationGoal goal) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(NavigationUtil.getUnicode(player, goal.getLocation())
                + " " + ChatColor.GOLD + goal.getMessage()));
    }
    
    public void register(Player player, NavigationGoal goal) {
        destinations.put(player.getUniqueId(), goal);
    }
    
    /**
     * @return true if key is present and goal is removed
     */
    public boolean unregister(Player player, NavigationRegistrable registrable) {
        if (!destinations.containsKey(player.getUniqueId()))
            return false;
        
        if (!destinations.get(player.getUniqueId()).getId().equals(registrable.getNavigationID()))
            return false;
        
        destinations.remove(player.getUniqueId());
        
        return true;
    }
    
    @Override
    public String getMessageID() {
        return "NavigationManager";
    }
    
    @Override
    public String getNavigationID() {
        return "NavigationManager";
    }
    
    public static class NavigationGoal implements ConfigurationSerializable {
        
        private String message;
        private Location location;
        private String id;
        
        public NavigationGoal(String message, Location location, NavigationRegistrable registrable) {
            this.message = message;
            this.location = location;
            this.id = registrable.getNavigationID();
        }
        
        public NavigationGoal(Map<String, Object> map) {
            this.message = (String) map.get("message");
            this.location = (Location) map.get("location");
            this.id = (String) map.get("id");
        }
        
        @Override
        public Map<String, Object> serialize() {
            Map<String, Object> map = new HashMap<>();
            
            map.put("message", message);
            map.put("location", location);
            map.put("id", id);
            
            return map;
        }
        
        public Location getLocation() {
            return location;
        }
        
        public String getMessage() {
            return message;
        }
        
        public String getId() {
            return id;
        }
    }
}
