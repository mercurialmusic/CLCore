package de.craftlancer.core;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import de.craftlancer.core.command.newcmd.NewCommandExecutor;
import de.craftlancer.core.command.newcmd.NewCommandHandler;

public class NewCommandTestHandler extends NewCommandHandler {
    
    public NewCommandTestHandler(Plugin plugin) {
        super(plugin);
        
        registerSubCommand("test1", new TestCommand1(), "testing1");
        registerSubCommand("test2", new TestCommand2());
        // TODO Auto-generated constructor stub
    }
    
    public class TestCommand1 extends NewCommandExecutor {
        public void performCommand(CommandSender sender, int value1, int value2, String value3) {
            sender.sendMessage(value1 + " " + value2 + " " + value3);
        }
    }
    
    public class TestCommand2 extends NewCommandExecutor {
        public void performCommand(Player sender, int value1, int value2, String value3) {
            sender.sendMessage(value1 + " " + value2 + " " + value3);
        }
    }
}
