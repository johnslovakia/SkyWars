package cz.johnslovakia.skywars.items;

import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.users.PlayerManager;
import cz.johnslovakia.gameapi.utils.*;
import cz.johnslovakia.skywars.SkyWars;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class GhostPearl implements Listener {

    public static AbilityItem getGhostPearlItem(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.ENDER_PEARL);
        itemBuilder.setCustomModelData(1010);


        return new AbilityItem.Builder("Ghost Pearl", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.ghost_pearl.lore")
                .addAction(AbilityItem.Action.DEFAULT, GhostPearl::execute)
                .build();
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (!e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)){
            return;
        }

        Player player = e.getPlayer();
        GamePlayer gamePlayer = PlayerManager.getGamePlayer(player);

        if (gamePlayer.getMetadata().get("ghost_pearl_used") != null && (boolean) gamePlayer.getMetadata().get("ghost_pearl_used")){
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 5 * 20, 1));
            new BukkitRunnable(){
                @Override
                public void run() {
                    gamePlayer.getMetadata().remove("ghost_pearl_used");
                }
            }.runTaskLater(SkyWars.getInstance(), 5 * 20L);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (!(e.getDamager() instanceof Player player)){
            return;
        }
        GamePlayer gamePlayer = PlayerManager.getGamePlayer(player);

        if (gamePlayer.getMetadata().get("ghost_pearl_used") != null && (boolean) gamePlayer.getMetadata().get("ghost_pearl_used")){
            if (player.hasPotionEffect(PotionEffectType.INVISIBILITY)){
                PotionEffect invisibilityEffect = player.getPotionEffect(PotionEffectType.INVISIBILITY);
                if (invisibilityEffect != null) {
                    double remainingSeconds = invisibilityEffect.getDuration() / 20.0;

                    if (remainingSeconds > 2){
                        player.removePotionEffect(PotionEffectType.INVISIBILITY);
                        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 30, invisibilityEffect.getAmplifier()));
                    }
                }
            }
        }
    }

    private static void execute(GamePlayer gamePlayer){
        gamePlayer.getMetadata().put("ghost_pearl_used", true);
    }

}
