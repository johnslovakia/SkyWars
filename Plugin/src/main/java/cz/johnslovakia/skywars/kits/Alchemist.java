package cz.johnslovakia.skywars.kits;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;
import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

public class Alchemist implements Kit {
    @Override
    public KitManager getKitManager() {
        return GameAPI.getInstance().getKitManager();
    }

    @Override
    public String getName() {
        return "Alchemist";
    }

    @Override
    public int getPrice() {
        return Util.getPrice("kits.archer", 80);
    }

    @Override
    public ItemStack getIcon() {
        return getPotion();
    }

    @Override
    public KitContent getContent() {
        ItemStack healingPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) healingPotion.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionType(PotionType.STRONG_HEALING);
            healingPotion.setItemMeta(potionMeta);
        }

        return new KitContent(getPotion(), healingPotion, new ItemStack(Material.LEATHER_LEGGINGS));
    }

    public ItemStack getPotion(){
        ItemStack harmingPotion = new ItemStack(Material.SPLASH_POTION);
        PotionMeta potionMeta = (PotionMeta) harmingPotion.getItemMeta();
        if (potionMeta != null) {
            potionMeta.setBasePotionType(PotionType.HARMING);
            harmingPotion.setItemMeta(potionMeta);
        }
        return harmingPotion;
    }

    //Splash Harming Potion
}
