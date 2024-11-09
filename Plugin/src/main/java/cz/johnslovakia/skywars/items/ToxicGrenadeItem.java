package cz.johnslovakia.skywars.items;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.utils.*;
import cz.johnslovakia.skywars.SkyWars;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.awt.*;

public class ToxicGrenadeItem {

    private static final ItemBuilder itemBuilder = new ItemBuilder(Material.CHORUS_FRUIT).setCustomModelData(1012);

    public static AbilityItem getToxicGrenadeItem(){
        return new AbilityItem.Builder("Toxic Grenade", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.toxic_grenade.lore")
                .addAction(AbilityItem.Action.RIGHT_CLICK, ToxicGrenadeItem::execute)
                .addCooldown(AbilityItem.Action.RIGHT_CLICK, new Cooldown("Toxic Grenade", 2.5D))
                .setConsumable(true)
                .build();
    }

    private static void execute(GamePlayer gamePlayer){
        Player player = gamePlayer.getOnlinePlayer();

        spawnGrenade(player);
        player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
    }

    private static void spawnGrenade(Player player) {
        Location spawnLocation = player.getEyeLocation().add(player.getLocation().getDirection().multiply(2.3));

        Snowball grenade = player.getWorld().spawn(spawnLocation, Snowball.class);
        grenade.setShooter(player);
        grenade.setItem(itemBuilder.toItemStack());

        Vector direction = player.getLocation().getDirection().multiply(1.7);
        grenade.setVelocity(direction);

        new BukkitRunnable() {
            private int ticks = 0;

            @Override
            public void run() {
                if (ticks > 60 || grenade.isDead() || grenade.isOnGround()) {
                    XParticle.sphere(3, 15, ParticleDisplay.of(Particle.DUST).withColor(Color.GREEN, 0.75f).withLocation(grenade.getLocation().add(0.5, 0.3, 0.5)));
                    grenade.getLocation().getWorld().playSound(grenade.getLocation(), Sounds.EXPLODE.bukkitSound(), 1F, 1F);
                    grenade.getLocation().getWorld().playSound(grenade.getLocation(), "custom:explosion", 0.3F, 0.3F);

                    for (Entity entity : grenade.getNearbyEntities(3, 3, 3)) {
                        if (entity instanceof Player nearbyPlayer) {
                            nearbyPlayer.addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 0));
                        }
                    }

                    grenade.remove();
                    cancel();
                    return;
                }

                grenade.getWorld().spawnParticle(Particle.SMOKE, grenade.getLocation(), 1, 0.1, 0.1, 0.1, 0.01);
                ticks++;
            }
        }.runTaskTimer(SkyWars.getInstance(), 0L, 1L);
    }
}
