package cz.johnslovakia.skywars.api;

import org.bukkit.Location;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

public interface VersionSupport {

    public ItemStack getItemInMainHand(Player player);
    public void setItemInMainHand(Player player, ItemStack item);
    public void damagePlayer(Player player, double damage);
    public void infernoEffect(Player player);
    public Location getChestLocation(InventoryCloseEvent event);
    public void setChestFacingDirection(BlockFace blockFace, Location chestLocation);
}
