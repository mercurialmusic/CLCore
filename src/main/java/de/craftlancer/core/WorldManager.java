package de.craftlancer.core;

import java.io.File;

import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.YamlConfiguration;

public class WorldManager {
    
    public WorldManager(CLCore plugin) {
        File file = new File(plugin.getDataFolder(), "worlds.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        
        for(String name : config.getStringList("worlds")) {
            plugin.getLogger().info(() -> "Loading world " + name);
            World w = WorldCreator.name(name).createWorld();
            
            if(w == null)
                plugin.getLogger().warning(() -> "Couldn't load world " + name);
        }
    }
}
