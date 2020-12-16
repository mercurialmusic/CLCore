package de.craftlancer.core.clipboard;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.Utils;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageUtil;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClipboardManager implements Listener {
    private static ClipboardManager instance;
    private CLCore plugin;
    
    private List<Player> cooldown = new ArrayList<>();
    private Map<Player, Clipboard> clipboards = new HashMap<>();
    
    public ClipboardManager(CLCore plugin) {
        instance = this;
        
        this.plugin = plugin;
    }
    
    public static ClipboardManager getInstance() {
        return instance;
    }
    
    public Clipboard getClipboard(Player player) {
        return clipboards.get(player);
    }
    
    public void removeClipboard(Player player) {
        clipboards.get(player).remove();
        clipboards.remove(player);
    }
    
    public void addClipboard(Player player, Clipboard clipboard) {
        clipboards.put(player, clipboard);
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        clipboards.remove(event.getPlayer());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK)
            return;
        
        if (cooldown.contains(player))
            return;
        else {
            cooldown.add(player);
            new LambdaRunnable(() -> cooldown.remove(player)).runTaskLater(plugin, 2);
        }
        
        if (!clipboards.containsKey(player))
            return;
        
        Clipboard clipboard = clipboards.get(player);
        Block block = event.getClickedBlock();
        
        if (!block.getWorld().equals(clipboard.getWorld())) {
            MessageUtil.sendMessage(plugin, player, MessageLevel.ERROR,
                    "You must select a block in the same world you created the clipboard in.");
            return;
        }
        
        if (!player.isOp() && !Utils.isTrusted(player, block.getLocation())) {
            MessageUtil.sendMessage(plugin, player, MessageLevel.ERROR,
                    "You must be trusted in this claim to set your clipboard here.");
            return;
        }
        
        if (!player.isOp() && !Utils.isInAdminRegion(player, block.getLocation())) {
            MessageUtil.sendMessage(plugin, player, MessageLevel.ERROR,
                    "You cannot set your clipboard in an admin claim.");
            return;
        }
        
        if (action == Action.LEFT_CLICK_BLOCK)
            clipboard.setlocation1(block.getLocation());
        else
            clipboard.setlocation2(block.getLocation());
        
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2F);
        MessageUtil.sendMessage(plugin, player, MessageLevel.SUCCESS,
                "Location " + (action == Action.LEFT_CLICK_BLOCK ? "1" : "2") + " set.");
    }
}
