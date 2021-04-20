package de.craftlancer.core.motd;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.Utils;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class MOTDRemoveCommand extends SubCommand {
    
    private CLCore plugin;
    private MOTDManager manager;
    
    public MOTDRemoveCommand(CLCore plugin, MOTDManager manager) {
        super("clcore.admin", plugin, true);
        
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length > 1)
            return Utils.getMatches(args[args.length - 1], manager.getMotdList());
        
        return Collections.emptyList();
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
            int pos = Integer.parseInt(args[1]);
            
            if (pos < 0 || manager.getMotdList().size() <= pos) {
                MessageUtil.sendMessage(manager, sender, MessageLevel.SUCCESS, "The given index is invalid.");
                return null;
            } else {
                manager.getMotdList().remove(Integer.parseInt(args[1]));
                MessageUtil.sendMessage(manager, sender, MessageLevel.SUCCESS, "Removed message at given position.");
                return null;
            }
        } catch (NumberFormatException e) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You must enter a valid number.");
            return null;
            
        }
    }
    
    @Override
    public void help(CommandSender sender) {
        
    }
}
