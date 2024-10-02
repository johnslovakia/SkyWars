package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class BlockPlaceListener implements Listener {

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        ItemStack item = e.getItemInHand();

        GamePlayer gp = PlayerManager.getGamePlayer(player);

        if (gp.getPlayerData().getGame().getState() != GameState.INGAME){
            return;
        }

        /*Location location = DataHandler.getMapsConfig().getLocation("maps." + gp.getGame().getPlayingArena().getID() + ".throwableTNT");
        if (location != null) {
            /*double radius = 3D;
            if (location.distanceSquared(block.getLocation()) <= radius * radius) {
                Messages.send(player, "chat.you_cant_place");
                e.setCancelled(true);
                return;
            }*

            if (Util.isInRadius(block.getLocation(), location, 3)){
                MessageManager.get(player, "chat.you_cant_place").send();
                e.setCancelled(true);
                return;
            }
        }*/

        /*if (block.getType().equals(Material.ICE)
                && item.getItemMeta().getDisplayName().equals("§bFreeze!")){
            e.setCancelled(true);
        }*/
    }
}
