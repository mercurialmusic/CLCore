package de.craftlancer.core.clipboard;

import de.craftlancer.core.CLCore;
import de.craftlancer.core.LambdaRunnable;
import de.craftlancer.core.Utils;
import de.craftlancer.core.util.MessageLevel;
import de.craftlancer.core.util.MessageRegisterable;
import de.craftlancer.core.util.MessageUtil;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class ClipboardManager implements Listener, MessageRegisterable {
    private static ClipboardManager instance;
    private CLCore plugin;
    
    private List<UUID> cooldown = new ArrayList<>();
    private Map<UUID, Clipboard> clipboards = new HashMap<>();
    
    public ClipboardManager(CLCore plugin) {
        instance = this;
        
        this.plugin = plugin;
        
        BaseComponent component = new TextComponent(new ComponentBuilder("[").color(ChatColor.DARK_GRAY).append("Clipboard").color(ChatColor.WHITE)
                .append("]").color(ChatColor.DARK_GRAY).create());
        
        MessageUtil.register(this,
                component,
                ChatColor.WHITE, ChatColor.YELLOW, ChatColor.RED, ChatColor.DARK_RED, ChatColor.DARK_AQUA, ChatColor.GREEN);
    }
    
    public static ClipboardManager getInstance() {
        return instance;
    }
    
    public Optional<Clipboard> getClipboard(UUID uuid) {
        return clipboards.values().stream().filter(c -> c.getOwner().equals(uuid)).findFirst();
    }
    
    public void removeClipboard(UUID owner) {
        getClipboard(owner).ifPresent(Clipboard::remove);
        clipboards.remove(owner);
    }
    
    public void addClipboard(Clipboard clipboard) {
        
        clipboards.put(clipboard.getOwner(), clipboard);
        new LambdaRunnable(() -> {
            removeClipboard(clipboard.getOwner());
            Player player = Bukkit.getPlayer(clipboard.getOwner());
            if (player != null)
                MessageUtil.sendMessage(this, player, MessageLevel.INFO, "Clipboard expired.");
        }).runTaskLater(plugin, 6000);
    }
    
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        removeClipboard(event.getPlayer().getUniqueId());
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK)
            return;
        
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR && player.getInventory().getItemInMainHand().getType() != Material.GOLDEN_AXE)
            return;
        
        if (cooldown.contains(player.getUniqueId()))
            return;
        else {
            cooldown.add(player.getUniqueId());
            new LambdaRunnable(() -> cooldown.remove(player.getUniqueId())).runTaskLater(plugin, 5);
        }
        
        Block block = event.getClickedBlock();
        
        if (!clipboards.containsKey(player.getUniqueId()))
            if (player.getInventory().getItemInMainHand().getType() == Material.GOLDEN_AXE) {
                addClipboard(new Clipboard(player.getUniqueId(), player.getWorld()));
                MessageUtil.sendMessage(this, player, MessageLevel.SUCCESS, "Successfully created a new clipboard.");
            } else
                return;
        
        if (!player.isOp() && !Utils.isTrusted(player.getUniqueId(), block.getLocation(), ClaimPermission.Build)) {
            MessageUtil.sendMessage(this, player, MessageLevel.WARNING,
                    "You must be trusted in this claim to set your clipboard here.");
            return;
        }
        
        if (!player.isOp() && Utils.isInAdminRegion(block.getLocation())) {
            MessageUtil.sendMessage(this, player, MessageLevel.WARNING,
                    "You cannot set your clipboard in an admin claim.");
            return;
        }
        
        Optional<Clipboard> optional = getClipboard(player.getUniqueId());
        if (!optional.isPresent())
            return;
        
        Clipboard clipboard = optional.get();
        
        if (!block.getWorld().equals(clipboard.getWorld())) {
            MessageUtil.sendMessage(this, player, MessageLevel.ERROR,
                    "You must select a block in the same world you created the clipboard in.");
            return;
        }
        
        if (action == Action.LEFT_CLICK_BLOCK)
            clipboard.setLocation1(block.getLocation());
        else
            clipboard.setLocation2(block.getLocation());
        
        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 0.5F, 2F);
    }
    
    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR && player.getInventory().getItemInMainHand().getType() != Material.GOLDEN_AXE)
            return;
        
        if (player.getGameMode() == GameMode.CREATIVE && clipboards.containsKey(player.getUniqueId()))
            event.setCancelled(true);
    }
    
    @Override
    public String getMessageID() {
        return "Clipboard";
    }
}
