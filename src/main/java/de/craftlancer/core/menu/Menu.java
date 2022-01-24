package de.craftlancer.core.menu;

import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.util.Tuple;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class Menu implements InventoryHolder, Listener {
    
    //Used only in association with ConditionalMenu
    private String menuKey = "default";
    private final Map<Integer, MenuItem> items = new TreeMap<>();
    private final Inventory inventory;
    private Plugin plugin;
    
    public Menu(Plugin plugin, String title, int rows) {
        if (rows < 0 || rows > 6)
            throw new IllegalArgumentException("Number of rows must be 1 and 6 (inclusive)");
        
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        
        fill(new MenuItem(new ItemStack(Material.AIR)), false);
    }
    
    public Menu(Plugin plugin, String title, InventoryType type) {
        this.inventory = Bukkit.createInventory(this, type, title);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.plugin = plugin;
        
        fill(new MenuItem(new ItemStack(Material.AIR)), false);
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
    
    public void set(int slot, MenuItem item) {
        items.put(slot, item.deepClone());
        inventory.setItem(slot, item.getItem());
    }
    
    public void replace(int slot, ItemStack item) {
        items.get(slot).setItem(item);
        inventory.setItem(slot, item);
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
            if (i < 9 || i >= (inventory.getSize() - 9) || i % 9 == 0 || (i + 1) % 9 == 0)
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
    public void onClose(InventoryCloseEvent event) {
        if (!isInventoryEqual(event.getView().getTopInventory()))
            return;
        
        Player player = (Player) event.getPlayer();
        
        for (Map.Entry<Integer, MenuItem> e : items.entrySet())
            if (e.getValue().dropOnClose()) {
                ItemStack item = getInventory().getItem(e.getKey());
                
                if (item != null)
                    player.getInventory().addItem(item.clone())
                            .forEach((slot, i) -> player.getWorld().dropItemNaturally(player.getLocation(), i));
            }
    }
    
    @EventHandler
    public void onInteract(InventoryClickEvent event) {
        if (isInventoryEqual(event.getView().getTopInventory()))
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
        ItemStack cursor = event.getCursor();
        ItemStack current = event.getCurrentItem();
        boolean clickedBottomInventory = event.getClickedInventory() == event.getView().getBottomInventory();
        boolean clickedTopInventory = isInventoryEqual(event.getClickedInventory());
        
        Optional<Tuple<Integer, MenuItem>> placeToItem = clickedBottomInventory ? getFirstAvailablePlaceItem(event.getCurrentItem()) : Optional.empty();
        
        if (clickedBottomInventory) {
            if (!placeToItem.isPresent() && !allowBottomInventoryClick(action))
                event.setCancelled(true);
            else if (action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                ItemStack currentClone = current.clone();
                event.setCancelled(true);
                current.setAmount(0);
                new LambdaRunnable(() -> replace(placeToItem.get().getKey(), currentClone))
                        .runTaskLater(plugin, 1);
            }
        }
        
        if (!clickedTopInventory)
            return;
        
        MenuClick menuClick = new MenuClick((Player) event.getWhoClicked(), event.getAction(),
                menuKey, item, event.getCursor(), event.getSlot(), getInventory());
        
        if (allowPickupClick(item, action))
            Optional.ofNullable(item.getPickupHandler()).ifPresent(s -> s.accept(menuClick));
        else if (cursor != null && allowPlaceClick(item, action, cursor))
            Optional.ofNullable(item.getPlaceHandler()).ifPresent(s -> s.accept(menuClick));
        else
            event.setCancelled(true);
        
        new LambdaRunnable(() -> item.getClickActions().getOrDefault(event.getClick(),
                item.getClickActions().getOrDefault(null, click -> {
                })).accept(menuClick)).runTaskLater(plugin, 1);
    }
    
    private boolean allowPickupClick(MenuItem item, InventoryAction action) {
        if (!item.canPickup())
            return false;
        
        switch (action) {
            case PICKUP_ALL:
            case PICKUP_SOME:
            case PICKUP_ONE:
            case PICKUP_HALF:
            case MOVE_TO_OTHER_INVENTORY:
                return true;
            default:
                return false;
        }
    }
    
    private boolean allowPlaceClick(MenuItem item, InventoryAction action, ItemStack placed) {
        if (!item.canPlace())
            return false;
        
        if (item.getItemFilter() != null && !item.getItemFilter().test(placed))
            return false;
        
        switch (action) {
            case PLACE_ALL:
            case PLACE_ONE:
            case PLACE_SOME:
                return true;
            default:
                return false;
        }
    }
    
    private boolean allowBottomInventoryClick(InventoryAction action) {
        switch (action) {
            case MOVE_TO_OTHER_INVENTORY:
            case HOTBAR_MOVE_AND_READD:
            case COLLECT_TO_CURSOR:
                return false;
            default:
                return true;
        }
    }
    
    private Optional<Tuple<Integer, MenuItem>> getFirstAvailablePlaceItem(ItemStack item) {
        if (item == null)
            return Optional.empty();
        
        return items.entrySet().stream().filter(e -> {
                    MenuItem m = e.getValue();
                    ItemStack i = getInventory().getItem(e.getKey());
                    
                    return (i == null || i.getType().isAir())
                            && m.canPlace()
                            && (m.getItemFilter() == null || m.getItemFilter().test(item));
                })
                .map(e -> new Tuple<>(e.getKey(), e.getValue())).findFirst();
    }
    
    public MenuItem getMenuItem(int slot) {
        return items.get(slot);
    }
    
    public String getMenuKey() {
        return menuKey;
    }
}
