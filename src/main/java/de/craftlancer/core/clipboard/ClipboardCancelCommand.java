package de.craftlancer.core.clipboard;

import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ClipboardCancelCommand extends SubCommand {
    
    private ClipboardManager manager;
    
    public ClipboardCancelCommand(Plugin plugin, ClipboardManager manager) {
        super("", plugin, false);
        
        this.manager = manager;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You do not have access to this command.");
            return null;
        }
        
        Player player = (Player) sender;
        
        if (!manager.getClipboard(player.getUniqueId()).isPresent()) {
            MessageUtil.sendMessage(manager, sender, MessageLevel.INFO, "You do not have an active clipboard.");
            return null;
        }
        
        manager.removeClipboard(player.getUniqueId());
        MessageUtil.sendMessage(manager, sender, MessageLevel.SUCCESS, "Successfully removed your active clipboard.");
        return null;
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
