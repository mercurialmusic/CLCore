package de.craftlancer.core.newgui;

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
    
    private final Map<Integer, MenuItem> items = new HashMap<>();
    private final Inventory inventory;
    
    public Menu(Plugin plugin, String title, int rows) {
        if (rows < 0 || rows > 6)
            throw new IllegalArgumentException("Number of rows must be 1 and 6 (inclusive)");
        
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(new MenuItem(new ItemStack(Material.AIR)), false);
    }
    
    public Menu(Plugin plugin, String title, InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(new MenuItem(new ItemStack(Material.AIR)), false);
    }
    
    public Menu(Plugin plugin, String title) {
        this(plugin, title, 6);
    }
    
    public Menu(Plugin plugin, int rows) {
        if (rows < 0 || rows > 6)
            throw new IllegalArgumentException("Number of rows must be 1 and 6 (inclusive)");
        
        this.inventory = Bukkit.createInventory(this, rows * 9);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(new MenuItem(new ItemStack(Material.AIR)), false);
    }
    
    public Menu(Plugin plugin, InventoryType type) {
        
        this.inventory = Bukkit.createInventory(this, type);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        
        fill(new MenuItem(new ItemStack(Material.AIR)), false);
    }
    
    public Menu(Plugin plugin) {
        this(plugin, 6);
    }
    
    public void add(int slot, MenuItem item) {
        if (item.getMenuType() == MenuType.ALL || item.getMenuType() == this.getMenuType()) {
            items.put(slot, item);
            inventory.setItem(slot, item.getItem());
        }
    }
    
    /**
     * Fills the inventory with a given menu item.
     *
     * @param replace whether or not to replace current items inside.
     */
    public void fill(MenuItem item, boolean replace) {
        if (item.getMenuType() == MenuType.ALL || item.getMenuType() == this.getMenuType())
            for (int i = 0; i < inventory.getSize(); i++)
                if (replace || items.get(i).getItem().getType().isAir()) {
                    items.put(i, item);
                    inventory.setItem(i, item.getItem());
                }
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
        MenuItem item = getItem(event.getSlot(), event.getView().getTopInventory());
        
        if (isInventoryEqual(event.getView().getTopInventory()))
            event.setCancelled(validateAction(event.getSlot(), item, action));
        
        if (isInventoryEqual(event.getClickedInventory()))
            return;
        
        item.getClickActions().getOrDefault(event.getClick(),
                item.getClickActions().getOrDefault(null, (p, m) -> {
                })).accept((Player) event.getWhoClicked(), event.getAction());
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
    
    public MenuType getMenuType() {
        return MenuType.DEFAULT;
    }
    
    protected MenuItem getItem(int slot, Inventory inventory) {
        return items.get(slot);
    }
}
