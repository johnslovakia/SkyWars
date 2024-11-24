package cz.johnslovakia.skywars.listeners;

import com.cryptomorin.xseries.XMaterial;

import cz.johnslovakia.gameapi.game.GameState;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.gameapi.utils.Utils;
import cz.johnslovakia.skywars.events.FireballLaunchEvent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
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
            e.setRadius(2.3F);
        }
    }

    @EventHandler
    public void onBlockExplode(EntityExplodeEvent e) {
        Location location = e.getEntity().getLocation();

        if (e.getEntity() instanceof TNTPrimed) {
            e.setYield(0f);
            //e.setCancelled(true);
            for (Block block : e.blockList()) {
                block.setType(Material.AIR);
            }

            double radius = 4;
            for (Entity entity : location.getWorld().getNearbyEntities(location, radius, radius, radius)) {
                if (entity instanceof TNTPrimed nearbyTnt) {
                    Vector direction = nearbyTnt.getLocation().toVector().subtract(location.toVector());

                    if (direction.length() > 0) {
                        direction = direction.normalize();
                        double randomFactor = 1 + (Math.random() * 0.6 - 0.3);
                        nearbyTnt.setVelocity(direction.multiply(0.9 * randomFactor));
                    }
                }
            }

            //e.getLocation().getWorld().playSound(e.getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F);*/
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
        Entity entity = e.getEntity();
        Entity damager = e.getDamager();

        if (entity instanceof Player player) {
            /*if (damager instanceof TNTPrimed) {
                e.setCancelled(true);
                Utils.damagePlayer(player, 8);
            } else */
            if (damager instanceof Fireball fireball) {
                e.setCancelled(true);

                if (entity.equals(fireball.getShooter())) {
                    Utils.damagePlayer(player, 3);
                    Vector velocity = player.getVelocity().add(new Vector(0, 0.8, 0));
                    player.setVelocity(velocity);
                } else {
                    Utils.damagePlayer(player, 5);
                }
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

        if (gp.getPlayerData().getGame() == null){
            return;
        }

        if (gp.getPlayerData().getGame().getState() != GameState.INGAME) {
            return;
        }

        if (eItem != null) {
            if (eItem.getType().equals(XMaterial.FIRE_CHARGE.parseMaterial())) {
                e.setCancelled(true);

                Location eye = player.getEyeLocation();
                Location loc = eye.add(eye.getDirection().multiply(1.2));

                Fireball fireball = (Fireball) loc.getWorld().spawnEntity(loc, EntityType.FIREBALL);
                fireball.setVelocity(loc.getDirection().normalize().multiply(2));
                fireball.setShooter(player);

                //fireball.setYield(1.0F);
                //fireball.setMetadata("Owner", (MetadataValue) new FixedMetadataValue(MicroBattle.getInstance(), player.getName()));

                FireballLaunchEvent ev = new FireballLaunchEvent(player, fireball);
                Bukkit.getPluginManager().callEvent(ev);


                if (eItem.getAmount() > 1) {
                    eItem.setAmount(eItem.getAmount() - 1);
                } else {
                    player.getInventory().remove(eItem);
                }
            }
        }
    }
}
