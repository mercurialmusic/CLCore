package de.craftlancer.core;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.spigotmc.ActivationRange;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.DedicatedServer;

public class NMSUtils {
    private static final String NMS_VERSION = Bukkit.getServer().getClass().getPackage().getName().substring(23);
    
    private static Class<?> nbttagClass;
    private static Class<?> craftItemStackClass;
    private static Class<?> nmsItemStackClass;
    private static Method asNMSCopy;
    private static Method save;
    
    static {
        try {
            nbttagClass = Class.forName("net.minecraft.server." + NMS_VERSION + ".NBTTagCompound");
            nmsItemStackClass = Class.forName("net.minecraft.server." + NMS_VERSION + ".ItemStack");
            craftItemStackClass = Class.forName("org.bukkit.craftbukkit." + NMS_VERSION + ".inventory.CraftItemStack");
            asNMSCopy = craftItemStackClass.getMethod("asNMSCopy", ItemStack.class);
            save = nmsItemStackClass.getMethod("save", nbttagClass);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to initialize NMSUtils.", e);
        }
    }
    
    private NMSUtils() {
    }
    
    public static int getPing(Player player) {
        try {
            Method handle = player.getClass().getMethod("getHandle");
            Object nmsHandle = handle.invoke(player);
            Field pingField = nmsHandle.getClass().getField("ping");
            return pingField.getInt(nmsHandle);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException | SecurityException
                | NoSuchFieldException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying to get player ping.", e);
        }
        
        return -1;
    }
    
    public static BaseComponent getItemHoverComponent(ItemStack item) {
        try {
            Object nmsItemStack = asNMSCopy.invoke(null, item);
            Object nbttag = nbttagClass.newInstance();
            
            return new TextComponent(save.invoke(nmsItemStack, nbttag).toString());
        }
        catch (Exception e) {
            Bukkit.getLogger().log(Level.SEVERE, "Exception while trying an item's hover component.", e);
        }
        
        return new TextComponent("Error");
    }
    
    @SuppressWarnings("resource")
    public static int getServerTick() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().ak();
    }

    @SuppressWarnings("resource")
    public static boolean isRunning() {
        return ((CraftServer) Bukkit.getServer()).getHandle().getServer().isRunning();
    }
    
    @SuppressWarnings("resource")
    public static double[] getRecentTPS() {
        DedicatedServer server = ((CraftServer) Bukkit.getServer()).getHandle().getServer();
        try {
            return (double[]) server.getClass().getField("recentTps").get(server);
        }
        catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            Bukkit.getLogger().warning(e.getMessage());
            return new double[] { 20D, 20D, 20D };
        }
    }

    public static boolean isEntityActive(Entity e) {
        return ActivationRange.checkIfActive(((CraftEntity) e).getHandle());
    }
}
