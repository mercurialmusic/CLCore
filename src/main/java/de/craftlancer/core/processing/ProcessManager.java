package de.craftlancer.core.processing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.scheduler.BukkitRunnable;

import de.craftlancer.core.processing.interfaces.IProcessManager;

public final class ProcessManager implements IProcessManager
{
    public static final int VERSION = 1;
    public static final long TICK_TIME = 20;
    
    private List<PluginProcess> activeProcesses = new ArrayList<>();
    
    public ProcessManager(Plugin plugin)
    {
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                tick();
            }
        }.runTaskTimer(plugin, TICK_TIME, TICK_TIME);
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
    
    @Override
    public int getVersion()
    {
        return VERSION;
    }
    
    public static void load(Plugin plugin)
    {
        ServicesManager manager = Bukkit.getServicesManager();
        
        if (!manager.isProvidedFor(IProcessManager.class))
            manager.register(IProcessManager.class, new ProcessManager(plugin), plugin, ServicePriority.Normal);
        else
        {
            Collection<RegisteredServiceProvider<IProcessManager>> handlers = manager.getRegistrations(IProcessManager.class);
            List<IProcessManager> remove = new ArrayList<>();
            IProcessManager newest = null;
            for (RegisteredServiceProvider<IProcessManager> handler : handlers)
            {
                IProcessManager provider = handler.getProvider();
                if (newest == null || provider.getVersion() > newest.getVersion())
                {
                    remove.add(newest);
                    newest = provider;
                }
                else
                    remove.add(provider);
            }
            
            if (newest == null || VERSION > newest.getVersion())
            {
                remove.add(newest);
                manager.register(IProcessManager.class, new ProcessManager(plugin), plugin, ServicePriority.Normal);
            }
            
            for (IProcessManager provider : remove)
                manager.unregister(provider);
        }
    }
}
