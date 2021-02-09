package de.craftlancer.core;

import de.craftlancer.core.clipboard.ClipboardCommandHandler;
import de.craftlancer.core.clipboard.ClipboardManager;
import de.craftlancer.core.conversation.ConvoCommand;
import de.craftlancer.core.items.CustomItemRegistry;
import de.craftlancer.core.legacy.MassChestInventory;
import de.craftlancer.core.structure.BlockStructure;
import de.craftlancer.core.structure.CuboidArea;
import de.craftlancer.core.structure.Point2D;
import de.craftlancer.core.structure.Point3D;
import de.craftlancer.core.structure.RectangleArea;
import de.craftlancer.core.util.MessageUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;

import java.io.File;

public class CLCore extends JavaPlugin {
    
    private static CLCore instance;
    
    /* Vault */
    private Economy econ = null;
    private Permission perms = null;
    private Chat chat = null;
    
    private CustomItemRegistry itemRegistry;
    private LastSeenCache lastSeenCache;
    private PlayerTaskScheduler playerTaskScheduler;
    
    /* MockBukkit start */
    public CLCore() {
        super();
    }
    
    protected CLCore(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }
    /* MockBukkit end */
    
    @Override
    public void onEnable() {
        instance = this;
        
        ConfigurationSerialization.registerClass(RectangleArea.class);
        ConfigurationSerialization.registerClass(CuboidArea.class);
        ConfigurationSerialization.registerClass(Point2D.class);
        ConfigurationSerialization.registerClass(Point3D.class);
        ConfigurationSerialization.registerClass(MassChestInventory.class);
        ConfigurationSerialization.registerClass(BlockStructure.class);
        
        BaseComponent prefix = new TextComponent(new ComponentBuilder("[").color(ChatColor.WHITE).append("Craft").color(ChatColor.DARK_RED).append("Citizen")
                .color(ChatColor.WHITE).append("]").color(ChatColor.WHITE).create());
        MessageUtil.register(this, prefix, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.RED, ChatColor.DARK_RED, ChatColor.DARK_AQUA, ChatColor.GREEN);
        
        getCommand("convo").setExecutor(new ConvoCommand());
        
        ClipboardManager clipboardManager = new ClipboardManager(this);
        Bukkit.getPluginManager().registerEvents(clipboardManager, this);
        getCommand("clipboard").setExecutor(new ClipboardCommandHandler(this, clipboardManager));
        
        itemRegistry = new CustomItemRegistry(this);
        lastSeenCache = new LastSeenCache(this);
        playerTaskScheduler = new PlayerTaskScheduler(this);
        
        setupChat();
        setupEconomy();
        setupPermissions();
        
        //getCommand("newCommandTest").setExecutor(new NewCommandTestHandler(this));
        
        new LambdaRunnable(this::autosave).runTaskTimer(this, 18000L, 18000L);
    }
    
    @Override
    public void onDisable() {
        autosave();
    }
    
    private void autosave() {
        itemRegistry.save();
        lastSeenCache.save();
    }
    
    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
    
    private boolean setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        chat = rsp.getProvider();
        return chat != null;
    }
    
    private boolean setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        perms = rsp.getProvider();
        return perms != null;
    }
    
    public CustomItemRegistry getItemRegistry() {
        return itemRegistry;
    }
    
    public Economy getEconomy() {
        return econ;
    }
    
    public Permission getPermissions() {
        return perms;
    }
    
    public Chat getChat() {
        return chat;
    }
    
    public LastSeenCache getLastSeenCache() {
        return lastSeenCache;
    }
    
    public PlayerTaskScheduler getPlayerTaskScheduler() {
        return playerTaskScheduler;
    }
    
    public static CLCore getInstance() {
        return instance;
    }
    
}
