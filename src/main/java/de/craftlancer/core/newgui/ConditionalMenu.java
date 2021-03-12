package de.craftlancer.core.newgui;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Primary use is to be used in accordance with a resource pack.
 * If a player is not using the resource pack, get the default
 * inventory, else, get the conditional inventory.
 */
public class ConditionalMenu extends Menu {

    private final Map<Integer, MenuItem> items = new HashMap<>();
    private final Inventory inventory;

    public ConditionalMenu(Plugin plugin, String title, String conditionalTitle, int rows) {
        super(plugin, title, rows);

        this.inventory = Bukkit.createInventory(this, rows * 9, conditionalTitle);
    }

    public ConditionalMenu(Plugin plugin, String title, String conditionalTitle, InventoryType type) {
        super(plugin, title, type);

        this.inventory = Bukkit.createInventory(this, type, conditionalTitle);
    }

    public ConditionalMenu(Plugin plugin, String title, String conditionalTitle) {
        this(plugin, title, conditionalTitle, 6);
    }

    public ConditionalMenu(Plugin plugin, int rows) {
        super(plugin, rows);

        this.inventory = Bukkit.createInventory(this, rows * 9);
    }

    public ConditionalMenu(Plugin plugin, InventoryType type) {
        super(plugin, type);

        this.inventory = Bukkit.createInventory(this, type);
    }

    public ConditionalMenu(Plugin plugin) {
        this(plugin, 6);
    }

    @Override
    public void add(int slot, MenuItem item) {
        super.add(slot, item);

        if (item.getMenuType() == MenuType.ALL || item.getMenuType() == this.getMenuType()) {
            items.put(slot, item);
            inventory.setItem(slot, item.getItem());
        }
    }

    @Override
    public void fill(MenuItem item, boolean replace) {
        super.fill(item, replace);

        if (item.getMenuType() == MenuType.ALL || item.getMenuType() == this.getMenuType())
            for (int i = 0; i < inventory.getSize(); i++)
                if (replace || items.get(i).getItem().getType().isAir()) {
                    items.put(i, item);
                    inventory.setItem(i, item.getItem());
                }
    }

    @Override
    public boolean isInventoryEqual(Inventory inventory) {
        return super.isInventoryEqual(inventory) && this.inventory == inventory;
    }

    @Override
    public MenuType getMenuType() {
        return MenuType.CONDITIONAL;
    }

    /**
     * @return the conditional inventory if true, the default inventory if false
     */
    public Inventory getInventory(boolean conditional) {
        return conditional ? this.inventory : getInventory();
    }

    @Override
    protected MenuItem getItem(int slot, Inventory inventory) {
        return this.inventory == inventory ? items.get(slot) : super.getItem(slot, inventory);
    }
}
