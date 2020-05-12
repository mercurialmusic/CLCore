package de.craftlancer.core;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class LastSeenCache implements Listener {
    private Map<UUID, Long> lastSeenMap = new ConcurrentHashMap<>();
    
    private final File rankingsFile;
    private final Plugin plugin;
    
    public LastSeenCache(Plugin plugin) {
        this.plugin = plugin;
        this.rankingsFile = new File(plugin.getDataFolder(), "lastSeen.yml");
        
        if (rankingsFile.exists()) {
            YamlConfiguration config = YamlConfiguration.loadConfiguration(rankingsFile);
            lastSeenMap = config.getConfigurationSection("lastSeenMap").getValues(false).entrySet().stream()
                                .collect(Collectors.toConcurrentMap(a -> UUID.fromString(a.getKey()), b -> (Long) b.getValue()));
        }
        
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
    
    public long getLastSeen(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        if (p != null)
            return System.currentTimeMillis();
        
        Long lastSeen = lastSeenMap.get(uuid);
        
        if (lastSeen != null)
            return lastSeen;
        
        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        return lastSeenMap.computeIfAbsent(player.getUniqueId(), a -> player.getLastPlayed());
    }
    
    public long getLastSeen(OfflinePlayer player) {
        if (player.isOnline())
            return System.currentTimeMillis();
        
        Long lastSeen = lastSeenMap.get(player.getUniqueId());
        
        if (lastSeen != null)
            return lastSeen;
        
        return lastSeenMap.computeIfAbsent(player.getUniqueId(), a -> player.getLastPlayed());
    }
    
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        lastSeenMap.put(event.getPlayer().getUniqueId(), System.currentTimeMillis());
    }
    
    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        config.set("lastSeenMap", lastSeenMap.entrySet().stream().collect(Collectors.toMap(a -> a.getKey().toString(), Map.Entry::getValue)));
        
        BukkitRunnable saveTask = new LambdaRunnable(() -> {
            try {
                config.save(rankingsFile);
            }
            catch (IOException e) {
                plugin.getLogger().log(Level.SEVERE, "Error while saving Rankings: ", e);
            }
        });
        
        if (plugin.isEnabled())
            saveTask.runTaskAsynchronously(plugin);
        else
            saveTask.run();
    }
}
