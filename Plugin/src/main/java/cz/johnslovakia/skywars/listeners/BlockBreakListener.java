package cz.johnslovakia.skywars.listeners;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.messages.MessageManager;
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

        if (e.getExpToDrop() != 0) {
            player.giveExp(e.getExpToDrop());
        }
        e.setExpToDrop(0);

        /*if (Util.isInvFull(player)) {
            if (block.getType().equals(Material.GOLD_ORE)
                    || block.getType().equals(Material.IRON_ORE)
                    || block.getType().equals(Material.DIAMOND_ORE)
                    || block.getType().equals(Material.LOG)
                    || block.getType().equals(Material.GRAVEL)) {
                player.sendMessage(Messages.translate(player, "chat.full_inventory"));
                e.setCancelled(true);
                return;
            }
        }*/




        if (block.getType().equals(Material.IRON_ORE)){
            e.setCancelled(true);
            block.setType(Material.AIR);

            Integer amount = Util.getRandom(1, 2);

            player.getInventory().addItem(new ItemStack(Material.IRON_INGOT, amount));
            //block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));

            //ItemStack item = ItemManager.getIronItem(player);
            //player.getInventory().addItem(item);
            //player.playSound(player.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 20.0F, 20.0F);
            player.giveExpLevels(1);
        }else if (block.getType().equals(Material.DIAMOND_ORE)){
            e.setCancelled(true);
            block.setType(Material.AIR);

            player.getInventory().addItem(new ItemStack(Material.DIAMOND));
            //block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND));

            //gp.getScoreByName("DiamondOre").increaseScore();
            //ItemStack item = ItemManager.getDiamondItem(player);
            //player.getInventory().addItem(item);
            //player.playSound(player.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 20.0F, 20.0F);
            player.giveExpLevels(1);

            /*if (!GameAPI.getQuestManager().getQuest("dailyquest4").isCompleted(player)) {
                GameAPI.getQuestManager().getQuest("dailyquest4").addProgress(player);
            }*/
        }else if (block.getType().equals(Material.GRAVEL)){
            e.setCancelled(true);
            block.setType(Material.AIR);

            player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            //block.getLocation().getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ARROW, 2));

            //player.getInventory().addItem(new ItemStack(Material.ARROW, 2));
            //player.playSound(player.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 20.0F, 20.0F);
            player.giveExpLevels(1);
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
