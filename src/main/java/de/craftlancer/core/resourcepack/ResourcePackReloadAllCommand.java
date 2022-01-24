package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ResourcePackReloadAllCommand extends SubCommand {
    
    private ResourcePackManager manager;
    
    public ResourcePackReloadAllCommand(CLCore plugin, ResourcePackManager manager) {
        super("clcore.admin", plugin, false);
        
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
        
        for (Player player : Bukkit.getOnlinePlayers())
            manager.sendResourcePack(player);
        
        return "§aAll players have been sent the resource pack.";
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
