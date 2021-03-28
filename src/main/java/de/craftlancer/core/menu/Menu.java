package de.craftlancer.core.menu;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;

public class Menu implements InventoryHolder, Listener {
    
    //Used only in association with ConditionalMenu
    private String menuKey = "default";
    private final MenuItem AIR = new MenuItem(new ItemStack(Material.AIR));
    private final Map<Integer, MenuItem> items = new HashMap<>();
    private final Inventory inventory;
    
    public Menu(Plugin plugin, String title, int rows) {
        if (rows < 0 || rows > 6)
            throw new IllegalArgumentException("Number of rows must be 1 and 6 (inclusive)");
        
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(AIR, false);
    }
    
    public Menu(Plugin plugin, String title, InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(AIR, false);
    }
    
    public Menu(Plugin plugin, String title) {
        this(plugin, title, 6);
    }
    
    protected void setMenuKey(String key) {
        this.menuKey = key;
    }
    
    public Menu(Plugin plugin, int rows) {
        if (rows < 0 || rows > 6)
            throw new IllegalArgumentException("Number of rows must be 1 and 6 (inclusive)");
        
        this.inventory = Bukkit.createInventory(this, rows * 9);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(AIR, false);
    }
    
    public Menu(Plugin plugin, InventoryType type) {
        
        this.inventory = Bukkit.createInventory(this, type);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(AIR, false);
    }
    
    public Menu(Plugin plugin) {
        this(plugin, 6);
    }
    
    public void set(int slot, MenuItem item) {
        items.put(slot, item);
        inventory.setItem(slot, item.getItem());
    }
    
    /**
     * Fills the inventory with a given menu item.
     *
     * @param replace whether or not to replace current items inside.
     */
    public void fill(MenuItem item, boolean replace) {
        for (int i = 0; i < inventory.getSize(); i++)
            if (replace || (!items.containsKey(i) || items.get(i).getItem().getType().isAir())) {
                items.put(i, item);
                inventory.setItem(i, item.getItem());
            }
    }
    
    public void fillBorders(MenuItem item, boolean replace) {
        for (int i = 0; i < inventory.getSize(); i++)
            if (i < 9 || i >= (inventory.getSize() - 1) * 9 || i % 9 == 0)
                if (replace || items.get(i).getItem().getType().isAir())
                    set(i, item);
    }
    
    public void remove(int slot) {
        items.put(slot, new MenuItem(new ItemStack(Material.AIR)));
    }
    
    @Override
    public @Nonnull
    Inventory getInventory() {
        return inventory;
    }
    
    public boolean isInventoryEqual(Inventory inventory) {
        return this.inventory == inventory;
    }
    
    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (isInventoryEqual(event.getClickedInventory())
                || isInventoryEqual(event.getView().getTopInventory())
                || isInventoryEqual(event.getInventory()))
            onInventoryInteract(event);
    }
    
    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (isInventoryEqual(event.getView().getTopInventory()) || isInventoryEqual(event.getInventory()))
            event.setCancelled(true);
    }
    
    public void onInventoryInteract(InventoryClickEvent event) {
        
        InventoryAction action = event.getAction();
        MenuItem item = items.get(event.getSlot());
        
        if (isInventoryEqual(event.getView().getTopInventory()))
            if (validateAction(event.getSlot(), item, action))
                event.setCancelled(true);
        if (!isInventoryEqual(event.getClickedInventory()))
            return;
        
        event.setCancelled(true);
        
        item.getClickActions().getOrDefault(event.getClick(),
                item.getClickActions().getOrDefault(null, click -> {
                })).accept(new MenuClick((Player) event.getWhoClicked(), event.getAction(), menuKey));
    }
    
    /**
     * @return true if event should be cancelled
     */
    public boolean validateAction(int slot, MenuItem item, InventoryAction action) {
        switch (action) {
            case COLLECT_TO_CURSOR:
            case MOVE_TO_OTHER_INVENTORY:
            case HOTBAR_MOVE_AND_READD:
            case HOTBAR_SWAP:
                return true;
            default:
                break;
        }
        
        return false;
    }
}
