package de.craftlancer.core.command.newcmd;

import java.util.List;

import org.bukkit.command.CommandSender;

public interface CLCommand {
    public String getPermission();
    
    public void handleCommand(CommandSender sender, String[] args) throws CommandHandlerException;
    
    public List<String> handleTabComplete(CommandSender sender, String[] args) throws CommandHandlerException;
}
