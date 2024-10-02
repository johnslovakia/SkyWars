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

    public static List<Player> freeze = new ArrayList<>();

    public HashMap<Player, Double> countdownInferno = new HashMap<>();
    public Map<Player, Double> countdownSoup = new HashMap<>();
    public Map<Player, Double> countdownFreeze = new HashMap<>();
    public Map<Player, Double> countdownRun = new HashMap<>();

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

        if (eItem != null) {

            /*if (eItem.getType().equals(XMaterial.FIRE_CHARGE.parseMaterial())){
                Location location1 = player.getEyeLocation();
                Location location2 = location1.add(location1.getDirection().multiply(1.2D));
                Fireball fireball = (Fireball)location2.getWorld().spawnEntity(location2, EntityType.FIREBALL);
                fireball.setVelocity(location2.getDirection().normalize().multiply(2));
                fireball.setYield(1.0F);
                fireball.setMetadata("Owner", (MetadataValue)new FixedMetadataValue(SkyWars.getInstance(), player.getName()));
                fireball.setShooter((ProjectileSource)player);

                CosmeticsCategory category = GameAPI.getCosmeticsManager().getCategoryByName("Projectile Trails");
                Util.trail(GameAPI.getCosmeticsManager().getSelectedCosmetic(category, player).getName(), fireball);

                int i = eItem.getAmount();
                if (i > 1) {
                    eItem.setAmount(i - 1);
                    e.setCancelled(true);
                    return;
                }

                player.getInventory().setItemInHand(null);
                e.setCancelled(true);
            }*/
            double infernoDelay = 20D;
            double freezeDelay = 20D;
            double runDelay = 10D;
            double soupDelay = 10D;

            //TODO: převést na GameAPI funkci
            /*if (eItem.getType().equals(Material.BLAZE_ROD)
                    && eItem.getItemMeta().getDisplayName().equals("§6Inferno Staff")) {
                e.setCancelled(true);
                double radius = 5D;
                if (player.getNearbyEntities(radius, radius, radius).stream().filter(en -> en instanceof Player).filter(en -> en != player).count() == 0) {
                    return;
                }

                if (countdownInferno.containsKey(player)) {
                    String roundedDouble = String.valueOf(Math.round(countdownInferno.get(player) * 100.0) / 100.0);
                    MessageManager.get(player, "chat.delay").replace("%delay%", roundedDouble).send();
                    return;
                } else {
                    countdownInferno.put(player, infernoDelay);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            countdownInferno.put(player, countdownInferno.get(player) - 0.1D);
                            if (SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getType().equals(Material.BLAZE_ROD)
                                    && SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getItemMeta().getDisplayName().equalsIgnoreCase("§6inferno staff")) {
                                GameUtil.countdownTimerBar(player, infernoDelay, countdownInferno.get(player));
                            }
                            if (countdownInferno.get(player) <= 0) {
                                countdownInferno.remove(player);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(SkyWars.getInstance(), 0L, 2L);
                }

                player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
                //MessageManager.get(player, "chat.special_item_used").replace("%special_item%", eItem.getItemMeta().getDisplayName()).send();
                if (SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getAmount() > 1) {
                    SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).setAmount(SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getAmount() - 1);
                } else {
                    SkyWars.getInstance().getVersionSupport().setItemInMainHand(player, new ItemStack(Material.AIR));
                }

                List<Entity> near = player.getNearbyEntities(radius, radius, radius);
                for (Entity entity : near) {
                    if (entity instanceof Player) {
                        Player nearPlayer = (Player) entity;
                        if (nearPlayer != player) {
                            nearPlayer.setFireTicks(100);
                            nearPlayer.damage(0.25D, player);
                        }
                    }
                }
            }

            if (eItem.getType().equals(Material.SUGAR)
                    && eItem.getItemMeta().getDisplayName().equals("§aRun!")) {
                e.setCancelled(true);

                if (countdownRun.containsKey(player)) {
                    String roundedDouble = String.valueOf(Math.round(countdownRun.get(player) * 100.0) / 100.0);
                    MessageManager.get(player, "chat.delay").replace("%delay%", roundedDouble).send();
                    return;
                } else {
                    countdownRun.put(player, runDelay);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            countdownRun.put(player, countdownRun.get(player) - 0.1D);
                            if (SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getType().equals(Material.SUGAR)
                                    && SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getItemMeta().getDisplayName().equalsIgnoreCase("§arun!")) {
                                GameUtil.countdownTimerBar(player, runDelay, countdownRun.get(player));
                            }
                            if (countdownRun.get(player) <= 0) {
                                countdownRun.remove(player);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(SkyWars.getInstance(), 0L, 2L);
                }

                player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
                //MessageManager.get(player, "chat.special_item_used").replace("%special_item%", eItem.getItemMeta().getDisplayName()).send();
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0));
                if (SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getAmount() > 1) {
                    SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).setAmount(SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getAmount() - 1);
                } else {
                    SkyWars.getInstance().getVersionSupport().setItemInMainHand(player, new ItemStack(Material.AIR));
                }
            }

            if (eItem.getType().equals(XMaterial.MUSHROOM_STEW.parseMaterial())) {
                e.setCancelled(true);

                if (countdownSoup.containsKey(player)) {
                    String roundedDouble = String.valueOf(Math.round(countdownSoup.get(player) * 100.0) / 100.0);
                    MessageManager.get(player, "chat.delay").replace("%delay%", roundedDouble).send();
                    return;
                } else {
                    countdownSoup.put(player, soupDelay);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            countdownSoup.put(player, countdownSoup.get(player) - 0.1D);
                            if (SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getType().equals(XMaterial.MUSHROOM_STEW.parseMaterial())) {
                                GameUtil.countdownTimerBar(player, soupDelay, countdownSoup.get(player));
                            }
                            if (countdownSoup.get(player) <= 0) {
                                countdownSoup.remove(player);
                                this.cancel();
                            }
                        }
                    }.runTaskTimer(SkyWars.getInstance(), 0L, 2L);
                }

                if (SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getAmount() > 1) {
                    SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).setAmount(SkyWars.getInstance().getVersionSupport().getItemInMainHand(player).getAmount() - 1);
                } else {
                    SkyWars.getInstance().getVersionSupport().setItemInMainHand(player, new ItemStack(Material.AIR));
                }
                player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
                player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 65, 1));
            }*/
        }
    }
}
