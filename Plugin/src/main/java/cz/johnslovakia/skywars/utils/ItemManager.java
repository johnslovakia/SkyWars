package cz.johnslovakia.skywars.utils;

import cz.johnslovakia.gameapi.utils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class ItemManager {

    private static List<ItemStack> iron = new ArrayList<>();
    private static List<ItemStack> diamond = new ArrayList<>();
    private static List<ItemStack> luckyBlock = new ArrayList<>();

    private static Map<Player, List<ItemStack>> playerIronItems = new HashMap<>();
    private static Map<Player, List<ItemStack>> playerDiamondItems = new HashMap<>();

    public static void loadItems(){
        iron.add(new ItemStack(Material.IRON_SWORD));
        iron.add(new ItemStack(Material.IRON_BOOTS));
        iron.add(new ItemStack(Material.IRON_HELMET));
        iron.add(new ItemStack(Material.IRON_LEGGINGS));
        iron.add(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.SHARPNESS, 1).toItemStack());
        iron.add(new ItemBuilder(Material.IRON_BOOTS).addEnchant(Enchantment.PROTECTION, 2).toItemStack());
        iron.add(new ItemBuilder(Material.IRON_CHESTPLATE).addEnchant(Enchantment.PROTECTION, 1).toItemStack());
        iron.add(new ItemBuilder(Material.IRON_LEGGINGS).addEnchant(Enchantment.PROTECTION, 2).toItemStack());
        iron.add(new ItemBuilder(Material.IRON_PICKAXE).addEnchant(Enchantment.EFFICIENCY, 3).toItemStack());
        iron.add(new ItemBuilder(Material.IRON_AXE).addEnchant(Enchantment.EFFICIENCY, 2).toItemStack());

        diamond.add(new ItemStack(Material.DIAMOND_SWORD));
        diamond.add(new ItemStack(Material.DIAMOND_PICKAXE));
        diamond.add(new ItemStack(Material.DIAMOND_AXE));
        diamond.add(new ItemStack(Material.DIAMOND_CHESTPLATE));
        diamond.add(new ItemStack(Material.DIAMOND_LEGGINGS));
        diamond.add(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.SHARPNESS, 1).toItemStack());
        diamond.add(new ItemBuilder(Material.DIAMOND_BOOTS).addEnchant(Enchantment.PROTECTION, 2).toItemStack());
        diamond.add(new ItemBuilder(Material.DIAMOND_HELMET).addEnchant(Enchantment.PROTECTION, 2).toItemStack());
        diamond.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION, 1).toItemStack());

        luckyBlock.add(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.SHARPNESS, 4).addEnchant(Enchantment.FIRE_ASPECT, 1).setName("§bRoyal sword").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.DIAMOND_SWORD).addEnchant(Enchantment.SHARPNESS, 2).addEnchant(Enchantment.FIRE_ASPECT, 2).setName("§bArthur's sword").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.BOW).addEnchant(Enchantment.POWER, 2).addEnchant(Enchantment.PUNCH, 1).setName("§bAccurate bow").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.BOW).addEnchant(Enchantment.POWER, 3).addEnchant(Enchantment.FLAME, 1).setName("§bKatarin's bow").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.DIAMOND_CHESTPLATE).addEnchant(Enchantment.PROTECTION, 3).addEnchant(Enchantment.THORNS, 2).setName("§bSauron's chestplate").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.DIAMOND_LEGGINGS).addEnchant(Enchantment.PROTECTION, 4).addEnchant(Enchantment.THORNS, 2).setName("§bNapoleon's leggings").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.BLAZE_ROD).addEnchant(Enchantment.FIRE_ASPECT, 2).addEnchant(Enchantment.KNOCKBACK, 1).setName("§bFire staff").toItemStack());
        luckyBlock.add(new ItemBuilder(Material.STICK).addEnchant(Enchantment.KNOCKBACK, 3).setName("§bPing-pong").toItemStack());
    }

    public static List<ItemStack> getIronItems() {
        return iron;
    }

    public static List<ItemStack> getDiamondItems() {
        return diamond;
    }

    public static List<ItemStack> getLuckyBlockItems() {
        return luckyBlock;
    }


    public static void addPlayerIronItem(Player player, ItemStack item){
        if (playerIronItems.get(player) != null) {
            List<ItemStack> is = new ArrayList<>();
            for (ItemStack itemStack : playerIronItems.get(player)) {
                is.add(itemStack);
            }
            is.add(item);
            playerIronItems.put(player, is);
        }else{
            playerIronItems.put(player, Arrays.asList(item));
        }
    }

    public static int hasIronItems(Player player){
        if (playerIronItems.get(player) != null){
            return playerIronItems.get(player).size();
        }else{
            return 0;
        }
    }

    public static void addPlayerDiamondItem(Player player, ItemStack item){
        if (playerDiamondItems.get(player) != null) {
            List<ItemStack> is = new ArrayList<>();
            for (ItemStack itemStack : playerDiamondItems.get(player)) {
                is.add(itemStack);
            }
            is.add(item);
            playerDiamondItems.put(player, is);
        }else{
            playerDiamondItems.put(player, Arrays.asList(item));
        }
    }

    public static int hasDiamondItems(Player player){
        if (playerDiamondItems.get(player) != null){
            return playerDiamondItems.get(player).size();
        }else{
            return 0;
        }
    }

    public static ItemStack getIronItem(Player player){
        if (hasIronItems(player) == 0){
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack is : iron){
                if (is.getType().toString().contains("SWORD")){
                    items.add(is);
                }
            }
            ItemStack item = items.get(new Random().nextInt((items.size())));
            addPlayerIronItem(player, item);
            return item;
        }else if (hasIronItems(player) == 1){
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack is : iron){
                if (is.getType().toString().contains("HELMET")
                || is.getType().toString().contains("CHESTPLATE")
                || is.getType().toString().contains("LEGGINGS")
                || is.getType().toString().contains("BOOTS")){
                    items.add(is);
                }
            }
            ItemStack item = items.get(new Random().nextInt((items.size())));
            addPlayerIronItem(player, item);
            return item;
        }else if (hasIronItems(player) == 2){
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack is : iron){
                if (is.getType().toString().contains("HELMET")
                        || is.getType().toString().contains("CHESTPLATE")
                        || is.getType().toString().contains("LEGGINGS")
                        || is.getType().toString().contains("BOOTS")){
                    items.add(is);
                }
            }
            ItemStack item = items.get(new Random().nextInt((items.size())));
            addPlayerIronItem(player, item);
            return item;
        }else{
            ItemStack item = iron.get(new Random().nextInt((iron.size())));
            addPlayerIronItem(player, item);
            return item;
        }
    }

    public static ItemStack getDiamondItem(Player player){
        if (hasDiamondItems(player) == 1){
            List<ItemStack> items = new ArrayList<>();
            for (ItemStack is : iron){
                if (is.getType().toString().contains("HELMET")
                        || is.getType().toString().contains("CHESTPLATE")
                        || is.getType().toString().contains("LEGGINGS")
                        || is.getType().toString().contains("BOOTS")){
                    items.add(is);
                }
            }
            ItemStack item = items.get(new Random().nextInt((items.size())));
            addPlayerDiamondItem(player, item);
            return item;
        }else{
            ItemStack item = diamond.get(new Random().nextInt((diamond.size())));
            addPlayerDiamondItem(player, item);
            return item;
        }
    }
}
