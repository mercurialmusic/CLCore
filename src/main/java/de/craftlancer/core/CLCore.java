package de.craftlancer.core;

import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import de.craftlancer.core.conversation.ConvoCommand;
import de.craftlancer.core.items.CustomItemRegistry;
import de.craftlancer.core.legacy.MassChestInventory;
import de.craftlancer.core.structure.BlockStructure;
import de.craftlancer.core.structure.CuboidArea;
import de.craftlancer.core.structure.Point2D;
import de.craftlancer.core.structure.Point3D;
import de.craftlancer.core.structure.RectangleArea;

public class CLCore extends JavaPlugin {
    
    private static CLCore instance;
    
    private CustomItemRegistry itemRegistry;
    
    @Override
    public void onEnable() {
        instance = this;
        
        ConfigurationSerialization.registerClass(RectangleArea.class);
        ConfigurationSerialization.registerClass(CuboidArea.class);
        ConfigurationSerialization.registerClass(Point2D.class);
        ConfigurationSerialization.registerClass(Point3D.class);
        ConfigurationSerialization.registerClass(MassChestInventory.class);
        ConfigurationSerialization.registerClass(BlockStructure.class);
        
        getCommand("convo").setExecutor(new ConvoCommand());
        
        itemRegistry = new CustomItemRegistry(this);
    }
    
    @Override
    public void onDisable() {
        itemRegistry.save();
    }
    
    public CustomItemRegistry getItemRegistry() {
        return itemRegistry;
    }
    
    public static CLCore getInstance() {
        return instance;
    }
    
}
