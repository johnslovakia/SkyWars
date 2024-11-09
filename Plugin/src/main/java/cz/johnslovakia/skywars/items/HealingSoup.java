package cz.johnslovakia.skywars.items;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.gameapi.utils.AbilityItem;
import cz.johnslovakia.gameapi.utils.Cooldown;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.gameapi.utils.Sounds;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class HealingSoup {

    public static AbilityItem getHealingSoupItem(){
        ItemBuilder itemBuilder = new ItemBuilder(Material.BEETROOT_SOUP);

        return new AbilityItem.Builder("Healing Soup", itemBuilder.toItemStack())
                .setLoreTranslationKey("special_item.healing_soup.lore")
                .addAction(AbilityItem.Action.RIGHT_CLICK, HealingSoup::execute)
                .addCooldown(AbilityItem.Action.RIGHT_CLICK, new Cooldown("HealingSoup", 10D))
                .setConsumable(true)
                .build();
    }

    private static void execute(GamePlayer gamePlayer){
        Player player = gamePlayer.getOnlinePlayer();

        if (player.getInventory().getItemInMainHand().getAmount() > 1) {
            player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
        } else {
            player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
        }

        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 5 * 20, 0));
        player.playSound(player.getLocation(), Sounds.CLICK.bukkitSound(), 20.0F, 20.0F);
    }
}
