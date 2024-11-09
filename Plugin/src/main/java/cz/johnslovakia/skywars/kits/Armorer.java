package cz.johnslovakia.skywars.kits;

import com.cryptomorin.xseries.XMaterial;
import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.gameapi.game.kit.Kit;

import cz.johnslovakia.gameapi.game.kit.KitContent;
import cz.johnslovakia.gameapi.game.kit.KitManager;
import cz.johnslovakia.skywars.utils.DataHandler;
import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashMap;

public class Armorer implements Kit {

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
        return Util.getPrice("kits.armorer", 80);
    }

    @Override
    public ItemStack getIcon() {
        return XMaterial.IRON_CHESTPLATE.parseItem();
    }

    @Override
    public KitContent getContent() {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.PLAYER);
        HashMap<Integer, ItemStack> integerItemStackHashMap = inventory.addItem(XMaterial.WOODEN_SWORD.parseItem());

        return new KitContent(XMaterial.WOODEN_SWORD.parseItem(),
                XMaterial.LEATHER_CHESTPLATE.parseItem(),
                XMaterial.CHAINMAIL_BOOTS.parseItem());
    }
}

