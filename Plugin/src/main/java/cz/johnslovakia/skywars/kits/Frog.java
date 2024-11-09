package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;
import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class Frog implements Kit {

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
        return Util.getPrice("kits.frog", 80);
    }

    @Override
    public ItemStack getIcon() {
        return GameAPI.getInstance().getVersionSupport().getCustomHead("eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvY2NkYThiYTY0NjNiYmUyYjJmMGM2MTVhOTA2ZTU4MmUwYTc4YzgyMGM4NjJjOTQyODY5OWZhZDU4ZjQzYTVlOSJ9fX0=");
        //return new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.LIME).toItemStack();
    }

    @Override
    public KitContent getContent() {
        ItemStack potion = new ItemStack(Material.SPLASH_POTION, 2);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        meta.setBasePotionType(PotionType.STRONG_LEAPING);
        meta.setDisplayName("§aFrog Potion");
        potion.setItemMeta(meta);

        return new KitContent(potion,
                new ItemBuilder(Material.LEATHER_CHESTPLATE).setLeatherArmorColor(Color.LIME).addEnchant(Enchantment.PROTECTION, 4).toItemStack());
    }
}
