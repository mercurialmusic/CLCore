package de.craftlancer.core;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bukkit.inventory.ItemStack;

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

    private Utils() {
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

    public static <T> List<T> paginate(Stream<T> clans, long page) {
        return clans.skip(page * Utils.ELEMENTS_PER_PAGE).limit(Utils.ELEMENTS_PER_PAGE).collect(Collectors.toList());
    }

    public static <T> List<T> paginate(Collection<T> clans, long page) {
        return paginate(clans.stream(), page);
    }
}
