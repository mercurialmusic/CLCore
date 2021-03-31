package de.craftlancer.core.menu;

import de.craftlancer.core.util.Tuple;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Primary use is to be used in accordance with a resource pack.
 * If a player is not using the resource pack, get the default
 * inventory, else, get the conditional inventory.
 */
public class ConditionalMenu {
    
    private final Map<String, Menu> menus = new HashMap<>();
    
    public ConditionalMenu(List<Tuple<String, Menu>> menus) {
        if (menus.size() == 0)
            throw new IllegalArgumentException("ConditionalMenu must be satisfied with at least one menu upon initialization.");
        for (Tuple<String, Menu> tuple : menus)
            this.menus.put(tuple.getKey(), tuple.getValue());
        
        setKeys();
    }
    
    public ConditionalMenu(Plugin plugin, int rows, List<Tuple<String, String>> menus) {
        if (menus.size() == 0)
            throw new IllegalArgumentException("ConditionalMenu must be satisfied with at least one menu upon initialization.");
        for (Tuple<String, String> menu : menus)
            if (menu.getValue() == null)
                this.menus.put(menu.getKey(), new Menu(plugin, rows));
            else
                this.menus.put(menu.getKey(), new Menu(plugin, menu.getValue(), rows));
        
        setKeys();
    }
    
    public ConditionalMenu(Plugin plugin, InventoryType type, List<Tuple<String, String>> menus) {
        if (menus.size() == 0)
            throw new IllegalArgumentException("ConditionalMenu must be satisfied with at least one menu upon initialization.");
        for (Tuple<String, String> menu : menus)
            if (menu.getValue() == null)
                this.menus.put(menu.getKey(), new Menu(plugin, type));
            else
                this.menus.put(menu.getKey(), new Menu(plugin, menu.getValue(), type));
        
        setKeys();
    }
    
    private void setKeys() {
        menus.forEach((key, menu) -> menu.setMenuKey(key));
    }
    
    public void fill(MenuItem item, boolean replace, String... keys) {
        if (keys.length == 0)
            menus.forEach((key, menu) -> menu.fill(item, replace));
        
        for (String key : keys)
            if (menus.containsKey(key))
                menus.get(key).fill(item, replace);
    }
    
    
    public void fillBorders(MenuItem item, boolean replace, String... keys) {
        if (keys.length == 0)
            menus.forEach((key, menu) -> menu.fillBorders(item, replace));
        
        for (String key : keys)
            if (menus.containsKey(key))
                menus.get(key).fillBorders(item, replace);
    }
    
    public void set(int slot, MenuItem item, String... keys) {
        if (keys.length == 0)
            menus.forEach((key, menu) -> menu.set(slot, item));
        
        for (String key : keys)
            if (menus.containsKey(key))
                menus.get(key).set(slot, item);
    }
    
    public void replace(int slot, ItemStack item, String... keys) {
        if (keys.length == 0)
            menus.forEach((key, menu) -> menu.replace(slot, item));
        
        for (String key : keys)
            if (menus.containsKey(key))
                menus.get(key).replace(slot, item);
    }
    
    public void remove(int slot, String... keys) {
        if (keys.length == 0)
            menus.forEach((key, menu) -> menu.remove(slot));
        
        for (String key : keys)
            if (menus.containsKey(key))
                menus.get(key).remove(slot);
    }
    
    public Menu getMenu(String key) {
        return menus.get(key);
    }
    
    
    /**
     * @return A map if menu key -> menu item for the item at the given slot
     */
    public Map<String, MenuItem> getMenuItem(int slot) {
        Map<String, MenuItem> map = new HashMap<>();
        
        menus.forEach((key, menu) -> map.put(key, menu.getMenuItem(slot)));
        
        return map;
    }
    
    public Map<String, Menu> getMenus() {
        return menus;
    }
}
