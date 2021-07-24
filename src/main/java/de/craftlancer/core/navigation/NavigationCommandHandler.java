package de.craftlancer.core.navigation;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.CommandHandler;

public class NavigationCommandHandler extends CommandHandler {
    public NavigationCommandHandler(CLCore plugin, NavigationManager manager) {
        super(plugin);
        
        registerSubCommand("set", new NavigationSetCommand(plugin, manager));
        registerSubCommand("stop", new NavigationStopCommand(plugin, manager));
    }
}
