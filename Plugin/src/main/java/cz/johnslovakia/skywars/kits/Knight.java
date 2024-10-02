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

public class Knight implements Kit {

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
        return Util.getPrice("kits.knight", 80);
    }

    @Override
    public ItemStack getIcon() {
        return XMaterial.IRON_SWORD.parseItem();
    }

    @Override
    public KitContent getContent() {
        return new KitContent(new ItemBuilder(Material.IRON_SWORD).addEnchant(Enchantment.SHARPNESS, 1).toItemStack(),
                XMaterial.GOLDEN_APPLE.parseItem(),
                XMaterial.IRON_CHESTPLATE.parseItem());
    }
}
