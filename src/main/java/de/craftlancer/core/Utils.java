package de.craftlancer.core;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 * ItemStack: <Material> <Amount> <Data> <Name> <Lore>
 *
 * ItemStack:
 *   Material: material
 *   Data: Data
 *   Amount: amount
 *   Name:
 *   Enchants:
 *   Lore:
 *
 */
public class Utils {
    
    public static final int ELEMENTS_PER_PAGE = 10;
    
    public static final ChatColor TEXT_COLOR_UNIMPORTANT = ChatColor.GRAY;
    public static final ChatColor TEXT_COLOR_IMPORTANT = ChatColor.WHITE;
    public static final String INDENTATION = "  ";
    
    private static final Pattern pattern = Pattern.compile("(?<!\\\\)(#[a-fA-F0-9]{6})");
    
    public static final int MS_PER_MINUTE = 60 * 1000;
    public static final int MS_PER_HOUR = 60 * MS_PER_MINUTE;
    public static final int MS_PER_DAY = 24 * MS_PER_HOUR;
    
    public static final SemanticVersion MC_VERSION = SemanticVersion.of(Bukkit.getBukkitVersion().split("-")[0]);
    
    private Utils() {
    }
    
    public static SemanticVersion getMCVersion() {
        return MC_VERSION;
    }
    
