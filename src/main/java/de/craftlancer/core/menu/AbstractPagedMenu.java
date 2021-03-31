package de.craftlancer.core.menu;

import de.craftlancer.core.Utils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class AbstractPagedMenu<T> {
    
    private Consumer<T> inventoryUpdateHandler;
    private List<MenuItem> pageItems;
    private Map<Integer, MenuItem> toolbarItems = new HashMap<>();
    
    private final int nextPageSlot;
    private final int prevPageSlot;
    private final int infoSlot;
    
    // Play a sound when the gui is displayed?
    private boolean playSounds;
    // Use black glass to surround everything?
    private boolean useBorders;
    
    // amount of rows in the gui
    private final int rows;
    // amount of rows dedicated to page items
    private int rowsPerPage;
    // amount of slots per page dedicated to page items
    private int itemsPerPage;
    
    private final Plugin plugin;
    
    private MenuItem nextPageItem = new MenuItem(Utils.buildItemStack(Material.ARROW, ChatColor.GOLD + "Forwards", Collections.emptyList()));
    private MenuItem prevPageItem = new MenuItem(Utils.buildItemStack(Material.ARROW, ChatColor.GOLD + "Backwards", Collections.emptyList()));
    private MenuItem emptyItem = new MenuItem(Utils.buildItemStack(Material.BLACK_STAINED_GLASS_PANE, "", Collections.emptyList()));
    private MenuItem infoItem = new MenuItem(new ItemStack(Material.AIR));
    
    /**
     * Creates a traversable GUI with the ability to go back in forth between pages.
     *
     * @param plugin     the owning plugin
     * @param useBorders whether to have a 1 block border around the list area
     * @param rows       the number of rows in the inventory, between 1 (2 with borders) and 6 (inclusive)
     * @param pageItems  list of items to display, can be modified later
     * @param playSounds whether to play sounds on certain actions
     */
    public AbstractPagedMenu(@Nonnull Plugin plugin, int rows, @Nonnull List<MenuItem> pageItems, boolean useBorders,
                             boolean playSounds) {
        this.plugin = plugin;
        this.useBorders = useBorders;
        if (rows <= (useBorders ? 2 : 1) || rows > 6)
            throw new IllegalArgumentException("Number of rows must be between " + (useBorders ? 2 : 1) + " and 6 (inclusive)");
        
        this.playSounds = playSounds;
        this.pageItems = pageItems;
        this.rows = rows;
        this.rowsPerPage = useBorders ? rows - 2 : rows - 1;
        this.itemsPerPage = useBorders ? rowsPerPage * 7 : rowsPerPage * 9;
        this.nextPageSlot = (rows * 9) - 4;
        this.infoSlot = (rows * 9) - 5;
        this.prevPageSlot = (rows * 9) - 6;
    }
    
    protected abstract void updateInventories();
    
    /**
     * Given consumer will be called upon inventory update.
     *
     * @param inventoryUpdateHandler Actions to be taken on a Menu or ConditionalMenu upon inventory update.
     */
    public void setInventoryUpdateHandler(Consumer<T> inventoryUpdateHandler) {
        this.inventoryUpdateHandler = inventoryUpdateHandler;
    }
    
    protected Consumer<T> getInventoryUpdateHandler() {
        return inventoryUpdateHandler;
    }
    
    /**
     * Sets a page.
     *
     * @param inventory the inventory to modify
     * @param page      the page to set
     */
    protected void fillPageItems(Menu inventory, int page) {
        int slot = getFirstSlot();
        // Looping through all the items to set the item in inventory and apply runnables
        List<MenuItem> localItems = pageItems.stream().filter(a -> a.getItem().getType() != Material.AIR).skip((long) (page) * itemsPerPage).limit(itemsPerPage)
                .collect(Collectors.toList());
        
        for (MenuItem pageItem : localItems) {
            int finalSlot = slot;
            inventory.set(finalSlot, pageItem);
            
            if (slot % 9 == (useBorders ? 7 : 9)) {
                if (slot == getLastSlot())
                    break;
                else
                    slot += 3;
            } else
                slot++;
        }
    }
    
    protected int getFirstSlot() {
        return useBorders ? 10 : 0;
    }
    
    protected int getLastSlot() {
        return useBorders ? (rows * 9) - 11 : rowsPerPage * 9;
    }
    
    /**
     * Updates the inventories by recreating them.
     */
    public void reload() {
        updateInventories();
    }
    
    /**
     * Set the forwards arrow item
     * Reloads the inventory
     *
     * @param forwardsArrowItem the item to be used as the forwards arrow button
     */
    public void setNextPageItem(@Nonnull MenuItem forwardsArrowItem) {
        this.nextPageItem = forwardsArrowItem;
        reload();
    }
    
    /**
     * Sets the backwards arrow item
     * Reloads the inventory
     *
     * @param backwardsArrowItem the item to be used as the backwards arrow button
     */
    public void setPreviousPageItem(@Nonnull MenuItem backwardsArrowItem) {
        this.prevPageItem = backwardsArrowItem;
        reload();
    }
    
    /**
     * Get a <b>mutable</b> list of page items.
     *
     * @return a list of all page items
     */
    public List<MenuItem> getPageItems() {
        return pageItems;
    }
    
    /**
     * Sets the items to be put into the menu
     * Reloads the inventory
     */
    public void setPageItems(@Nonnull List<MenuItem> pageItems) {
        this.pageItems = pageItems;
        reload();
    }
    
    /**
     * Adds a page item
     * Reloads the inventory if changes occurred
     */
    public void addPageItem(@Nonnull MenuItem pageItem) {
        pageItems.add(pageItem);
        reload();
    }
    
    /**
     * Removes a page item
     * Reloads the inventory if changes occurred
     */
    public void removePageItem(@Nonnull MenuItem pageItem) {
        if (pageItems.remove(pageItem))
            reload();
    }
    
    /**
     * Add a toolbar item
     * Reloads the inventory if changes occurred.
     *
     * @param relativeSlot the slot to be placed at. Must be between 0 and 8 inclusive, and will automatically be placed
     *                     at the bottom of the inventory.
     */
    public void addToolbarItem(int relativeSlot, @Nonnull MenuItem toolbarItem) {
        if (relativeSlot < 0 || relativeSlot > 8)
            throw new IllegalArgumentException("Relative slot must be between 0 and 8 inclusive.");
        
        toolbarItems.put(relativeSlot, toolbarItem);
        reload();
    }
    
    /**
     * Removes a toolbar item
     * Reloads the inventory if changes occurred
     */
    public void removeToolbarItem(int relativeSlot) {
        if (toolbarItems.remove(relativeSlot) != null)
            reload();
    }
    
    public Map<Integer, MenuItem> getToolbarItems() {
        return toolbarItems;
    }
    
    public int getRows() {
        return rows;
    }
    
    /**
     * Get the info item
     */
    @Nonnull
    public MenuItem getInfoItem() {
        return infoItem;
    }
    
    /**
     * Sets the info item
     * Reloads the inventory
     */
    public void setInfoItem(@Nonnull MenuItem item) {
        this.infoItem = item;
        reload();
    }
    
    public boolean isPlaySounds() {
        return playSounds;
    }
    
    public int getRowsPerPage() {
        return rowsPerPage;
    }
    
    public int getInfoSlot() {
        return infoSlot;
    }
    
    public int getNextPageSlot() {
        return nextPageSlot;
    }
    
    public int getPrevPageSlot() {
        return prevPageSlot;
    }
    
    public MenuItem getPrevPageItem() {
        return prevPageItem;
    }
    
    public MenuItem getNextPageItem() {
        return nextPageItem;
    }
    
    public boolean isUseBorders() {
        return useBorders;
    }
    
    public int getItemsPerPage() {
        return itemsPerPage;
    }
    
    public MenuItem getEmptyItem() {
        return emptyItem;
    }
    
    public Plugin getPlugin() {
        return plugin;
    }
}
