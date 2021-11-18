package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.CommandHandler;

public class ResourcePackCommandHandler extends CommandHandler {
    public ResourcePackCommandHandler(CLCore plugin, ResourcePackManager manager) {
        super(plugin);
        
        registerSubCommand("setURL", new ResourcePackSetURLCommand(plugin, manager));
        registerSubCommand("setUsingResourcePack", new ResourcePackSetUsingResourcePackCommand(plugin, manager));
        registerSubCommand("setForceResourcePack", new ResourcePackSetForceResourcePackCommand(plugin, manager));
        registerSubCommand("reload", new ResourcePackReloadCommand(plugin, manager));
//        registerSubCommand("toggle", new ResourcePackToggleCommand(plugin, manager));
        registerSubCommand("setDelay", new ResourcePackSetDelayCommand(plugin, manager));
    }
}
