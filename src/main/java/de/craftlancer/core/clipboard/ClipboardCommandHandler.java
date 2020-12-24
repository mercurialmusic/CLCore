package de.craftlancer.core.clipboard;

import de.craftlancer.core.command.CommandHandler;
import org.bukkit.plugin.Plugin;

public class ClipboardCommandHandler extends CommandHandler {
    public ClipboardCommandHandler(Plugin plugin, ClipboardManager manager) {
        super(plugin);
        
        registerSubCommand("new", new ClipboardNewCommand(plugin, manager));
        registerSubCommand("cancel", new ClipboardCancelCommand(plugin, manager));
    }
}
