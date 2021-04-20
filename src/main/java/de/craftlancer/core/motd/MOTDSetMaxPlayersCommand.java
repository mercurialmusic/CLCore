package de.craftlancer.core.motd;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class MOTDSetMaxPlayersCommand extends SubCommand {
    
    private CLCore plugin;
    private MOTDManager manager;
    
    public MOTDSetMaxPlayersCommand(CLCore plugin, MOTDManager manager) {
        super("clcore.admin", plugin, true);
        
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You do not have access to this command.");
            return null;
        }
        
        if (args.length < 2) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You must enter a number.");
            return null;
        }
        
        try {
            manager.setMaxPlayers(Integer.parseInt(args[1]));
            MessageUtil.sendMessage(manager, sender, MessageLevel.SUCCESS, "Max players set.");
            return null;
        } catch (NumberFormatException e) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You must enter a valid number.");
            return null;
            
        }
    }
    
    @Override
    public void help(CommandSender sender) {
        
    }
}
