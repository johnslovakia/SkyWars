package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;
import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public class Archer implements Kit {

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
        return Util.getPrice("kits.archer", 80);
    }

    @Override
    public ItemStack getIcon() {
        return XMaterial.BOW.parseItem();
    }

    @Override
    public KitContent getContent() {
        return new KitContent(new ItemBuilder(Material.BOW).addEnchant(Enchantment.POWER, 1).addEnchant(Enchantment.FLAME, 1).damageItem(12).toItemStack(),
                new ItemStack(Material.ARROW, 7),
                new ItemBuilder(XMaterial.GOLDEN_LEGGINGS.parseMaterial()).addEnchant(Enchantment.PROTECTION, 2).toItemStack());
    }
}
