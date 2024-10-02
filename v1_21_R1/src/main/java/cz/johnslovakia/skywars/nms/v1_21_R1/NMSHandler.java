package cz.johnslovakia.skywars.nms.v1_21_R1;

import com.cryptomorin.xseries.particles.ParticleDisplay;
import com.cryptomorin.xseries.particles.XParticle;
import cz.johnslovakia.skywars.api.VersionSupport;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.Directional;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class NMSHandler implements VersionSupport {

    @Override
    public ItemStack getItemInMainHand(Player player) {
        return player.getInventory().getItemInMainHand();
    }

    @Override
    public void setItemInMainHand(Player player, ItemStack item) {
        player.getInventory().setItemInMainHand(item);
    }


    private double calculateDamageApplied(double damage, double points, double toughness, int resistance, int epf) {
        double withArmorAndToughness = damage * (1 - Math.min(20, Math.max(points / 5, points - damage / (2 + toughness / 4))) / 25);
        double withResistance = withArmorAndToughness * (1 - (resistance * 0.2));
        double withEnchants = withResistance * (1 - (Math.min(20.0, epf) / 25));
        return withEnchants;
    }

    private int getEPF(PlayerInventory inv) {
        ItemStack helm = inv.getHelmet();
        ItemStack chest = inv.getChestplate();
        ItemStack legs = inv.getLeggings();
        ItemStack boot = inv.getBoots();

        return (helm != null ? helm.getEnchantmentLevel(Enchantment.SHARPNESS) : 0) +
                (chest != null ? chest.getEnchantmentLevel(Enchantment.SHARPNESS) : 0) +
                (legs != null ? legs.getEnchantmentLevel(Enchantment.SHARPNESS) : 0) +
                (boot != null ? boot.getEnchantmentLevel(Enchantment.SHARPNESS) : 0);
    }

    @Override
    public void damagePlayer(Player player, double damage) {
        double points = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
        double toughness = player.getAttribute(Attribute.GENERIC_ARMOR_TOUGHNESS).getValue();
        PotionEffect effect = player.getPotionEffect(PotionEffectType.RESISTANCE);
        int resistance = effect == null ? 0 : effect.getAmplifier();
        int epf = getEPF(player.getInventory());

        player.damage(calculateDamageApplied(damage, points, toughness, resistance, epf));
    }

    @Override
    public void infernoEffect(Player player) {
        ParticleDisplay pd = ParticleDisplay.of(Particle.FLAME).withLocation(player.getLocation());
        pd.count = 50;
        pd.extra = 5;
        XParticle.sphere(5.0F, 0.3, pd);
    }

    @Override
    public Location getChestLocation(InventoryCloseEvent event) {
        if (event.getInventory().getType() == InventoryType.CHEST) {
            if (event.getInventory().getLocation() == null) {
                return null;
            }
            return event.getInventory().getLocation();
        }
        return null;
    }

    @Override
    public void setChestFacingDirection(BlockFace blockFace, Location location) {
        Block block = location.getBlock();
        BlockState state = block.getState();
        Directional data = (Directional)block.getBlockData();

        switch (blockFace) {
            case EAST:
                data.setFacing(BlockFace.EAST);
                break;
            case SOUTH:
                data.setFacing(BlockFace.SOUTH);
                break;
            case WEST:
                data.setFacing(BlockFace.WEST);
                break;
            default:
                data.setFacing(BlockFace.NORTH);
                break;
        }
        block.setBlockData(data);
    }
}
