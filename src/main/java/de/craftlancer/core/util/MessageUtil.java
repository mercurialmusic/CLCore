package de.craftlancer.core.util;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MessageUtil implements Listener {
    private static final MessageSettings NULL_SETTINGS = new MessageSettings();
    private static final Map<String, MessageSettings> settings = new HashMap<>();
    private static final Map<UUID, List<BossBarMessage>> bossBars = new HashMap<>();
    private static final Map<BossBarMessageRegistrable, Map<UUID, BukkitRunnable>> bossBarRunnables = new HashMap<>();
    
    public MessageUtil(CLCore plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    //Resend boss bars on login
    @EventHandler(ignoreCancelled = true)
    public void onPlayerLogin(PlayerJoinEvent event) {
        Optional.ofNullable(bossBars.get(event.getPlayer().getUniqueId())).ifPresent(l -> l.forEach(b -> b.initialize(event.getPlayer())));
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
    
    public static void register(MessageRegisterable registrable, BaseComponent prefix, ChatColor normalColor, ChatColor infoColor, ChatColor warningColor,
                                ChatColor errorColor, ChatColor debugColor, ChatColor successColor) {
        settings.put(registrable.getMessageID(), new MessageSettings(prefix, normalColor, infoColor, warningColor, errorColor, debugColor, successColor));
    }
    
    public static void register(MessageRegisterable registrable, BaseComponent prefix) {
        register(registrable, prefix, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.RED, ChatColor.DARK_RED, ChatColor.DARK_AQUA, ChatColor.GREEN);
    }
    
    public static void register(Plugin plugin, BaseComponent prefix) {
        register(plugin, prefix, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.RED, ChatColor.DARK_RED, ChatColor.DARK_AQUA, ChatColor.GREEN);
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
    
    /**
     * Sends a message in the form of a boss bar to a player using a green
     * boss bar, which is invisible in the texture pack.
     *
     * @param player      The player in which the message is sent
     * @param registrable The implementing class the boss bar is being sent from
     */
    public static void sendBossBarMessage(BossBarMessageRegistrable registrable, Player player, String message) {
        List<BossBarMessage> messageList = bossBars.compute(player.getUniqueId(),
                (uuid, list) -> list == null ? new ArrayList<>() : list);
        
        BossBarMessage bossBarMessage = messageList.stream().filter(b -> b.getRegistrable().equals(registrable))
                .findFirst()
                .orElseGet(() -> {
                    BossBarMessage barMessage = new BossBarMessage(registrable, player);
                    messageList.add(barMessage);
                    return barMessage;
                });
        
        bossBarMessage.setMessage(message);
    }
    
    /**
     * Sends a message in the form of a boss bar to a player using a green
     * boss bar, which is invisible in the texture pack.
     *
     * @param player      The player in which the message is sent
     * @param registrable The implementing class the boss bar is being sent from
     * @param ticks       The amount of ticks to stay on the screen
     */
    public static void sendBossBarMessage(BossBarMessageRegistrable registrable, Player player, String message, int ticks) {
        sendBossBarMessage(registrable, player, message);
        
        BukkitRunnable runnable = new LambdaRunnable(() -> clearBossBarMessage(player.getUniqueId(), registrable));
        bossBarRunnables.put(registrable, bossBarRunnables.compute(registrable, (r, map) -> {
            if (map == null)
                map = new HashMap<>();
            else
                Optional.ofNullable(map.get(player.getUniqueId())).ifPresent(BukkitRunnable::cancel);
            
            map.put(player.getUniqueId(), runnable);
            
            return map;
        }));
        runnable.runTaskLater(CLCore.getInstance(), ticks);
    }
    
    public static void clearBossBarMessage(UUID uuid, BossBarMessageRegistrable registrable) {
        Optional.ofNullable(bossBars.get(uuid)).ifPresent(l -> l.removeIf(b -> {
            if (!b.getRegistrable().equals(registrable))
                return false;
            
            b.clear();
            return true;
        }));
    }
    
    public static void clearAllBossBarMessages(UUID uuid) {
        Optional.ofNullable(bossBars.get(uuid)).ifPresent(list -> {
            list.forEach(BossBarMessage::clear);
            list.clear();
        });
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
            if (prefix != null) {
                base.addExtra(prefix);
                base.addExtra(" ");
            }
            base.addExtra(message);
            
            return base;
        }
    }
    
    private static class BossBarMessage {
        
        private final BossBarMessageRegistrable registrable;
        private final UUID owner;
        private BossBar bossBar;
        private String message;
        
        private BossBarMessage(BossBarMessageRegistrable registrable, Player player) {
            this.registrable = registrable;
            this.owner = player.getUniqueId();
            this.message = "";
            
            initialize(player);
        }
        
        private void initialize(Player player) {
            this.bossBar = Bukkit.getServer().createBossBar(registrable.getBossBarId(), BarColor.GREEN, BarStyle.SOLID);
            this.bossBar.addPlayer(player);
            
            setMessage(message);
        }
        
        private void setMessage(String message) {
            this.message = message;
            
            bossBar.setTitle(message);
        }
        
        private BossBar getBossBar() {
            return bossBar;
        }
        
        private BossBarMessageRegistrable getRegistrable() {
            return registrable;
        }
        
        private void clear() {
            Optional.ofNullable(Bukkit.getPlayer(owner)).ifPresent(bossBar::removePlayer);
        }
    }
}
