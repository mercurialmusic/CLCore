package de.craftlancer.core;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ItemTag;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_18_R1.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.ActivationRange;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class NMSUtils {
    private static ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    
    private NMSUtils() {
    }
    
    /**
     * Implemented by Bukkit API
     */
    @Deprecated
    public static int getPing(Player player) {
        return player.getPing();
    }
    
    public static BaseComponent getItemHoverComponent(ItemStack item) {
        return new TextComponent(CraftItemStack.asNMSCopy(item).getTag().toString());
    }
    
    public static Item getItemHoverTag(ItemStack item) {
        ItemTag tag = ItemTag.ofNbt(CraftItemStack.asNMSCopy(item).getTag().toString());
        return new Item(item.getType().getKey().toString(), item.getAmount(), tag);
    }
    
    public static int getServerTick() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().getTickCount();
    }
    
    public static boolean isRunning() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning();
    }
    
    public static double[] getRecentTPS() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().recentTps;
    }
    
    public static CommandMap getCommandMap() {
        return ((CraftServer) Bukkit.getServer()).getCommandMap();
    }
    
    public static boolean isEntityActive(Entity e) {
        return ActivationRange.checkIfActive(((CraftEntity) e).getHandle());
    }
    
    /**
     * Sends an entity destroy packet to specified player.
     * <p>
     * This is clientside and does not affect the server, but it can have
     * unintended side effects, like passengers not displaying correctly,
     * to the player.
     */
    public static void destroyEntity(Player to, int entityIDs) {
        
        PacketContainer destroyContainer = protocolManager.createPacket(PacketType.Play.Server.ENTITY_DESTROY);
        destroyContainer.getIntLists().write(0, List.of(entityIDs));
        
        try {
            protocolManager.sendServerPacket(to, destroyContainer);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
