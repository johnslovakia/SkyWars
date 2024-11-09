package cz.johnslovakia.skywars.items;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.utils.*;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.awt.*;

public class SparkOfLevitationItem {

    public static AbilityItem getSparkOfLevitationItem(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.CHORUS_FRUIT);
        itemBuilder.setCustomModelData(1011);

        return new AbilityItem.Builder("Spark of Levitation", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.spark_of_levitation.lore")
                .addAction(AbilityItem.Action.RIGHT_CLICK, SparkOfLevitationItem::execute)
                .addCooldown(AbilityItem.Action.RIGHT_CLICK, new Cooldown("Levitation", 2.5D))
                .setConsumable(true)
                .build();
    }

    private static void execute(GamePlayer gamePlayer){
        Player player = gamePlayer.getOnlinePlayer();

        Utils.levitatePlayer(player, 6, 13);
        XParticle.sphere(1.3, 6, ParticleDisplay.of(Particle.DUST).withColor(Color.CYAN, 0.7f).withLocation(player.getLocation().add(0.5, 0.3, 0.5)));
        player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
    }
}
