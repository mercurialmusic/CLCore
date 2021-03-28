package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.CommandHandler;

public class ResourcePackCommandHandler extends CommandHandler {
    public ResourcePackCommandHandler(CLCore plugin, ResourcePackManager manager) {
        super(plugin);
        
        registerSubCommand("setURL", new ResourcePackSetURLCommand(plugin, manager));
        registerSubCommand("setUsingResourcePack", new ResourcePackSetUsingResourcePackCommand(plugin, manager));
    }
}
