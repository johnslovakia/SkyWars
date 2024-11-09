package cz.johnslovakia.skywars.items;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.utils.AbilityItem;
import cz.johnslovakia.gameapi.utils.Cooldown;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.gameapi.utils.Sounds;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class RunItem {

    public static AbilityItem getRunItem(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.SUGAR);

        return new AbilityItem.Builder("Run", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.run.lore")
                .addAction(AbilityItem.Action.RIGHT_CLICK, RunItem::execute)
                .addCooldown(AbilityItem.Action.RIGHT_CLICK, new Cooldown("Run", 10D))
                .setConsumable(true)
                .build();
    }

    private static void execute(GamePlayer gamePlayer){
        Player player = gamePlayer.getOnlinePlayer();

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 5 * 20, 0));
        player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
    }
}
