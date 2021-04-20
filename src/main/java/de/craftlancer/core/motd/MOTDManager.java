package de.craftlancer.core.motd;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.util.MessageRegisterable;
import de.craftlancer.core.util.MessageUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MOTDManager implements Listener, MessageRegisterable {
    
    private CLCore plugin;
    private List<String> motdList;
    private int maxPlayers;
    
    public MOTDManager(CLCore plugin) {
        this.plugin = plugin;
        
        plugin.getCommand("motd").setExecutor(new MOTDCommandHandler(plugin, this));
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        MessageUtil.register(this, new TextComponent("§8[§fMOTD§8]"));
        
        load();
    }
    
    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onServerListPing(ServerListPingEvent event) {
        event.setMaxPlayers(maxPlayers);
        if (motdList.size() > 0)
            event.setMotd(motdList.get((int) (Math.random() * motdList.size())));
    }
    
    protected void load() {
        File file = new File(plugin.getDataFolder(), "motdManager.yml");
        
        if (!file.exists())
            plugin.saveResource(file.getName(), false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        this.motdList = (List<String>) config.getList("motdList", new ArrayList<>());
        this.maxPlayers = config.getInt("maxPlayers");
    }
    
    public void save() {
        File file = new File(plugin.getDataFolder(), "motdManager.yml");
        
        if (!file.exists())
            plugin.saveResource(file.getName(), false);
        
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        config.set("motdList", motdList);
        config.set("maxPlayers", maxPlayers);
        
        BukkitRunnable runnable = new LambdaRunnable(() -> {
            try {
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
    
    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }
    
    public List<String> getMotdList() {
        return motdList;
    }
    
    public int getMaxPlayers() {
        return maxPlayers;
    }
    
    @Override
    public String getMessageID() {
        return "MOTDManager";
    }
}
