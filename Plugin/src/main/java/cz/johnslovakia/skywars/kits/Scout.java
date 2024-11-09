package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;
import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class Scout implements Kit {

    @Override
    public KitManager getKitManager() {
        return GameAPI.getInstance().getKitManager();
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public int getPrice() {
        return Util.getPrice("kits.scout", 80);
    }

    @Override
    public ItemStack getIcon() {
        return XMaterial.SUGAR.parseItem();
    }

    @Override
    public KitContent getContent() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 3);
        PotionMeta potionMeta = (PotionMeta) potion.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionType(PotionType.STRONG_SWIFTNESS);
            potion.setItemMeta(potionMeta);
        }

        return new KitContent(new ItemStack(Material.STONE_SWORD),
                new ItemBuilder(potion).hideAllFlags().toItemStack());
    }
}