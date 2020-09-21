package de.craftlancer.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import be.seeseemelk.mockbukkit.ServerMock;
import de.craftlancer.core.util.InventoryUtils;
import de.craftlancer.core.util.ItemBuilder;

public class InventoryUtilsTest {
    private ServerMock server;
    private Inventory inv;
    
    @Before
    public void setUp() {
        server = MockBukkit.mock();
        
        inv = server.createInventory(null, InventoryType.CHEST);
        inv.setItem(0, new ItemStack(Material.STONE, 64));
        inv.setItem(1, new ItemStack(Material.STONE, 64));
        inv.setItem(2, new ItemStack(Material.STONE, 64));
        inv.setItem(3, new ItemStack(Material.STONE, 64));
        inv.setItem(4, new ItemStack(Material.STONE, 64));
        inv.setItem(5, new ItemStack(Material.STONE, 64));
        inv.setItem(6, new ItemStack(Material.GRASS, 44));
        inv.setItem(7, new ItemStack(Material.DIAMOND, 14));
        inv.setItem(8, new ItemStack(Material.IRON_ORE, 34));
        inv.setItem(9, new ItemBuilder(Material.IRON_INGOT).setAmount(32).setCustomModelData(1).setDisplayName("Test").build());
    }
    
    @After
    public void tearDown() {
        MockBukkit.unmock();
    }
    
    @Test
    public void testContainsItemList() {
        assertThrows(NullPointerException.class, () -> InventoryUtils.containsAtLeast(null));
        assertThrows(NullPointerException.class, () -> InventoryUtils.containsAtLeast(inv, (ItemStack[]) null));
        assertTrue(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.STONE, 64)));
        assertTrue(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, 44)));
        assertTrue(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, -1)));
        assertTrue(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, -1), null));
        assertFalse(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, 45)));
        assertFalse(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, 45), new ItemStack(Material.STONE, 64)));
        assertFalse(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, 40), new ItemStack(Material.GRASS, 5)));
        assertTrue(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.GRASS, 40), new ItemStack(Material.GRASS, 4)));
        assertFalse(InventoryUtils.containsAtLeast(inv, new ItemStack(Material.IRON_INGOT, 1)));
        assertTrue(InventoryUtils.containsAtLeast(inv, new ItemBuilder(Material.IRON_INGOT).setAmount(1).setCustomModelData(1).setDisplayName("Test").build()));
        assertFalse(InventoryUtils.containsAtLeast(inv, new ItemBuilder(Material.IRON_INGOT).setAmount(33).setCustomModelData(1).setDisplayName("Test").build()));
    }
    
    @Test
    public void testContainsAtLeast() {
        assertThrows(NullPointerException.class, () -> InventoryUtils.containsAtLeast(null, Material.STONE, 1));
        assertThrows(NullPointerException.class, () -> InventoryUtils.containsAtLeast(null, null, 1));
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.STONE, 1));
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.STONE, 320));
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.GRASS, 40));
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 1));
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, -1));
        
        assertFalse(InventoryUtils.containsAtLeast(inv, Material.STONE, 640));
        assertFalse(InventoryUtils.containsAtLeast(inv, Material.GRASS, 50));
        assertFalse(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 100));
        assertFalse(InventoryUtils.containsAtLeast(inv, null, 100));
    }
    
    @Test
    public void testRemove() {
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 32));
        assertEquals(0, InventoryUtils.remove(inv, Material.IRON_INGOT, 10));
        assertFalse(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 32));
        assertFalse(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 23));
        assertTrue(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 22));
        assertEquals(2, InventoryUtils.remove(inv, Material.IRON_INGOT, 24));
        assertFalse(InventoryUtils.containsAtLeast(inv, Material.IRON_INGOT, 1));
    }
}
