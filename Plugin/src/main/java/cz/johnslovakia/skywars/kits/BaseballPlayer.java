package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;

import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.gameapi.utils.ItemBuilder;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public class BaseballPlayer implements Kit {

    @Override
    public KitManager getKitManager() {
        return GameAPI.getInstance().getKitManager();
    }

    @Override
    public String getName() {
        return "Baseball Player";
    }

    @Override
    public int getPrice() {
        return Util.getPrice("kits.baseballplayer", 80);
    }

    @Override
    public ItemStack getIcon() {
        return XMaterial.STICK.parseItem();
    }

    @Override
    public KitContent getContent() {
        return new KitContent(new ItemBuilder(XMaterial.STICK.parseMaterial()).addEnchant(Enchantment.KNOCKBACK, 1).toItemStack(),
                new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION, 2).toItemStack());
    }
}
