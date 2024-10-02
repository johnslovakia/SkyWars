package cz.johnslovakia.skywars.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PotionSplashListener implements Listener {

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        for (PotionEffect potionEffect : e.getPotion().getEffects()) {
            if (potionEffect.getType().equals(PotionEffectType.SPEED) && potionEffect.getAmplifier() == 1) {
                PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 20 * 20, potionEffect.getAmplifier());
                e.setCancelled(true);
                for (LivingEntity livingEntity : e.getAffectedEntities()) {
                    newPotionEffect.apply(livingEntity);
                }
                break;
            }
            if (potionEffect.getType().equals(PotionEffectType.SPEED) && potionEffect.getAmplifier() == 0) {
                PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 15 * 20, 2);
                e.setCancelled(true);
                for (LivingEntity livingEntity : e.getAffectedEntities()) {
                    newPotionEffect.apply(livingEntity);
                }
                break;
            }
            if (potionEffect.getType().equals(PotionEffectType.INSTANT_HEALTH)) {
                PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 1, potionEffect.getAmplifier());
                e.setCancelled(true);
                for (LivingEntity livingEntity : e.getAffectedEntities()) {
                    newPotionEffect.apply(livingEntity);
                }
                break;
            }
            if (potionEffect.getType().equals(PotionEffectType.FIRE_RESISTANCE)) {
                PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(),  Integer.MAX_VALUE, potionEffect.getAmplifier());
                e.setCancelled(true);
                for (LivingEntity livingEntity : e.getAffectedEntities()) {
                    newPotionEffect.apply(livingEntity);
                }
                break;
            }
            if (potionEffect.getType().equals(PotionEffectType.REGENERATION)) {
                PotionEffect newPotionEffect = new PotionEffect(potionEffect.getType(), 15 * 20, potionEffect.getAmplifier());
                e.setCancelled(true);
                for (LivingEntity livingEntity : e.getAffectedEntities()) {
                    newPotionEffect.apply(livingEntity);
                }
                break;
            }
        }
    }
}
