package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ResourcePackManager implements Listener {
    
    private static ResourcePackManager instance;
    
    private CLCore plugin;
    private Map<UUID, PlayerResourcePackStatusEvent.Status> playerStatuses = new HashMap<>();
    private String url;
    private boolean usingResourcePack;
    
    public ResourcePackManager(CLCore plugin) {
        instance = this;
        this.plugin = plugin;
        
        File file = new File(plugin.getDataFolder(), "resourcePack.yml");
        
        if (!file.exists())
            plugin.saveResource(file.getName(), false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        this.usingResourcePack = config.getBoolean("useResourcePack", true);
        this.url = config.getString("resourcePackURL", "");
    }
    
    public void save() {
        File file = new File(plugin.getDataFolder(), "resourcePack.yml");
        
        if (!file.exists())
            plugin.saveResource(file.getName(), false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        LambdaRunnable saveTask = new LambdaRunnable(() -> {
            config.set("useResourcePack", usingResourcePack);
            config.set("resourcePackURL", url);
            
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        
        if (plugin.isEnabled())
            saveTask.runTaskAsynchronously(plugin);
        else
            saveTask.run();
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        
        if (usingResourcePack && !url.isEmpty())
            player.setResourcePack(url);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onResourcePackInteract(PlayerResourcePackStatusEvent event) {
        playerStatuses.put(event.getPlayer().getUniqueId(), event.getStatus());
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerStatuses.remove(event.getPlayer().getUniqueId());
    }
    
    public PlayerResourcePackStatusEvent.Status getStatus(UUID uuid) {
        return playerStatuses.getOrDefault(uuid, PlayerResourcePackStatusEvent.Status.DECLINED);
    }
    
    public PlayerResourcePackStatusEvent.Status getStatus(Player player) {
        return getStatus(player.getUniqueId());
    }
    
    public boolean isFullyAccepted(Player player) {
        return isFullyAccepted(player.getUniqueId());
    }
    
    public boolean isFullyAccepted(UUID uuid) {
        return getStatus(uuid) == PlayerResourcePackStatusEvent.Status.SUCCESSFULLY_LOADED;
    }
    
    public static ResourcePackManager getInstance() {
        return instance;
    }
    
    public void setUrl(String url) {
        this.url = url;
    }
    
    public void setUsingResourcePack(boolean usingResourcePack) {
        this.usingResourcePack = usingResourcePack;
    }
}
