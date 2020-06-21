package de.craftlancer.core.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.plugin.Plugin;

public class PluginFileLogger extends Logger {

    public PluginFileLogger(String name, Plugin plugin, String logFileFormat) {
        super(name, null);
        // create dir
        new File(plugin.getDataFolder(), "/logs/").mkdirs();

        setUseParentHandlers(false);
        try {
            Handler handler = new FileHandler(plugin.getDataFolder() + "/logs/" + logFileFormat, true);
            handler.setFormatter(new SimpleLogFormatter());
            
            addHandler(handler);
            addHandler(new PassthroughHandler(plugin.getLogger()));
        }
        catch (SecurityException | IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error while creating logger", e);
        }
    }
    
}
