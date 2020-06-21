package de.craftlancer.core.gui;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class GUIInventory implements InventoryHolder, Listener {

    private final Inventory inventory;
    private Map<Integer, Runnable> clickActions = new HashMap<>();
    
    public GUIInventory(Plugin plugin, String title) {
        this.inventory = Bukkit.createInventory(this, 54, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    public GUIInventory(Plugin plugin) {
        this.inventory = Bukkit.createInventory(this, 54);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @Override
    public Inventory getInventory() {
        return inventory;
    }
    
    public void fill(ItemStack item) {
        for(int i = 0; i < inventory.getSize(); i++)
            inventory.setItem(i, item);
    }
    
    public void setItem(int slotId, ItemStack item) {
        if(slotId < 0 || slotId >= 54)
            return;
        
        inventory.setItem(slotId, item);
    }
    
    public void setClickAction(int slotId, @Nonnull Runnable action) {
        if(slotId < 0 || slotId >= 54)
            return;
        
        clickActions.put(slotId, action);
    }
    
    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if(event.getClickedInventory() != inventory)
            return;
        
        event.setCancelled(true);
        clickActions.getOrDefault(event.getSlot(), () -> {}).run();
    }
}
