package de.craftlancer.core.legacy;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

/*
 * Config Format
 * 
 * MATERIAL:
 *   <amount>:
 *     - MATERIAL1 AMOUNT1 MATERIAL2 AMOUNT2
 * 
 * 
 */
// TODO update to 1.14
@Deprecated
public class CraftItYourself
{
    private static Map<Material, List<ExchangeRecipe>> exchange = new HashMap<Material, List<ExchangeRecipe>>();
    
    public static void load(FileConfiguration config)
    {
        for (String key : config.getKeys(false))
        {
            if (config.getConfigurationSection(key) == null)
                continue;
            
            Material result = Material.matchMaterial(key);
            
            if (result == null)
                continue;
            
            List<ExchangeRecipe> recipes = new ArrayList<ExchangeRecipe>();
            
            for (String key2 : config.getConfigurationSection(key).getKeys(false))
            {
                try
                {
                    int amount = Integer.parseInt(key2);
                    for (String value : config.getStringList(key + "." + key2))
                    {
                        String[] tmp = value.split(" ");
                        if (tmp.length % 2 != 0)
                            throw new RuntimeException("Error while loading ExchangeRecipies - malformatted recipe string!");
                        
                        List<ItemStack> contents = new ArrayList<ItemStack>();
                        
                        for (int i = 0; i + 1 < tmp.length; i += 2)
                        {
                            Material mat = Material.matchMaterial(tmp[i]);
                            int am = Integer.parseInt(tmp[i + 1]);
                            
                            contents.add(new ItemStack(mat, am));
                        }
                        
                        recipes.add(new ExchangeRecipe(new ItemStack(result, amount), contents));
                    }
                }
                catch (NumberFormatException e)
                {
                    continue;
                }
            }
            
            registerExchangeRecipes(result, recipes);
        }
        
    }
    
    public static void registerExchangeRecipes(Material result, List<ExchangeRecipe> recipes)
    {
        exchange.put(result, recipes);
    }
    
    public static Material cleanupTechnical(Material mat)
    {
        return mat;
        /*
        switch (mat)
        {
            case WOODEN_DOOR:
                return Material.WOOD_DOOR;
            case BED_BLOCK:
                return Material.BED;
            case REDSTONE_WIRE:
                return Material.REDSTONE;
            case CROPS:
                return Material.SEEDS;
            case WALL_SIGN:
            case SIGN_POST:
                return Material.SIGN;
            case IRON_DOOR_BLOCK:
                return Material.IRON_DOOR;
            case SUGAR_CANE_BLOCK:
                return Material.SUGAR_CANE;
            case CAKE_BLOCK:
                return Material.CAKE;
            case DIODE_BLOCK_OFF:
            case DIODE_BLOCK_ON:
                return Material.DIODE;
            case PUMPKIN_STEM:
                return Material.PUMPKIN_SEEDS;
            case MELON_STEM:
                return Material.MELON_SEEDS;
            case NETHER_WARTS:
                return Material.NETHER_STALK;
            case BREWING_STAND:
                return Material.BREWING_STAND_ITEM;
            case CAULDRON:
                return Material.CAULDRON_ITEM;
            case STATIONARY_WATER:
            case WATER:
            case STATIONARY_LAVA:
            case LAVA:
                return Material.AIR;
            case TRIPWIRE:
                return Material.STRING;
            case FLOWER_POT:
                return Material.FLOWER_POT_ITEM;
            case CARROT:
                return Material.CARROT_ITEM;
            case POTATO:
                return Material.POTATO_ITEM;
            case SKULL:
                return Material.SKULL_ITEM;
            case REDSTONE_COMPARATOR_ON:
            case REDSTONE_COMPARATOR_OFF:
                return Material.REDSTONE_COMPARATOR;
                
            case SOIL: // EXTRA
                return Material.DIRT;
            case REDSTONE_LAMP_ON:
                return Material.REDSTONE_LAMP_OFF;
            case BURNING_FURNACE:
                return Material.FURNACE;
            case GLOWING_REDSTONE_ORE:
                return Material.REDSTONE_ORE;
            case REDSTONE_TORCH_OFF:
                return Material.REDSTONE_TORCH_ON;
                
            default:
                return mat;
        }
        */
    }
    
    public static boolean ignoreDataAndMetaValues(ItemStack item)
    {
        switch (item.getType())
        {
            default:
                return true;
        }
    }
    
    public static boolean containsAtLeast(Inventory inventory, ItemStack item, int amount)
    {
        
        if (!ignoreDataAndMetaValues(item))
            return inventory.containsAtLeast(item, amount);
        else
        {
            if (item == null)
                return false;
            
            if (amount <= 0)
                return true;
            
            for (ItemStack i : inventory.getContents())
                if (i != null && item.getType() == i.getType() && (amount -= i.getAmount()) <= 0)
                    return true;
        }
        
        return false;
    }
    
    public static List<ItemStack> removeItem(Inventory inventory, Collection<ItemStack> items)
    {
        return removeItem(inventory, items.toArray(new ItemStack[0]));
    }
    
