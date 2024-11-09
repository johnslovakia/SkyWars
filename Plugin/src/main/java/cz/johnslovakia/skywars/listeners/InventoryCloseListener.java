package cz.johnslovakia.skywars.listeners;

import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.skywars.chest.ChestManager;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import cz.johnslovakia.gameapi.users.GamePlayer;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) {
        Player player = (Player) e.getPlayer();
        GamePlayer gp = PlayerManager.getGamePlayer(player);
        Game game = gp.getPlayerData().getGame();

        if (game == null){
            return;
        }

        if (game.getState() != GameState.INGAME){
            return;
        }

        if (e.getInventory().getType() == InventoryType.CHEST){
            Chest chest = (Chest) gp.getMetadata().get("chest");
            if (chest == null){
                return;
            }
            if (e.getInventory().equals(chest.getInventory())){
                ChestManager.getChestManager(game).closeChest(chest);
                //ChestManager.closeChest(chest);
            }
        }
    }
}
