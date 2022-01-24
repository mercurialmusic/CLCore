package de.craftlancer.core.menu;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

public class MenuTester implements CommandExecutor {
    
    public MenuTester(CLCore plugin) {
        plugin.getCommand("menutester").setExecutor(this);
    }
    
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String s, @Nonnull String[] args) {
        if (sender instanceof Player) {
            new MenuTest().display((Player) sender);
        }
        
        return true;
    }
    
    private static class MenuTest {
        
        private Menu menu;
        
        private MenuTest() {
            menu = new Menu(CLCore.getInstance(), "Test Menu", 6);
            
            MenuItem test2 = new MenuItem(new ItemBuilder(Material.EMERALD_BLOCK).build(), MenuItemFlag.PLACE, MenuItemFlag.PICKUP, MenuItemFlag.DROP_ON_CLOSE)
                    .withItemFilter(i -> i.getType() == Material.EMERALD_BLOCK);
            
            menu.set(0, new MenuItem(new ItemBuilder(Material.IRON_BLOCK).build()));
            menu.set(1, new MenuItem(new ItemBuilder(Material.DIAMOND_BLOCK).build(), MenuItemFlag.PICKUP));
            menu.set(21, test2);
            menu.set(22, test2);
            menu.set(23, test2);
            menu.set(30, test2);
            menu.set(31, test2);
            menu.set(32, test2);
            menu.set(39, test2);
            menu.set(40, test2);
            menu.set(41, test2);
        }
        
        private void display(Player player) {
            player.openInventory(menu.getInventory());
        }
    }
}
