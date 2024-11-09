package cz.johnslovakia.skywars.listeners;

import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class PotionSplashListener implements Listener {

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e) {
        for (PotionEffect potionEffect : e.getPotion().getEffects()) {
            PotionEffectType type = potionEffect.getType();
            List<PotionEffect> newPotionEffects = new ArrayList<>();

            if (type.equals(PotionEffectType.SPEED)) {
                if (potionEffect.getAmplifier() == 1) {
                    newPotionEffects.add(new PotionEffect(PotionEffectType.SPEED, 20 * 20, potionEffect.getAmplifier()));
                } else if (potionEffect.getAmplifier() == 0) {
                    newPotionEffects.add(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 2));
                }
            } else if (type.equals(PotionEffectType.INSTANT_HEALTH)) {
                newPotionEffects.add(new PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, potionEffect.getAmplifier()));
            } else if (type.equals(PotionEffectType.FIRE_RESISTANCE)) {
                newPotionEffects.add(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, Integer.MAX_VALUE, potionEffect.getAmplifier()));
            } else if (type.equals(PotionEffectType.REGENERATION)) {
                newPotionEffects.add(new PotionEffect(PotionEffectType.REGENERATION, 15 * 20, potionEffect.getAmplifier()));
            } else if (type.equals(PotionEffectType.JUMP_BOOST)) {
                newPotionEffects.add(new PotionEffect(PotionEffectType.JUMP_BOOST, 15 * 20, 4));
                newPotionEffects.add(new PotionEffect(PotionEffectType.SPEED, 15 * 20, 1));
            } else {
                return;
            }

            e.setCancelled(true);
            for (LivingEntity livingEntity : e.getAffectedEntities()) {
                newPotionEffects.forEach(pe -> pe.apply(livingEntity));
            }
        }
    }
}
