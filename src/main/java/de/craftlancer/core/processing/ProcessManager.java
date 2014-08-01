package de.craftlancer.core.processing;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ProcessManager
{
    private List<PluginProcess> activeProcesses = new ArrayList<>();
    
    public ProcessManager(Plugin plugin, long timePerTick)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                tick();
            }
        }.runTaskTimer(plugin, timePerTick, timePerTick);
    }
    
    public void tick()
    {
        Set<PluginProcess> remove = new HashSet<>();
        
        for (PluginProcess process : activeProcesses)
        {
            if (process.isReady())
            {
                process.execute();
                remove.add(process);
            }
        }
        
        activeProcesses.removeAll(remove);
    }
    
}
