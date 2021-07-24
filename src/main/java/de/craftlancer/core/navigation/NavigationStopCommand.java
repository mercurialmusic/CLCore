package de.craftlancer.core.navigation;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class NavigationStopCommand extends SubCommand {
    
    private CLCore plugin;
    private NavigationManager manager;
    
    public NavigationStopCommand(CLCore plugin, NavigationManager manager) {
        super("", plugin, false);
        
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You do not have access to this command.");
            return null;
        }
        
        if (manager.getDestinations().remove(((Player) sender).getUniqueId()) != null)
            MessageUtil.sendMessage(manager, sender, MessageLevel.SUCCESS, "Navigation stopped.");
        else
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You did not have a navigation set.");
        return null;
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