    public static int parseIntegerOrDefault(String val, int defaultVal) {
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    public static double parseDoubleOrDefault(String val, double defaultVal) {
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    public static float parseFloatOrDefault(String val, float defaultVal) {
        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    public static <T> boolean arrayContains(T[] a, T o) {
        if (a != null && a.length != 0)
            for (T ob : a)
                if (ob.equals(o))
                    return true;
        
        return false;
    }
    
    /**
     * Get all values of a string Collection which start with a given, case insensitive, string.
     *
     * @param value the given String
     * @param list  the Collection
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, Collection<String> list) {
        return list.stream().filter(a -> a.toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList());
    }
    
    /**
     * Get all values of a string array which start with a given, case insensitive, string
     *
     * @param value the given String
     * @param list  the array
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, String[] list) {
        return Arrays.stream(list).filter(a -> a.toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList());
    }
    
    public static float clamp(float value, float min, float max) {
        return Math.max(Math.min(value, max), min);
    }
    
    public static double clamp(double value, double min, double max) {
        return Math.max(Math.min(value, max), min);
    }
    
    public static String ticksToTimeString(long ticks) {
        long h = (ticks / 72000);
        long min = (ticks / 1200) % 60;
        long s = (ticks / 20) % 60;
        
        return String.format("%dh %02dmin %02ds", h, min, s);
    }
    
    public static boolean isBetween(int value, int bound1, int bound2) {
        return (bound1 > bound2 && value >= bound2 && value <= bound1) || (bound1 < bound2 && value <= bound2 && value >= bound1);
    }
    
    public static boolean isBetween(double value, double bound1, double bound2) {
        return (bound1 > bound2 && value >= bound2 && value <= bound1) || (bound1 < bound2 && value <= bound2 && value >= bound1);
    }
    
    public static TextComponent getItemComponent(ItemStack item) {
        String displayName = item.hasItemMeta() && item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
        
        TextComponent component = new TextComponent(displayName);
        component.setHoverEvent(new HoverEvent(Action.SHOW_ITEM, new BaseComponent[]{NMSUtils.getItemHoverComponent(item)}));
        
        return component;
    }
    
    public static <T> List<T> paginate(Stream<T> values, long page) {
        if (page < 0)
            return Collections.emptyList();
        
        return values.skip(page * Utils.ELEMENTS_PER_PAGE).limit(Utils.ELEMENTS_PER_PAGE).collect(Collectors.toList());
    }
    
    public static <T> List<T> paginate(Collection<T> values, long page) {
        return paginate(values.stream(), page);
    }
    
    public static <T> List<T> paginate(T[] values, long page) {
        return paginate(Arrays.stream(values), page);
    }
    
    public static BoundingBox calculateBoundingBoxBlock(Collection<Block> blocks) {
        int minX = blocks.stream().map(Block::getX).min(Integer::compare).orElse(0);
        int maxX = blocks.stream().map(Block::getX).max(Integer::compare).orElse(0);
        int minY = blocks.stream().map(Block::getY).min(Integer::compare).orElse(0);
        int maxY = blocks.stream().map(Block::getY).max(Integer::compare).orElse(0);
        int minZ = blocks.stream().map(Block::getZ).min(Integer::compare).orElse(0);
        int maxZ = blocks.stream().map(Block::getZ).max(Integer::compare).orElse(0);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    public static BoundingBox calculateBoundingBoxLocation(Collection<Location> blocks) {
        int minX = blocks.stream().map(Location::getBlockX).min(Integer::compare).orElse(0);
        int maxX = blocks.stream().map(Location::getBlockX).max(Integer::compare).orElse(0);
        int minY = blocks.stream().map(Location::getBlockY).min(Integer::compare).orElse(0);
        int maxY = blocks.stream().map(Location::getBlockY).max(Integer::compare).orElse(0);
        int minZ = blocks.stream().map(Location::getBlockZ).min(Integer::compare).orElse(0);
        int maxZ = blocks.stream().map(Location::getBlockZ).max(Integer::compare).orElse(0);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }
    
    public static Collection<String> toString(Collection<? extends Object> input) {
        return input.stream().map(Object::toString).collect(Collectors.toList());
    }
    
    public static Collection<String> toString(Object[] values) {
        return Arrays.stream(values).map(Object::toString).collect(Collectors.toList());
    }
    
    public static boolean isChunkLoaded(Location loc) {
        return loc.getWorld().isChunkLoaded(loc.getBlockX() >> 4, loc.getBlockZ() >> 4);
    }
    
    public static ItemStack buildItemStack(Material type, String name, List<String> lore) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        
        if (name != null)
            meta.setDisplayName(name);
        if (!lore.isEmpty())
            meta.setLore(lore);
        
        item.setItemMeta(meta);
        return item;
    }
    
    public static boolean isInAdminRegion(Location loc) {
        if (!WorldGuardPlugin.inst().isEnabled())
            return false;
        
        RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
        RegionManager regions = container.get(BukkitAdapter.adapt(loc.getWorld()));
        
        if (WorldGuard.getInstance() != null && regions != null) {
            BlockVector3 position = BlockVector3.at(loc.getX(), loc.getY(), loc.getZ());
            ApplicableRegionSet set = regions.getApplicableRegions(position);
            return set.size() != 0;
        }
        return false;
    }
    
    public static boolean isTrusted(UUID uuid, Location loc, ClaimPermission permission) {
        if (!GriefPrevention.instance.isEnabled())
            return true;
        
        Claim claim = GriefPrevention.instance.dataStore.getClaimAt(loc, true, null);
        
        return claim == null
                || uuid.equals(claim.ownerID)
                || claim.hasExplicitPermission(uuid, permission);
    }
    
    public static Rotation getRotationFromYaw(float yaw) {
        double rotation = (yaw + 180) % 360.0F;
        if (rotation < 0.0D) {
            rotation += 360.0D;
        }
        if ((0.0D <= rotation) && (rotation < 22.5D)) {
            return Rotation.NONE;
        }
        if ((22.5D <= rotation) && (rotation < 67.5D)) {
            return Rotation.CLOCKWISE_45;
        }
        if ((67.5D <= rotation) && (rotation < 112.5D)) {
            return Rotation.CLOCKWISE;
        }
        if ((112.5D <= rotation) && (rotation < 157.5D)) {
            return Rotation.CLOCKWISE_135;
        }
        if ((157.5D <= rotation) && (rotation < 202.5D)) {
            return Rotation.FLIPPED;
        }
        if ((202.5D <= rotation) && (rotation < 247.5D)) {
            return Rotation.FLIPPED_45;
        }
        if ((247.5D <= rotation) && (rotation < 292.5D)) {
            return Rotation.COUNTER_CLOCKWISE;
        }
        if ((292.5D <= rotation) && (rotation < 337.5D)) {
            return Rotation.COUNTER_CLOCKWISE_45;
        }
        if ((337.5D <= rotation) && (rotation < 360.0D)) {
            return Rotation.NONE;
        }
        return Rotation.NONE;
    }
    
    public static Rotation getRotationFromBlockFace(BlockFace face) {
        switch (face) {
            case SOUTH:
            case SOUTH_SOUTH_EAST:
            case SOUTH_SOUTH_WEST:
                return Rotation.FLIPPED;
            case EAST:
            case EAST_NORTH_EAST:
            case EAST_SOUTH_EAST:
                return Rotation.CLOCKWISE;
            case WEST:
            case WEST_NORTH_WEST:
            case WEST_SOUTH_WEST:
                return Rotation.COUNTER_CLOCKWISE;
            case SOUTH_WEST:
                return Rotation.FLIPPED_45;
            case NORTH_EAST:
                return Rotation.CLOCKWISE_45;
            case NORTH_WEST:
                return Rotation.COUNTER_CLOCKWISE_45;
            case SOUTH_EAST:
                return Rotation.CLOCKWISE_135;
            default:
                return Rotation.NONE;
        }
    }
    
    public static Material getInstrumentMaterial(Instrument instrument) {
        switch (instrument) {
            case BANJO:
                return Material.HAY_BLOCK;
            case BASS_DRUM:
                return Material.GRAY_CONCRETE;
            case BASS_GUITAR:
                return Material.OAK_LOG;
            case BELL:
                return Material.GOLD_BLOCK;
            case BIT:
                return Material.EMERALD_BLOCK;
            case CHIME:
                return Material.PACKED_ICE;
            case COW_BELL:
                return Material.SOUL_SAND;
            case DIDGERIDOO:
                return Material.PUMPKIN;
            case FLUTE:
                return Material.BLUE_TERRACOTTA;
            case GUITAR:
                return Material.WHITE_WOOL;
            case IRON_XYLOPHONE:
                return Material.IRON_BLOCK;
            case PLING:
                return Material.GLOWSTONE;
            case SNARE_DRUM:
                return Material.SAND;
            case STICKS:
                return Material.GLASS;
            case XYLOPHONE:
                return Material.BONE_BLOCK;
            default:
                return Material.BARRIER;
        }
    }
    
    public static String translateColorCodes(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);
        Matcher matcher = pattern.matcher(message);
        
        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + ChatColor.of(color));
        }
        
        return message;
    }
}
