package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;

import cz.johnslovakia.gameapi.game.kit.Kit;
import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class Ninja implements Kit {

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
        return Util.getPrice("kits.ninja", 80);
    }

    @Override
    public ItemStack getIcon() {
        return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.BLACK).toItemStack();
    }

    @Override
    public KitContent getContent() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 2);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.SWIFTNESS);
        potion.setItemMeta(meta);

        return new KitContent(new ItemBuilder(Material.GOLDEN_SWORD).addEnchant(Enchantment.SHARPNESS, 2).toItemStack(),
                new ItemStack(XMaterial.SNOWBALL.parseMaterial(), 10),
                new ItemStack(Material.WATER_BUCKET),
                potion,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.BLACK).addEnchant(Enchantment.PROTECTION, 4).toItemStack());
    }
}
