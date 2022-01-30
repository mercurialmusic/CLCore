package de.craftlancer.core.util;

import de.craftlancer.core.Utils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemBuilder {
    
    private ItemStack item;
    private ItemMeta meta;
    
    public ItemBuilder(@Nonnull ItemStack item) {
        this.item = item.clone();
        this.meta = item.getItemMeta();
    }
    
    public ItemBuilder(@Nonnull Material material) {
        this(new ItemStack(material));
    }
    
    public ItemStack build() {
        item.setItemMeta(meta);
        
        return item;
    }
    
    public ItemBuilder setDisplayName(@Nullable String name) {
        meta.setDisplayName(name == null ? null : Utils.translateColorCodes(name));
        
        return this;
    }
    
    public ItemBuilder setType(@Nonnull Material material) {
        item.setType(material);
        
        return this;
    }
    
    public ItemBuilder setAmount(int amount) {
        if (amount < 1 || amount > item.getMaxStackSize())
            throw new IllegalArgumentException("You must specify a value larger than 0 and less than " + item.getMaxStackSize() + " (inclusive)");
        
        item.setAmount(amount);
        
        return this;
    }
    
    public ItemBuilder setAmountUnsafe(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemBuilder setLore(String... lore) {
        setLore(Arrays.asList(lore));
        
        return this;
    }
    
    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore.stream().map(Utils::translateColorCodes).collect(Collectors.toList()));
        
        return this;
    }
    
    public ItemBuilder setLore(int line, String message) {
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());
        
        lore.set(line, message);
        
        return setLore(lore);
    }
    
    public ItemBuilder insertLore(int line, String message) {
        List<String> lore = Optional.ofNullable(meta.getLore()).orElse(new ArrayList<>());
        
        lore.add(line, message);
        
        return setLore(lore);
    }
    
    
    public ItemBuilder addLore(String... lore) {
        return addLore(Arrays.asList(lore));
    }
    
    public ItemBuilder addLore(List<String> lore) {
        if (!meta.hasLore())
            meta.setLore(lore.stream().map(Utils::translateColorCodes).collect(Collectors.toList()));
        else {
            List<String> list = meta.getLore();
            list.addAll(lore.stream().map(Utils::translateColorCodes).collect(Collectors.toList()));
            meta.setLore(list);
        }
        
        return this;
    }
    
    public ItemBuilder setCustomModelData(int customModelData) {
        meta.setCustomModelData(customModelData);
        
        return this;
    }
    
    public ItemBuilder setUnbreakable(boolean isUnbreakable) {
        meta.setUnbreakable(isUnbreakable);
        
        return this;
    }
    
    public ItemBuilder addEnchant(@Nonnull Enchantment enchantment, int level, boolean allowUnsafe) {
        meta.addEnchant(enchantment, level, allowUnsafe);
        
        return this;
    }
    
    public ItemBuilder removeEnchantment(@Nonnull Enchantment enchantment) {
        meta.removeEnchant(enchantment);
        
        return this;
    }
    
    public ItemBuilder addItemFlag(@Nonnull ItemFlag itemFlag) {
        meta.addItemFlags(itemFlag);
        
        return this;
    }
    
    public ItemBuilder removeItemFlag(@Nonnull ItemFlag itemFlag) {
        meta.removeItemFlags(itemFlag);
        
        return this;
    }
    
    public ItemBuilder dye(Color color) {
        if (meta instanceof LeatherArmorMeta)
            ((LeatherArmorMeta) meta).setColor(color);
        
        return this;
    }
    
    public ItemBuilder addAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier attributeModifier) {
        meta.addAttributeModifier(attribute, attributeModifier);
        
        return this;
    }
    
    public ItemBuilder removeAttributeModifier(@Nonnull Attribute attribute) {
        meta.removeAttributeModifier(attribute);
        
        return this;
    }
    
    public ItemBuilder removeAttributeModifier(@Nonnull Attribute attribute, @Nonnull AttributeModifier attributeModifier) {
        meta.removeAttributeModifier(attribute, attributeModifier);
        
        return this;
    }
    
    /**
     * Makes the item glow - should only be used on creative items not meant for player use (or items not meant to be
     * enchanted), but can be used
     * on enchantable items.
     * <p>
     * When used to remove glow, all enchantments will be removed.
     */
    public ItemBuilder setEnchantmentGlow(boolean shouldGlow) {
        if (shouldGlow) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
            addItemFlag(ItemFlag.HIDE_ENCHANTS);
        } else {
            meta.getEnchants().forEach((enchant, level) -> meta.removeEnchant(enchant));
        }
        
        return this;
    }
    
    /**
     * Adds a NBT Tag to the item. Use item.getPersistentDataContainer().get(key) to get the value back.
     *
     * @param key The key to set the tag by. Will replace any current key with the same name.
     */
    public ItemBuilder addPersistentData(@Nonnull Plugin plugin, @Nonnull String key, @Nonnull String value) {
        return addPersistentData(plugin, key, PersistentDataType.STRING, value);
    }
    
    /**
     * Adds a NBT Tag to the item. Use item.getPersistentDataContainer().get(key) to get the value back.
     *
     * @param key The key to set the tag by. Will replace any current key with the same name.
     */
    public <T, Z> ItemBuilder addPersistentData(@Nonnull Plugin plugin, @Nonnull String key, @Nonnull PersistentDataType<T, Z> type, @Nonnull Z object) {
        NamespacedKey k = NamespacedKey.fromString(key, plugin);
        
        if (k == null)
            k = new NamespacedKey(plugin, key);
        
        return addPersistentData(k, type, object);
    }
    
    /**
     * Adds a NBT Tag to the item. Use item.getPersistentDataContainer().get(key) to get the value back.
     *
     * @param key The key to set the tag by. Will replace any current key with the same name.
     */
    public <T, Z> ItemBuilder addPersistentData(NamespacedKey key, PersistentDataType<T, Z> type, Z object) {
        meta.getPersistentDataContainer().set(key, type, object);
        
        return this;
    }
    
    /**
     * Removes the NBT Tag by the given key (if applicable)
     *
     * @throws IllegalArgumentException If there isn't a key by this name, check if the key exists before using this
     *                                  method.
     */
    public ItemBuilder removePersistentData(NamespacedKey key) {
        try {
            meta.getPersistentDataContainer().remove(key);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Error while trying to remove NBT tag: The item does not contain such key '" + key + "'!");
        }
        
        return this;
    }
    
    /**
     * Removes the NBT Tag by the given key (if applicable)
     *
     * @throws IllegalArgumentException If there isn't a key by this name, check if the key exists before using this
     *                                  method.
     */
    public ItemBuilder removePersistentData(@Nonnull Plugin plugin, @Nonnull String key) {
        NamespacedKey k = NamespacedKey.fromString(key, plugin);
        
        if (k == null)
            k = new NamespacedKey(plugin, key);
        
        return removePersistentData(k);
    }
    
    public ItemBuilder removeLore() {
        meta.setLore(new ArrayList<>());
        
        return this;
    }
    
    public ItemBuilder removeLastLoreLine() {
        if (!meta.hasLore())
            return this;
        
        List<String> lore = meta.getLore();
        lore.remove(lore.size() - 1);
        meta.setLore(lore);
        
        return this;
    }
}
