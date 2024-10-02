package cz.johnslovakia.skywars.listeners;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.gameapi.utils.Utils;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.events.FireballLaunchEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class TNTandFireballListener implements Listener {

    @EventHandler
    public void onExplosionPrime(ExplosionPrimeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            e.setRadius(2.5F);
        }
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        if (e.getEntity() instanceof TNTPrimed) {
            e.setCancelled(true);
            for (Block block : e.blockList()) {
                block.setType(Material.AIR);
            }
            e.getLocation().getWorld().playSound(e.getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F);
        }else if (e.getEntity() instanceof Fireball){
            e.setCancelled(true);
            for (Block block : e.blockList()) {
                block.setType(Material.AIR);
            }
            e.getLocation().getWorld().playSound(e.getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F);
        }
    }

    @EventHandler
    public void onTNTDamagePlayer(EntityDamageByEntityEvent e) {
        if(e.getEntity() instanceof Player &&
                e.getDamager() instanceof TNTPrimed) {
            e.setCancelled(true);

            Player player = (Player) e.getEntity();
            //TNTPrimed tnt = (TNTPrimed) e.getDamager();
            Utils.damagePlayer(player, 8);
        }else if(e.getEntity() instanceof Player &&
                e.getDamager() instanceof Fireball) {
            e.setCancelled(true);

            Player player = (Player) e.getEntity();

            if (e.getEntity().equals(((Fireball) e.getDamager()).getShooter())) {
                Utils.damagePlayer(player, 3);
                Vector velocity = player.getVelocity().add(new Vector(0, 0.8, 0));
                player.setVelocity(velocity);
            }else{
                Utils.damagePlayer(player, 5);
            }
        }
    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block placedBlock = event.getBlock();
        Player player = event.getPlayer();

        if (placedBlock.getType() == Material.TNT) {
            placedBlock.setType(Material.AIR);
            TNTPrimed tnt = (TNTPrimed) event.getBlock().getWorld().spawn(event.getBlock().getLocation().add(0.5, 0, 0.5), TNTPrimed.class);
            tnt.setFuseTicks(60);
            player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        GamePlayer gp = PlayerManager.getGamePlayer(player);
        ItemStack eItem = e.getItem();

        if (gp.getPlayerData().getGame().getState() != GameState.INGAME) {
            return;
        }

        if (eItem != null) {
            if (eItem.getType().equals(XMaterial.FIRE_CHARGE.parseMaterial())) {
                Location eye = player.getEyeLocation();
                Location loc = eye.add(eye.getDirection().multiply(1.2));

                Fireball fireball = (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                fireball.setVelocity(loc.getDirection().normalize().multiply(2));
                fireball.setShooter(player);

                //fireball.setYield(1.0F);
                //fireball.setMetadata("Owner", (MetadataValue) new FixedMetadataValue(MicroBattle.getInstance(), player.getName()));

                FireballLaunchEvent ev = new FireballLaunchEvent(player, fireball);
                Bukkit.getPluginManager().callEvent(ev);


                int i = eItem.getAmount();
                if (i > 1) {
                    eItem.setAmount(i - 1);
                    e.setCancelled(true);
                    return;
                }

                player.getInventory().setItemInMainHand(null);
                e.setCancelled(true);
            }
        }
    }
}
