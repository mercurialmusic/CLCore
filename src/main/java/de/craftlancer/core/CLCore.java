package de.craftlancer.core;

import de.craftlancer.core.clipboard.ClipboardCommandHandler;
import de.craftlancer.core.clipboard.ClipboardManager;
import de.craftlancer.core.conversation.ConvoCommand;
import de.craftlancer.core.items.CustomItemRegistry;
import de.craftlancer.core.motd.MOTDManager;
import de.craftlancer.core.navigation.NavigationManager;
import de.craftlancer.core.resourcepack.ResourcePackCommandHandler;
import de.craftlancer.core.resourcepack.ResourcePackManager;
import de.craftlancer.core.structure.BlockStructure;
import de.craftlancer.core.structure.CuboidArea;
import de.craftlancer.core.structure.Point2D;
import de.craftlancer.core.structure.Point3D;
import de.craftlancer.core.structure.RectangleArea;
import de.craftlancer.core.util.MessageUtil;
import de.craftlancer.core.vault.DefaultChat;
import de.craftlancer.core.vault.DefaultEconomy;
import de.craftlancer.core.vault.DefaultPermission;
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
    private ResourcePackManager resourcePackManager;
    private MOTDManager motdManager;
    private NavigationManager navigationManager;
    
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
        ConfigurationSerialization.registerClass(BlockStructure.class);
        ConfigurationSerialization.registerClass(NavigationManager.NavigationGoal.class);
        
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
        
        resourcePackManager = new ResourcePackManager(this);
        Bukkit.getPluginManager().registerEvents(resourcePackManager, this);
        getCommand("resourcepack").setExecutor(new ResourcePackCommandHandler(this, resourcePackManager));
        
        motdManager = new MOTDManager(this);
        
        navigationManager = new NavigationManager(this);
        
        setupPermissions();
        setupChat();
        setupEconomy();
        
        // getCommand("newCommandTest").setExecutor(new NewCommandTestHandler(this));
        
        new LambdaRunnable(this::autosave).runTaskTimer(this, 18000L, 18000L);
    }
    
    @Override
    public void onDisable() {
        autosave();
    }
    
    public NavigationManager getNavigationManager() {
        return navigationManager;
    }
    
    private void autosave() {
        itemRegistry.save();
        lastSeenCache.save();
        resourcePackManager.save();
        motdManager.save();
        navigationManager.save();
    }
    
    private void setupEconomy() {
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        
        if (rsp == null) {
            getLogger().severe("Failed to find economy plugin, falling back to null-implementation!");
            this.econ = new DefaultEconomy();
        }
        else {
            econ = rsp.getProvider();
            getLogger().info(() -> String.format("Loaded economy plugin %s", econ.getName()));
        }
    }
    
    private void setupChat() {
        RegisteredServiceProvider<Chat> rsp = getServer().getServicesManager().getRegistration(Chat.class);
        
        if (rsp == null) {
            getLogger().severe("Failed to find chat plugin, falling back to null-implementation!");
            this.chat = new DefaultChat(getPermissions());
        }
        else {
            chat = rsp.getProvider();
            getLogger().info(() -> String.format("Loaded chat plugin %s", chat.getName()));
        }
    }
    
    private void setupPermissions() {
        RegisteredServiceProvider<Permission> rsp = getServer().getServicesManager().getRegistration(Permission.class);
        
        if (rsp == null) {
            getLogger().severe("Failed to find permission plugin, falling back to null-implementation!");
            this.perms = new DefaultPermission();
        }
        else {
            perms = rsp.getProvider();
            getLogger().info(() -> String.format("Loaded permission plugin %s", perms.getName()));
        }
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
