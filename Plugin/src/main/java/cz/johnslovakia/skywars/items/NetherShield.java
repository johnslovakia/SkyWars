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

import java.util.List;

public class NetherShield {

    public static AbilityItem getNetherShieldItem(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.SHIELD);
        itemBuilder.setCustomModelData(1010);

        return new AbilityItem.Builder("Nether Shield", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.nether_shield.lore")
                .addAction(AbilityItem.Action.RIGHT_CLICK, NetherShield::execute)
                .addCooldown(AbilityItem.Action.RIGHT_CLICK, new Cooldown("NetherShield", 10D))
                .build();
    }

    private static void execute(GamePlayer gamePlayer){
        Player player = gamePlayer.getOnlinePlayer();

        double radius = 5D;
        /*if (player.getNearbyEntities(radius, radius, radius).stream().filter(en -> en instanceof Player).filter(en -> en != player).count() == 0) {
            return;
        }*/

        List<Entity> near = player.getNearbyEntities(radius, radius, radius);
        for (Entity entity : near) {
            if (entity instanceof Player nearPlayer) {
                if (nearPlayer != player) {
                    nearPlayer.setFireTicks(100);
                    nearPlayer.damage(0.25D, player);
                }
            }
        }

        XParticle.magicCircles(5, 3, 3, 1, ParticleDisplay.of(Particle.FLAME).withLocation(player.getLocation().add(0, 0.7, 0))).run();

        player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
        //TODO: přidat custom sound
        //MessageManager.get(player, "chat.special_item_used").replace("%special_item%", eItem.getItemMeta().getDisplayName()).send();
    }
}
