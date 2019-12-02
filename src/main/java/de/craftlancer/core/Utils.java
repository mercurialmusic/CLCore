package de.craftlancer.core;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
    
    private Utils() { }
    
    public static <T> boolean arrayContains(T[] a, T o) {
        if (a != null && a.length != 0)
            for (T ob : a)
                if (ob.equals(o))
                    return true;
        
        return false;
    }
    
    /**
     * Get all values of a String Collection which start with a given String
     * 
     * @param value the given String
     * @param list the Collection
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, Collection<String> list) {
        return list.stream().filter(a -> a.startsWith(value)).collect(Collectors.toList());
    }
    
    /**
     * Get all values of a String array which start with a given String
     * 
     * @param value the given String
     * @param list the array
     * @return a List of all matches
     */
    public static List<String> getMatches(String value, String[] list) {
        return Arrays.stream(list).filter(a -> a.startsWith(value)).collect(Collectors.toList());
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
}
