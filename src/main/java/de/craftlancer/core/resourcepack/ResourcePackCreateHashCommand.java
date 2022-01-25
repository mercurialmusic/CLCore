package de.craftlancer.core.resourcepack;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;

public class ResourcePackCreateHashCommand extends SubCommand {
    
    private ResourcePackManager manager;
    
    public ResourcePackCreateHashCommand(CLCore plugin, ResourcePackManager manager) {
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
        
        return "§aUse this link to create a hash §2http://onlinemd5.com/§a. Then, upload the zipped resource pack directly from the download url provided, " +
                "then, using the sha1 hashing algorithm, copy and paste the generated hash and use /resourcepack hash <hash> command to set it.";
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
