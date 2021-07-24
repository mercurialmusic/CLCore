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
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Allows players to set a location to have a compass emoji be sent to guide them.
 */
public class NavigationManager implements MessageRegisterable {
    
    private CLCore plugin;
    private Map<UUID, Location> destinations = new HashMap<>();
    
    public NavigationManager(CLCore plugin) {
        
        this.plugin = plugin;
        plugin.getCommand("navigation").setExecutor(new NavigationCommandHandler(plugin, this));
        
        File file = new File(plugin.getDataFolder(), "navigation.yml");
        
        if (file.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            
            ConfigurationSection destinationSection = config.getConfigurationSection("destinations");
            
            if (destinationSection != null)
                for (String key : destinationSection.getKeys(false))
                    destinations.put(UUID.fromString(key), destinationSection.getLocation(key));
        }
        
        new LambdaRunnable(() -> Bukkit.getOnlinePlayers().stream().filter(p -> destinations.containsKey(p.getUniqueId()))
                .forEach(this::sendLocationUpdate)).runTaskTimer(plugin, 20, 3);
        
        MessageUtil.register(this, new TextComponent("§8[§cNavigation§8]"));
    }
    
    public void save() {
        File file = new File(plugin.getDataFolder(), "navigation.yml");
        
        BukkitRunnable runnable = new LambdaRunnable(() -> {
            try {
                if (!file.exists())
                    file.createNewFile();
                
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                
                ConfigurationSection destinationSection = config.createSection("destinations");
                
                destinations.forEach(((uuid, location) -> destinationSection.set(uuid.toString(), location)));
                
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
    
    public void sendLocationUpdate(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getUnicode(player, destinations.get(player.getUniqueId()))
                + ChatColor.GOLD + " Move forward to complete the quest"));
    }
    
    public String getUnicode(Player player, Location destination) {
        
        if (destination == null)
            return CompassUnicode.ZERO.toString();
        
        if (!player.getWorld().equals(destination.getWorld()))
            return CompassUnicode.ZERO.toString();
        
        double xDiff = destination.getX() - player.getLocation().getX();
        double zDiff = destination.getZ() - player.getLocation().getZ();
        
        double DistanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        double newYaw = Math.acos(xDiff / DistanceXZ) * 180 / Math.PI;
        if (zDiff < 0.0)
            newYaw = newYaw + Math.abs(180 - newYaw) * 2;
        newYaw = (newYaw - 90);
        
        double newAngle = player.getEyeLocation().getYaw() - newYaw;
        while (newAngle < 0)
            newAngle += 360;
        while (newAngle > 360)
            newAngle -= 360;
        
        int num = getNumberCompass(newAngle);
        
        return CompassUnicode.values()[num].toString();
    }
    
    private int getNumberCompass(double angle) {
        //0 <= num <= 31
        int num = Math.min(31, (int) ((angle) / 11.25));
        
        return num < 17 ? 16 - num : 32 - (num - 16);
    }
    
    public Map<UUID, Location> getDestinations() {
        return destinations;
    }
    
    @Override
    public String getMessageID() {
        return "NavigationManager";
    }
}
