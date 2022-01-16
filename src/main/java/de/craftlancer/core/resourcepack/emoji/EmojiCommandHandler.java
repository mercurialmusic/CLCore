package de.craftlancer.core.resourcepack.emoji;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.command.CommandHandler;

public class EmojiCommandHandler extends CommandHandler {
    public EmojiCommandHandler(CLCore plugin, EmojiFontRegistry registry) {
        super(plugin);
        
        registerSubCommand("add", new EmojiAddCommand(plugin, registry));
        registerSubCommand("remove", new EmojiRemoveCommand(plugin, registry));
        registerSubCommand("list", new EmojiListCommand(plugin, registry));
    }
}
