package cz.johnslovakia.skywars.listeners;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.ItemStack;

public class EnchantingTable implements Listener {

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (e.getInventory() instanceof EnchantingInventory) {
            e.getInventory().setItem(1, new ItemBuilder(XMaterial.LAPIS_LAZULI.parseItem(), 64).toItemStack());
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() instanceof EnchantingInventory) {
            event.getInventory().setItem(1, null);
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        ItemStack dragged = event.getOldCursor();

        if (dragged.getType().equals(new ItemBuilder(XMaterial.LAPIS_LAZULI.parseItem(), 64).toItemStack().getType())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick2(InventoryClickEvent event) {
        if (event.getCursor() != null) {
            if (event.getCursor().getType().equals(new ItemBuilder(XMaterial.LAPIS_LAZULI.parseItem(), 64).toItemStack().getType())) {
                event.setCancelled(true);
            }
        }

        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType().equals(new ItemBuilder(XMaterial.LAPIS_LAZULI.parseItem(), 64).toItemStack().getType())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        Item item = e.getItemDrop();

        if (item.getItemStack().getType().equals(new ItemBuilder(XMaterial.LAPIS_LAZULI.parseItem(), 64).toItemStack().getType())){
            e.getItemDrop().remove();
        }
    }

    @EventHandler
    public void onEnchantItem(EnchantItemEvent e) {
        PlayerManager.getGamePlayer(e.getEnchanter()).getScoreByName("EnchantItem").increaseScore();
    }
}
