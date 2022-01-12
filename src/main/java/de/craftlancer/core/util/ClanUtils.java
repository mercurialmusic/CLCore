package de.craftlancer.core.util;

import de.craftlancer.clapi.clclans.AbstractAlliance;
import de.craftlancer.clapi.clclans.AbstractClan;
import de.craftlancer.clapi.clclans.ClanRank;
import de.craftlancer.clapi.clclans.PluginCLChat;
import de.craftlancer.clapi.clclans.PluginClans;
import de.craftlancer.core.CLCore;
import de.craftlancer.core.Utils;
import de.craftlancer.core.resourcepack.emoji.EmojiFontRegistry;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ClanUtils {
    public static final List<String> COLOR_LIST = Arrays.asList(ChatColor.values()).stream().filter(ChatColor::isColor).filter(a -> a != ChatColor.BLACK)
            .map(ChatColor::name).collect(Collectors.toList());
    
    public static final int CLAN_NAME_LIMIT = 20;
    public static final int CLAN_TAG_LIMIT = 6;
    public static final int CLAN_DESC_LIMIT = 280;
    public static final int CLAN_MOTD_LIMIT = 280;
    public static final int ALLIANCE_TAG_LIMIT = 10;
    
    public static final long SCORE_CACHE_TIME = 1 * 60 * 60 * 1000L; // 1 hours
    
    private ClanUtils() {
    }
    
    public static ChatColor getColor(String name, boolean onlyColors) {
        for (ChatColor c : ChatColor.values())
            if ((c.isColor() || !onlyColors) && c.name().equals(name))
                return c;
        
        return null;
    }
    
    public static BaseComponent getClanNameComponent(AbstractClan clan) {
        TextComponent entry = new TextComponent(clan.getName());
        return applyClanFormatting(entry, clan);
    }
    
    public static BaseComponent getClanTagComponent(AbstractClan clan) {
        TextComponent entry = new TextComponent("[" + clan.getTag() + "]");
        return applyClanFormatting(entry, clan);
    }
    
    public static BaseComponent getClanTagAndNameComponent(AbstractClan clan) {
        TextComponent entry = new TextComponent("[" + clan.getTag() + "] - " + clan.getName());
        return applyClanFormatting(entry, clan);
    }
    
    private static BaseComponent applyClanFormatting(BaseComponent entry, AbstractClan clan) {
        entry.setColor(clan.getColor().asBungee());
        entry.setBold(true);
        entry.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/clan profile " + clan.getTag()));
        entry.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(new ComponentBuilder(clan.getTag() + " - " + clan.getName()).append("\nClick to view profile").create())));
        
        return entry;
    }
    
    
    public static BaseComponent getPlayerNameComponent(Player player) {
        return getPlayerNameComponent(player, Bukkit.getServicesManager().load(PluginClans.class).getClan(player));
    }
    
    public static BaseComponent getPlayerNameComponent(OfflinePlayer player, AbstractClan clan) {
        return getPlayerNameComponent(player, clan, false, false);
    }
    
    
    public static BaseComponent getPlayerNameComponent(OfflinePlayer player, boolean withPrefix, boolean withSuffix) {
        return getPlayerNameComponent(player, Bukkit.getServicesManager().load(PluginClans.class).getClan(player), withPrefix, withSuffix);
    }
    
    public static BaseComponent getPlayerNameComponent(OfflinePlayer player, AbstractClan clan, boolean withPrefix, boolean withSuffix) {
        /*
         * <Name> <ClanTag>
         * <ClanName> <Rank>
         * Click to /msg
         */
        
        ComponentBuilder b = new ComponentBuilder(player.getName());
        if (clan != null)
            b.append(" [").append(clan.getTag()).append("]\n").append(clan.getName()).append(" ")
                    .append(ClanUtils.getRankComponent(clan, clan.getMember(player).getRank()));
        b.append("\nClick to /msg");
        
        String displayName = player.getName();
        
        if ((withPrefix || withSuffix) && player.isOnline())
            displayName = player.getPlayer().getDisplayName();
        
        TextComponent entry = new TextComponent(TextComponent.fromLegacyText(displayName));
        entry.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(b.create())));
        entry.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/msg " + player.getName() + " "));
        
        return entry;
    }
    
    public static String getTitle(OfflinePlayer p) {
        // TODO
        return "";
    }
    
    public static BaseComponent getRankComponent(AbstractClan clan, ClanRank rank) {
        BaseComponent rankComp = new TextComponent(clan.getRankName(rank));
        rankComp.setColor(rank.getColor());
        rankComp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                new Text(new ComponentBuilder(rank.getDefaultName()).color(rank.getColor()).create())));
        
        return rankComp;
    }
    
    public static BaseComponent getAllianceComponent(AbstractAlliance alliance) {
        BaseComponent comp = new TextComponent(alliance.getId());
        BaseComponent creator = new TextComponent("Creator ");
        creator.addExtra(getClanTagComponent(alliance.getCreator()));
        BaseComponent members = new TextComponent("Members: ");
        alliance.getMembers().forEach(a -> {
            BaseComponent entry = ClanUtils.getClanTagComponent(a);
            members.addExtra(entry);
            members.addExtra(" ");
        });
        
        comp.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(new ComponentBuilder(creator).append("\n").append(members).create())));
        comp.setClickEvent(new ClickEvent(Action.RUN_COMMAND, "/clan alliance info " + alliance.getId()));
        return comp;
    }
    
    public static String toDate(long lastPlayed) {
        Date date = new Date(lastPlayed);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm z");
        return format.format(date);
    }
    
    public static String getLastSeen(OfflinePlayer p) {
        if (p.isOnline())
            return "§2Online§r";
        
        long timeDiff = System.currentTimeMillis() - CLCore.getInstance().getLastSeenCache().getLastSeen(p);
        
        if (timeDiff < Utils.MS_PER_HOUR)
            return ChatColor.GREEN.toString() + (timeDiff / Utils.MS_PER_MINUTE) + " minutes" + ChatColor.RESET;
        if (timeDiff < Utils.MS_PER_DAY)
            return ChatColor.YELLOW.toString() + (timeDiff / Utils.MS_PER_HOUR) + " hours" + ChatColor.RESET;
        else
            return (timeDiff / Utils.MS_PER_DAY) + " days";
    }
    
    public static Integer convertChatColorToHex(ChatColor color) {
        switch (color) {
            case DARK_RED:
                return 0x00AA0000;
            case RED:
                return 0x00FF5555;
            case GOLD:
                return 0x00FFAA00;
            case YELLOW:
                return 0x00FFFF55;
            case DARK_GREEN:
                return 0x0000AA00;
            case GREEN:
                return 0x0055FF55;
            case AQUA:
                return 0x0055FFFF;
            case DARK_AQUA:
                return 0x0000AAAA;
            case DARK_BLUE:
                return 0x000000AA;
            case BLUE:
                return 0x005555FF;
            case LIGHT_PURPLE:
                return 0x00FF55FF;
            case DARK_PURPLE:
                return 0x00AA00AA;
            case GRAY:
                return 0x00AAAAAA;
            case DARK_GRAY:
                return 0x00555555;
            case BLACK:
                return 0x00000000;
            case WHITE:
            default:
                return 0x00FFFFFF;
        }
    }
    
    public static BaseComponent composeChatMessage(Player sender, AbstractClan clan, String message, ChatColor messageColor, BaseComponent prefix,
                                                   boolean useRankColor) {
        BaseComponent player = getPlayerNameComponent(sender, clan, !useRankColor, true);
        if (useRankColor && clan != null)
            player.setColor(clan.getMember(sender).getRank().getColor());
        
        if (!sender.hasPermission("clcore.bypassunicodeblock"))
            message = message.replaceAll("\\p{C}", "");
        message = EmojiFontRegistry.getInstance().replace(message);
        
        if (message.isEmpty())
            message = "§r";
        
        BaseComponent base = new TextComponent();
        if (messageColor != null)
            base.setColor(messageColor.asBungee());
        base.addExtra(prefix);
        base.addExtra(player);
        base.addExtra(ChatColor.RESET.toString());
        base.addExtra(" ");
        base.addExtra(Bukkit.getServicesManager().load(PluginCLChat.class).applyMarkdown(sender, message));
        
        return base;
    }
}
