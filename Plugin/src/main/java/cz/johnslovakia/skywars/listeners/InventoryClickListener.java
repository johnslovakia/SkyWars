package cz.johnslovakia.skywars.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (e.getCurrentItem() != null) {
            if (e.getCurrentItem().getType().equals(Material.POTION)) {
                if (e.getAction() == InventoryAction.SWAP_WITH_CURSOR) {
                    e.getCursor().setAmount(0);
                    e.getCurrentItem().setAmount(e.getCurrentItem().getAmount() + e.getCursor().getAmount());
                }
            }
        }
    }
}
