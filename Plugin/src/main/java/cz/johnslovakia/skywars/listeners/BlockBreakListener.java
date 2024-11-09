package cz.johnslovakia.skywars.listeners;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.inventory.ItemStack;

public class BlockBreakListener implements Listener {

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();

        GamePlayer gp = PlayerManager.getGamePlayer(player);

        if (gp.getPlayerData().getGame().getState() != GameState.INGAME){
            return;
        }
        e.setExpToDrop(0);


        if (block.getType().equals(Material.IRON_ORE)){
            e.setCancelled(true);
            block.setType(Material.AIR);

            int amount = Util.getRandom(1, 3);

            player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, amount));
            player.playSound(player.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 1F, 1F);
            player.giveExp(7 + (4 * amount));
        }else if (block.getType().equals(Material.DIAMOND_ORE)){
            e.setCancelled(true);
            block.setType(Material.AIR);

            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
            player.playSound(player.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 1F, 1F);
            player.giveExp(14);
        }else if (block.getType().equals(Material.GRAVEL)){
            e.setCancelled(true);
            block.setType(Material.AIR);

            player.getInventory().addItem(new ItemStack(Material.ARROW));
            player.playSound(player.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 1F, 1F);
            player.giveExp(8);
        }else if (block.getType().equals(Material.CHEST)){
            e.setCancelled(true);
            Chest chest = (Chest)e.getBlock().getState();

            for(ItemStack item : chest.getInventory().getContents()) {
                if (item != null) {
                    block.getWorld().dropItemNaturally(block.getLocation(), item);
                }
            }

            block.setType(Material.AIR);
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent e) {
        if (e.getBlock().getType().equals(Material.DRAGON_EGG)){
            e.setCancelled(true);
        }
    }
}
