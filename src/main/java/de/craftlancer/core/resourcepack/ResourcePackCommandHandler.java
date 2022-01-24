package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.CommandHandler;

public class ResourcePackCommandHandler extends CommandHandler {
    public ResourcePackCommandHandler(CLCore plugin, ResourcePackManager manager) {
        super(plugin);
        
        registerSubCommand("url", new ResourcePackSetURLCommand(plugin, manager));
        registerSubCommand("usePack", new ResourcePackSetUsingResourcePackCommand(plugin, manager));
        registerSubCommand("forcePack", new ResourcePackSetForceResourcePackCommand(plugin, manager));
        registerSubCommand("reload", new ResourcePackReloadCommand(plugin, manager));
        registerSubCommand("reloadAll", new ResourcePackReloadAllCommand(plugin, manager));
    }
}
