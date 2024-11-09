package cz.johnslovakia.skywars.items;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.utils.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;

public class SparkOfInvisibilityItem {

    public static AbilityItem getSparkOfInvisibilityItem(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.CHORUS_FRUIT);
        itemBuilder.setCustomModelData(1010);

        return new AbilityItem.Builder("Spark of Invisibility", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.spark_of_invisibility.lore")
                .addAction(AbilityItem.Action.RIGHT_CLICK, SparkOfInvisibilityItem::execute)
                .addCooldown(AbilityItem.Action.RIGHT_CLICK, new Cooldown("Invisibility", 2.5D))
                .setConsumable(true)
                .build();
    }

    private static void execute(GamePlayer gamePlayer){
        Player player = gamePlayer.getOnlinePlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 50, 0));
        XParticle.sphere(1.3, 7, ParticleDisplay.of(Particle.DUST).withColor(Color.WHITE, 0.7f).withLocation(player.getLocation().add(0.5, 0.3, 0.5)));
        player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
    }
}
