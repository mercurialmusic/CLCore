package de.craftlancer.core.motd;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MOTDAddCommand extends SubCommand {
    
    private CLCore plugin;
    private MOTDManager manager;
    
    public MOTDAddCommand(CLCore plugin, MOTDManager manager) {
        super("clcore.admin", plugin, true);
        
        this.plugin = plugin;
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length > 1)
            return Collections.singletonList("<message>");
        
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You do not have access to this command.");
            return null;
        }
        
        if (args.length < 2) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You must enter a message.");
            return null;
        }
        
        String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length)).replace("%n%", "\n");
        
        manager.getMotdList().add(ChatColor.translateAlternateColorCodes('&', message));
        
        MessageUtil.sendMessage(manager, sender, MessageLevel.SUCCESS, "MOTD added successfully.");
        return null;
    }
    
    @Override
    public void help(CommandSender sender) {
        
    }
}
