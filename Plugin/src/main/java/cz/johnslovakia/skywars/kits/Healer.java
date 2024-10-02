package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import com.cryptomorin.xseries.XPotion;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;

import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class Healer implements Kit {

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
        return Util.getPrice("kits.healer", 80);
    }

    @Override
    public ItemStack getIcon() {
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.HEALING);
        potion.setItemMeta(meta);

        return potion;
    }

    @Override
    public KitContent getContent() {
        ItemStack potion = new ItemStack(Material.POTION, 2);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.HEALING);
        potion.setItemMeta(meta);

        return new KitContent(potion,
                XMaterial.GOLDEN_APPLE.parseItem());
    }
}