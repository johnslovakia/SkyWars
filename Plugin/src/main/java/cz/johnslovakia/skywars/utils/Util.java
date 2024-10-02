package cz.johnslovakia.skywars.utils;

import cz.johnslovakia.gameapi.utils.Sounds;
import cz.johnslovakia.skywars.SkyWars;
import cz.johnslovakia.skywars.api.Trail;
import org.bukkit.Location;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class Util {


    public static int getPrice(String path, int defaultPrice){
        int price = DataHandler.getMainConfig().getConfig().getInt(path);
        return (price != 0 ? price : defaultPrice);
    }

    public static void killSound(String paramString, Player killer, Player dead) {
        switch (paramString.toUpperCase()) {
            case "EXPLODE SOUND":
                killer.playSound(killer.getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.EXPLODE.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "ANVIL LAND SOUND":
                killer.playSound(killer.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.ANVIL_LAND.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "GLASS SOUND":
                killer.playSound(killer.getLocation(), Sounds.GLASS.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.GLASS.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "EGG POP SOUND":
                killer.playSound(killer.getLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.CHICKEN_EGG_POP.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "WOLF HOWL SOUND":
                killer.playSound(killer.getLocation(), Sounds.WOLF_HOWL.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.WOLF_HOWL.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "SWIM SOUND":
                killer.playSound(killer.getLocation(), Sounds.SWIM.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.SWIM.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "BURP SOUND":
                killer.playSound(killer.getLocation(), Sounds.BURP.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.BURP.bukkitSound(), 20.0F, 20.0F);
                }
                break;

            case "LEVEL UP SOUND":
                killer.playSound(killer.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.LEVEL_UP.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "DRAGON HIT SOUND":
                killer.playSound(killer.getLocation(), Sounds.ENDERDRAGON_HIT.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.ENDERDRAGON_HIT.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "DRAGON DEATH SOUND":
                killer.playSound(killer.getLocation(), Sounds.ENDERDRAGON_DEATH.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.ENDERDRAGON_DEATH.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "BLAZE DEATH SOUND":
                killer.playSound(killer.getLocation(), Sounds.BLAZE_DEATH.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.BLAZE_DEATH.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "VILLAGER SOUND":
                killer.playSound(killer.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.VILLAGER_NO.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "MEOW SOUND":
                killer.playSound(killer.getLocation(), Sounds.CAT_MEOW.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.CAT_MEOW.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "GHAST MOAN SOUND":
                killer.playSound(killer.getLocation(), Sounds.GHAST_MOAN.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.GHAST_MOAN.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            case "WITHER SOUND":
                killer.playSound(killer.getLocation(), Sounds.WITHER_SPAWN.bukkitSound(), 20.0F, 20.0F);
                if (dead != null) {
                    dead.playSound(dead.getLocation(), Sounds.WITHER_SPAWN.bukkitSound(), 20.0F, 20.0F);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + paramString.toUpperCase());
        }
    }

    /*public static void setChest(BlockFace blockFace, Location location) {
        Block block = location.getBlock();
        BlockState state = block.getState();
        block.setType(Material.AIR);
        block.setType(Material.CHEST);
        state.setType(Material.CHEST);
        Chest chest = (Chest) block.getState().getData();

        switch (blockFace) {
            case EAST:
                //state.setData(new org.bukkit.material.Chest(BlockFace.EAST));
                chest.setFacingDirection(BlockFace.EAST);
                break;
            case SOUTH:
                //state.setData(new org.bukkit.material.Chest(BlockFace.SOUTH));
                chest.setFacingDirection(BlockFace.SOUTH);
                break;
            case WEST:
                //state.setData(new org.bukkit.material.Chest(BlockFace.WEST));
                chest.setFacingDirection(BlockFace.WEST);
                break;
            default:
                //state.setData(new org.bukkit.material.Chest(BlockFace.NORTH));
                chest.setFacingDirection(BlockFace.NORTH);
                break;
        }
        state.update();
    }

    public static void setChestFace(Location location) {
        String str = getCardinalDirection(location);
        BlockFace blockFace = BlockFace.valueOf(str);
        setChest(blockFace, location);
    }

    public static String getCardinalDirection(Location paramLocation) {
        double d = ((paramLocation.getYaw() - 90.0F) % 360.0F);
        if (d < 0.0D)
            d += 360.0D;
        if (0.0D <= d && d < 22.5D)
            return "EAST";
        if (67.5D <= d && d < 112.5D)
            return "SOUTH";
        if (157.5D <= d && d < 202.5D)
            return "WEST";
        if (247.5D <= d && d < 292.5D)
            return "NORTH";
        if (337.5D <= d && d < 360.0D)
            return "NORTH";
        return "NORTH";
    }*/


    /*public static void chestLookOpen(Location location, boolean open) {

        PacketContainer libPacket = new PacketContainer(PacketType.Play.Server.BLOCK_ACTION);
        Block block = location.getBlock();

        libPacket.getIntegers().write(0, 1);
        libPacket.getIntegers().write(1, open ? 1 : 0);
        libPacket.getBlocks().write(0, block.getType());
        libPacket.getBlockPositionModifier().write(0, new BlockPosition(block.getX(), block.getY(), block.getZ()));

        int distanceSquared = 64 * 64;
        ProtocolManager manager = ProtocolLibrary.getProtocolManager();

        try {
            for (Player player : location.getBlock().getWorld().getPlayers()) {
                if (player.getLocation().distanceSquared(location) < distanceSquared) {
                    manager.sendServerPacket(player, libPacket);
                }
            }
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }*/


    public static String randomString(int length, boolean numeric,
                                      boolean alphabetical, boolean symbolic) {
        if (!numeric && !alphabetical && !symbolic) {
            alphabetical = true;
            numeric = true;
        }


        String characters = (alphabetical ? "ABCDEFGHIJKLMNOPQRSTUVWXYZ" : "")
                + (numeric ? "0123456789" : "")
                + (symbolic ? "~,.:?;[]{}´`^!@#$%¨&*()-_+=></ " : "");
        Random random = new Random();

        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char c = characters.charAt(random.nextInt(characters.length()));
            if (random.nextInt(2) == 0 && Character.isUpperCase(c)) {
                c = Character.toLowerCase(c);
            }
            sb.append(c);
        }

        return sb.toString();
    }

    public static int getRandom(int lower, int upper) {
        Random random = new Random();
        return random.nextInt((upper - lower) + 1) + lower;
    }

    /*public static boolean isInvFull(Player player){
        if (player.getInventory().firstEmpty() == -1){
            return true;
        }
        return false;
    }*/

    public static boolean isInvFull(Player player) {
        int slot = player.getInventory().firstEmpty();
        if (slot == -1) {
            return true;
        }
        return false;
    }

    /*public static /*HashMap<EditSession, List<Block>>* EditSession loadCageSchematic(Location loc, String schemName) throws IOException, com.sk89q.worldedit.world.DataException, MaxChangedBlocksException {
        final File srcSchem = new File(GameAPI.getPlugin().getDataFolder()+"/cages/" + schemName + ".schematic");
        if (srcSchem.exists()) {
            World world = loc.getWorld();

            Vector v = new Vector(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()); // loc is your location variable.
            EditSession es = new EditSessionBuilder(new BukkitWorld(world)).fastmode(true).limit(FaweLimit.MAX).build();
            try {
                CuboidClipboard cc = CuboidClipboard.loadSchematic(srcSchem);
                cc.paste(es, v, true);

                /*SchematicFormat format = SchematicFormat.getFormat(srcSchem);
                format.load(srcSchem).paste(es, v, true);
                es.flushQueue();*

                /*Schematic s = FaweAPI.load(srcSchem);
                s.paste(es, v, true);

                Iterator<BlockVector> iterator = s.getClipboard().getRegion().iterator();

                List<Block> blocks = new ArrayList<>();
                while (iterator.hasNext()) {
                    BlockVector bv = iterator.next();
                    Block b = loc.getWorld().getBlockAt(new Location(loc.getWorld(), bv.getBlockX(), bv.getBlockY(), bv.getBlockZ()));
                    blocks.add(b);
                }

                HashMap<EditSession, List<Block>> map = new HashMap<>();
                map.put(es, blocks);

                return map;*
                return es;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }*/


    public static boolean isInRadius(Location check, Location start, double radius) {
        return Math.abs(check.getX() - start.getX()) <= radius &&
                Math.abs(check.getY() - start.getY()) <= radius &&
                Math.abs(check.getZ() - start.getZ()) <= radius;
    }


}