    public static List<ItemStack> removeItem(Inventory inventory, ItemStack... items)
    {
        Validate.notNull(items, "Items cannot be null");
        List<ItemStack> leftover = new ArrayList<ItemStack>();
        
        for (ItemStack item : items)
        {
            if (!ignoreDataAndMetaValues(item))
                inventory.removeItem(items);
            else
            {
                int toDelete = item.getAmount();
                
                while (true)
                {
                    int first = inventory.first(item.getType());
                    
                    if (first == -1)
                    {
                        item.setAmount(toDelete);
                        leftover.add(item);
                        break;
                    }
                    else
                    {
                        ItemStack itemStack = inventory.getItem(first);
                        int amount = itemStack.getAmount();
                        
                        if (amount <= toDelete)
                        {
                            toDelete -= amount;
                            inventory.clear(first);
                        }
                        else
                        {
                            itemStack.setAmount(amount - toDelete);
                            inventory.setItem(first, itemStack);
                            toDelete = 0;
                        }
                    }
                    
                    if (toDelete <= 0)
                        break;
                }
            }
        }
        
        return leftover;
    }
    
    public static ExchangeRecipe isCraftable(Inventory inv, ItemStack item)
    {
        return isCraftable(inv, item, new HashSet<Material>());
    }
    
    private static ExchangeRecipe isCraftable(Inventory inv, ItemStack item, Set<Material> ignore)
    {
        return isCraftable(inv, item, ignore, true, true);
    }
    
    // TODO extend system to support Material and durability/data - ItemMeta CAN'T be supported easily with vanilla recipes
    // TODO add ItemMeta support
    private static ExchangeRecipe isCraftable(Inventory inv, ItemStack item, Set<Material> ignore, boolean useVanillaRecipes, boolean useFurnaceRecipes)
    {
        if (ignore.contains(item.getType()))
            return null;
        
        ignore.add(item.getType());
        
        for (ExchangeRecipe recipe : getApplicableRecipes(item, useVanillaRecipes, useFurnaceRecipes))
        {
            int rounds = (int) Math.ceil((double) item.getAmount() / recipe.getResult().getAmount());
            
            boolean fail = false;
            for (ItemStack i : recipe.getItems())
            {
                if (ignore.contains(i.getType()))
                {
                    fail = true;
                    break;
                }
                
                if (!containsAtLeast(inv, i, i.getAmount() * rounds))
                    if (isCraftable(inv, item, ignore) == null)
                    {
                        fail = true;
                        break;
                    }
            }
            
            if (!fail)
                return recipe;
        }
        
        return null;
    }
    
    private static List<ExchangeRecipe> getApplicableRecipes(ItemStack item, boolean useVanillaRecipes, boolean useFurnaceRecipes)
    {
        List<ExchangeRecipe> list = new ArrayList<ExchangeRecipe>();
        
        if (exchange.containsKey(item.getType()))
            list.addAll(exchange.get(item.getType()));
        
        if (useVanillaRecipes)
            for (Recipe recipe : Bukkit.getRecipesFor(item))
            {
                ItemStack result = recipe.getResult();
                ItemStack[] input;
                
                if (recipe instanceof ShapedRecipe)
                {
                    List<ItemStack> ingredient = new ArrayList<ItemStack>();
                    
                    Map<Character, ItemStack> map = ((ShapedRecipe) recipe).getIngredientMap();
                    for (String str : ((ShapedRecipe) recipe).getShape())
                        for (char c : str.toCharArray())
                        {
                            ItemStack localItem = map.get(c);
                            if (localItem != null)
                                ingredient.add(localItem.clone());
                        }
                    
                    list.add(new ExchangeRecipe(result, ingredient));
                }
                else if (useFurnaceRecipes && recipe instanceof FurnaceRecipe)
                {
                    input = new ItemStack[] { ((FurnaceRecipe) recipe).getInput() };
                    list.add(new ExchangeRecipe(result, input));
                }
                else if (recipe instanceof ShapelessRecipe)
                {
                    list.add(new ExchangeRecipe(result, ((ShapelessRecipe) recipe).getIngredientList()));
                }
            }
        
        return list;
    }
    
    public static boolean removeItemFromInventory(Inventory inv, ItemStack item)
    {
        if (item.getType() == Material.AIR)
            return true;
        
        item.setType(cleanupTechnical(item.getType()));
        
        if (containsAtLeast(inv, item, item.getAmount()))
        {
            removeItem(inv, item);
            return true;
        }
        
        ExchangeRecipe recipe = isCraftable(inv, item);
        
        if (recipe == null)
            return false;
        
        craft(inv, recipe, item.getAmount());
        removeItem(inv, item);
        return true;
    }
    
    public static void craft(Inventory inv, ExchangeRecipe recipe, int amount)
    {
        int rounds = (int) Math.ceil((double) amount / recipe.getResult().getAmount());
        
        for (ItemStack i : recipe.getItems())
            for (int j = 0; j < rounds; j++)
                removeItem(inv, i);
        
        for (int j = 0; j < rounds; j++)
            inv.addItem(recipe.getResult());
    }
}

class ExchangeRecipe
{
    private Collection<ItemStack> items;
    private ItemStack result;
    
    public ExchangeRecipe(ItemStack result, ItemStack... items)
    {
        this(result, Arrays.asList(items));
    }
    
    public ExchangeRecipe(ItemStack result, Collection<ItemStack> items)
    {
        this.result = result;
        this.items = items;
    }
    
    public Collection<ItemStack> getItems()
    {
        return items;
    }
    
    public ItemStack getResult()
    {
        return result;
    }
    
}
