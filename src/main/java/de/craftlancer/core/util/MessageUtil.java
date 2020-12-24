package de.craftlancer.core.util;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class MessageUtil {
    private static final MessageSettings NULL_SETTINGS = new MessageSettings();
    private static final Map<String, MessageSettings> settings = new HashMap<>();
    
    private MessageUtil() {
    }
    
    @Deprecated
    public static void registerPlugin(Plugin plugin, BaseComponent prefix, ChatColor normalColor, ChatColor infoColor, ChatColor warningColor,
                                      ChatColor errorColor, ChatColor debugColor) {
        settings.put(plugin.getName(), new MessageSettings(prefix, normalColor, infoColor, warningColor, errorColor, debugColor));
    }
    
    public static void register(Plugin plugin, BaseComponent prefix, ChatColor normalColor, ChatColor infoColor, ChatColor warningColor,
                                ChatColor errorColor, ChatColor debugColor, ChatColor successColor) {
        settings.put(plugin.getName(), new MessageSettings(prefix, normalColor, infoColor, warningColor, errorColor, debugColor, successColor));
    }
    
    public static void register(MessageRegisterable registerable, BaseComponent prefix, ChatColor normalColor, ChatColor infoColor, ChatColor warningColor,
                                ChatColor errorColor, ChatColor debugColor, ChatColor successColor) {
        settings.put(registerable.getMessageID(), new MessageSettings(prefix, normalColor, infoColor, warningColor, errorColor, debugColor, successColor));
    }
    
    public static void sendMessage(Plugin plugin, CommandSender sender, MessageLevel level, String message) {
        sender.spigot().sendMessage(settings.getOrDefault(plugin != null ? plugin.getName() : "", NULL_SETTINGS).formatMessage(level, message));
    }
    
    public static void sendMessage(Plugin plugin, CommandSender sender, MessageLevel level, BaseComponent message) {
        sender.spigot().sendMessage(settings.getOrDefault(plugin != null ? plugin.getName() : "", NULL_SETTINGS).formatMessage(level, message));
    }
    
    public static void sendMessage(MessageRegisterable registerable, CommandSender sender, MessageLevel level, String message) {
        sender.spigot().sendMessage(settings.getOrDefault(registerable.getMessageID(), NULL_SETTINGS).formatMessage(level, message));
    }
    
    public static void sendMessage(MessageRegisterable registerable, CommandSender sender, MessageLevel level, BaseComponent message) {
        sender.spigot().sendMessage(settings.getOrDefault(registerable.getMessageID(), NULL_SETTINGS).formatMessage(level, message));
    }
    
    private static class MessageSettings {
        private BaseComponent prefix;
        private Map<MessageLevel, ChatColor> levelColors = new EnumMap<>(MessageLevel.class);
        
        MessageSettings() {
            this.prefix = new TextComponent();
        }
        
        public MessageSettings(BaseComponent prefix, ChatColor normalColor, ChatColor infoColor, ChatColor warningColor, ChatColor errorColor,
                               ChatColor debugColor) {
            this(prefix, normalColor, infoColor, warningColor, errorColor, debugColor, ChatColor.GREEN);
        }
        
        public MessageSettings(BaseComponent prefix, ChatColor normalColor, ChatColor infoColor, ChatColor warningColor, ChatColor errorColor,
                               ChatColor debugColor, ChatColor successColor) {
            this.prefix = prefix;
            levelColors.put(MessageLevel.NORMAL, normalColor);
            levelColors.put(MessageLevel.INFO, infoColor);
            levelColors.put(MessageLevel.WARNING, warningColor);
            levelColors.put(MessageLevel.ERROR, errorColor);
            levelColors.put(MessageLevel.DEBUG, debugColor);
            levelColors.put(MessageLevel.SUCCESS, successColor);
        }
        
        public BaseComponent formatMessage(MessageLevel level, String message) {
            return formatMessage(level, new TextComponent(message));
        }
        
        public BaseComponent formatMessage(MessageLevel level, BaseComponent message) {
            ChatColor levelColor = levelColors.get(level);
            
            BaseComponent base = new TextComponent();
            if (levelColor != null)
                base.setColor(levelColor);
            base.addExtra(prefix);
            base.addExtra(" ");
            base.addExtra(message);
            
            return base;
        }
    }
}
