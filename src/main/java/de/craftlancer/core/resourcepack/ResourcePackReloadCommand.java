package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ResourcePackReloadCommand extends SubCommand {
    
    private ResourcePackManager manager;
    
    public ResourcePackReloadCommand(CLCore plugin, ResourcePackManager manager) {
        super("", plugin, false);
        
        this.manager = manager;
    }
    
    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Collections.emptyList();
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender))
            return "§cYou do not have access to this command.";
        
        manager.sendResourcePack((Player) sender);
        
        return "§aYou have been sent the resource pack.";
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
