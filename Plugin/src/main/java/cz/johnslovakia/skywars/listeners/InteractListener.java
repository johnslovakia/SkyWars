package cz.johnslovakia.skywars.listeners;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.game.Game;
import cz.johnslovakia.gameapi.users.PlayerData;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.skywars.chest.ChestManager;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class InteractListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        GamePlayer gp = PlayerManager.getGamePlayer(player);
        PlayerData playerData = gp.getPlayerData();
        Game game = playerData.getGame();
        Block block = e.getClickedBlock();
        ItemStack eItem = e.getItem();

        if (game == null){
            return;
        }

        if (game.getState() != GameState.INGAME){
            return;
        }

        if (block != null){
            BlockState state = block.getState();
            if (state instanceof Chest) {
                Chest chest = (Chest) state;
                if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    gp.getMetadata().put("chest", chest);
                    if (!ChestManager.getChestManager(game).getOpened().contains(chest)){
                        ChestManager.getChestManager(game).addOpened(chest);
                    }
                }
            }
        }
    }
}
