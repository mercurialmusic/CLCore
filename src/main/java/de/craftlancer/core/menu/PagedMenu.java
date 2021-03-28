package de.craftlancer.core.menu;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class PagedMenu extends AbstractPagedMenu {
    
    private List<Menu> inventories;
    private String title;
    
    /**
     * Creates a traversable GUI with the ability to go back in forth between pages.
     *
     * @param plugin     the owning plugin
     * @param title      the title of the inventory
     * @param useBorders whether to have a 1 block border around the list area
     * @param rows       the number of rows in the inventory, between 1 (2 with borders) and 6 (inclusive)
     * @param pageItems  list of items to display, can be modified later
     * @param playSounds whether to play sounds on certain actions
     */
    public PagedMenu(@Nonnull Plugin plugin, @Nullable String title, boolean useBorders, int rows, @Nonnull List<MenuItem> pageItems,
                     boolean playSounds) {
        super(plugin, rows, pageItems, useBorders, playSounds);
        
        this.title = title;
    }
    
    /**
     * Creates a traversable GUI with the ability to go back in forth between pages.
     *
     * @param plugin     the owning plugin
     * @param useBorders whether to have a 1 block border around the list area
     * @param rows       the number of rows in the inventory, between 1 (2 with borders) and 6 (inclusive)
     * @param pageItems  list of items to display, can be modified later
     * @param playSounds whether to play sounds on certain actions
     */
    public PagedMenu(Plugin plugin, boolean useBorders, int rows, List<MenuItem> pageItems, boolean playSounds) {
        this(plugin, null, useBorders, rows, pageItems, playSounds);
    }
    
    @Override
    protected void updateInventories() {
        inventories = new ArrayList<>();
        
        for (int page = 0; page <= getPageItems().size() / getItemsPerPage(); page++) {
            int finalPage = page;
            Menu inventory = title == null ? new Menu(getPlugin(), getRows()) : new Menu(getPlugin(), title, getRows());
            if (isUseBorders())
                inventory.fillBorders(getEmptyItem(), true);
            
            // Setting the forwards, backwards, and question mark items depending on the map size
            if (getPageItems().size() <= (page + 1) * getItemsPerPage())
                inventory.set(getNextPageSlot(), getEmptyItem());
            else
                inventory.set(getNextPageSlot(), getNextPageItem().clone().addClickAction(click -> display(click.getPlayer(), finalPage + 1)));
            
            if (page == 0)
                inventory.set(getPrevPageSlot(), getEmptyItem());
            else
                inventory.set(getPrevPageSlot(), getPrevPageItem().clone().addClickAction(click -> display(click.getPlayer(), finalPage - 1)));
            
            inventory.set(getInfoSlot(), getInfoItem());
            fillPageItems(inventory, page);
            getToolbarItems().forEach((slot, menuItem) -> inventory.set(getRows() * 9 + slot, menuItem));
            inventories.add(inventory);
        }
    }
    
    /**
     * Opens page 0 for given player
     *
     * @param player the player to open the inventory for
     */
    public void display(@Nonnull Player player) {
        display(player, 0);
    }
    
    /**
     * Opens a given page for a given player
     *
     * @param player
     * @param page
     */
    public void display(@Nonnull Player player, int page) {
        if (inventories == null)
            updateInventories();
        
        if (page < 0 || inventories.size() < page)
            throw new IllegalArgumentException("Given page is out of bounds.");
        
        if (isPlaySounds())
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2F);
        player.openInventory(inventories.get(page).getInventory());
    }
}
