package cz.johnslovakia.skywars.chest;

import cz.johnslovakia.skywars.utils.Util;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

//TODO: rewrite
public class ChestItem {

    private ChestManager manager;

    private ItemStack item;
    private Integer limit = 0;
    private Integer limitInIsland = -1;
    private Integer limitInChest = -1;
    private Map<String, Integer> insterted = new HashMap<>();
    private Map<Chest, Integer> instertedInChests = new HashMap<>();
    private Integer gived = 0;
    private Integer chance = 15;
    private boolean randomAmount = false;

    private ChestType chestType;
    private boolean mustBeInserted = false;

    //randomAmount
    private Integer lower;
    private Integer upper;

    public ChestItem(ChestManager manager, ChestType chestType, ItemStack item){
        this.manager = manager;
        this.chestType = chestType;
        this.item = item;
    }

    public ChestItem(ChestManager manager, ChestType chestType,  ItemStack item, boolean mustBeInserted){
        this.manager = manager;
        this.chestType = chestType;
        this.item = item;
        this.mustBeInserted = mustBeInserted;
    }

    public ChestItem(ChestManager manager, ChestType chestType,  ItemStack item, Integer chance){
        this.manager = manager;
        this.chestType = chestType;
        this.item = item;
        this.chance = chance;
    }

    public ChestItem setItemRandomAmount(Integer lower, Integer upper){
        this.lower = lower;
        this.upper = upper;
        this.randomAmount = true;
        return this;
    }

    public ChestItem setLimit(Integer limit){
        this.limit = limit;
        return this;
    }

    public Integer getLimitInChest() {
        return limitInChest;
    }

    public ChestItem setLimitInChest(Integer limitInChest) {
        this.limitInChest = limitInChest;
        return this;
    }

    public ChestItem setChance(Integer chance) {
        this.chance = chance;
        return this;
    }

    public ItemStack getItem() {
        ItemStack itemStack = item;
        if (randomAmount){
            itemStack.setAmount(Util.getRandom(lower, upper));
        }
        return itemStack;
    }

    public void setItem(Inventory inventory, int slot) {
        ItemStack itemStack = getItem();
        if (itemStack == null){
            return;
        }

        if (limit != 0){
            gived = gived + itemStack.getAmount();

            if (gived /*+ itemStack.getAmount()*/ >= limit){
                if(chestType.equals(ChestType.NORMAL)){
                    manager.getNormalItems().remove(this);
                }else if(chestType.equals(ChestType.RARE)){
                    manager.getRareItems().remove(this);
                }
                return;
            }
        }

        inventory.setItem(slot, getItem());
    }

    public Integer getLimit() {
        return limit;
    }

    public boolean mustBeInserted() {
        return mustBeInserted;
    }

    public ChestItem setMustBeInserted(boolean mustBeInserted) {
        this.mustBeInserted = mustBeInserted;
        return this;
    }

    public Integer getChance() {
        return chance;
    }

    public ChestType getChestType() {
        return chestType;
    }

    public Integer getLimitInIsland() {
        return limitInIsland;
    }

    public ChestItem setLimitInIsland(Integer limitInIsland) {
        this.limitInIsland = limitInIsland;
        return this;
    }

    public void setGived(Integer gived) {
        this.gived = gived;
    }

    public int getInsterted(String island) {
        return (insterted.get(island) != null ? insterted.get(island) : 0);
    }

    public void insterted(String island){
        insterted.put(island, (insterted.get(island) == null ? 1 : insterted.get(island) + 1));
    }

    public int getInstertedInChests(Chest chest) {
        return (instertedInChests.get(chest) != null ? instertedInChests.get(chest) : 0);
    }

    public void instertedInChests(Chest chest){
        instertedInChests.put(chest, (instertedInChests.get(chest) == null ? 1 : instertedInChests.get(chest) + 1));
    }

    public Map<String, Integer> getInsterted() {
        return insterted;
    }

    public Map<Chest, Integer> getInstertedInChests() {
        return instertedInChests;
    }
}
