package de.craftlancer.core.motd;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.CommandHandler;

public class MOTDCommandHandler extends CommandHandler {
    public MOTDCommandHandler(CLCore plugin, MOTDManager manager) {
        super(plugin);
        
        registerSubCommand("add", new MOTDAddCommand(plugin, manager));
        registerSubCommand("remove", new MOTDRemoveCommand(plugin, manager));
        registerSubCommand("setMaxPlayers", new MOTDSetMaxPlayersCommand(plugin, manager));
    }
}
