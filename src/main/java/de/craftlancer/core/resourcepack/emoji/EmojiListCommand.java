package de.craftlancer.core.resourcepack.emoji;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.Utils;
import de.craftlancer.core.command.SubCommand;
import de.craftlancer.core.resourcepack.TranslateSpaceFont;
import de.craftlancer.core.util.ItemBuilder;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.stream.Collectors;

public class EmojiListCommand extends SubCommand {
    
    private final EmojiFontRegistry registry;
    
    public EmojiListCommand(CLCore plugin, EmojiFontRegistry registry) {
        super("", plugin, false);
        
        this.registry = registry;
    }
    
    @Override
    protected String execute(CommandSender sender, Command cmd, String label, String[] args) {
        if (!checkSender(sender)) {
            MessageUtil.sendMessage(getPlugin(), sender, MessageLevel.INFO, "You do not have permission to that command.");
            return null;
        }
        
        sendPlayerEmojiList((Player) sender);
        
        return null;
    }
    
    protected void sendPlayerEmojiList(Player player) {
        Collection<EmojiFontRegistry.Emoji> emojis = registry.getEmojis();
        
        MessageUtil.sendMessage(getPlugin(), player, MessageLevel.SUCCESS, "Now showing all emojis...");
        
        ComponentBuilder builder = new ComponentBuilder();
        
        int count = 0;
        for (EmojiFontRegistry.Emoji emoji : emojis.stream().filter(e -> !e.isEndOfList()).collect(Collectors.toList())) {
            append(emoji, builder);
            count++;
            
            if (count % 10 == 0)
                builder.append("\n");
        }
        
        if (count % 10 != 0)
            builder.append("\n");
        
        for (EmojiFontRegistry.Emoji emoji : emojis.stream().filter(EmojiFontRegistry.Emoji::isEndOfList).collect(Collectors.toList())) {
            append(emoji, builder);
            builder.append("\n");
        }
        
        player.spigot().sendMessage(builder.create());
    }
    
    private void append(EmojiFontRegistry.Emoji emoji, ComponentBuilder builder) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.STONE).setDisplayName("&f&lAliases for &f" + emoji.getUnicode())
                .setLore(emoji.getAliases().stream().map(a -> "&7:" + a + ":").collect(Collectors.toList()));
        
        TextComponent component = Utils.getItemComponent(itemBuilder.build());
        component.setText(TranslateSpaceFont.TRANSLATE_POSITIVE_1 + emoji.getUnicode());
        
        builder.append(component);
    }
    
    @Override
    public void help(CommandSender sender) {
    
    }
}
