package de.craftlancer.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.BoundingBox;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.HoverEvent.Action;

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
        }
        catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    public static double parseDoubleOrDefault(String val, double defaultVal) {
        try {
            return Double.parseDouble(val);
        }
        catch (NumberFormatException e) {
            return defaultVal;
        }
    }
    
    public static float parseFloatOrDefault(String val, float defaultVal) {
        try {
            return Float.parseFloat(val);
        }
        catch (NumberFormatException e) {
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
     * 
     * @param value the given String
     * @param list the Collection
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, Collection<String> list) {
        return list.stream().filter(a -> a.toLowerCase().startsWith(value.toLowerCase())).collect(Collectors.toList());
    }
    
    /**
     * Get all values of a string array which start with a given, case insensitive, string
     * 
     * @param value the given String
     * @param list the array
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
    
    public static boolean isBetween(int locX, int x, int x2) {
        return (x > x2 && locX >= x2 && locX <= x) || (x < x2 && locX <= x2 && locX >= x);
    }
    
    public static boolean isBetween(double d, double x, double x2) {
        return (x > x2 && d >= x2 && d <= x) || (x < x2 && d <= x2 && d >= x);
    }
    
    public static TextComponent getItemComponent(ItemStack item) {
        String displayName = item.getItemMeta().hasDisplayName() ? item.getItemMeta().getDisplayName() : item.getType().name();
        
        TextComponent component = new TextComponent(displayName);
        component.setHoverEvent(new HoverEvent(Action.SHOW_ITEM, new BaseComponent[] { NMSUtils.getItemHoverComponent(item) }));
        
        return component;
    }
    
    public static <T> List<T> paginate(Stream<T> values, long page) {
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
    
    public static boolean isChunkLoaded(World world, int chunkX, int chunkZ) {
        return world.isChunkLoaded(chunkX, chunkZ);
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
}
