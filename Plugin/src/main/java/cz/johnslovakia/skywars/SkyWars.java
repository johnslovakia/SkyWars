package cz.johnslovakia.skywars;

import cz.johnslovakia.gameapi.GameAPI;
import cz.johnslovakia.skywars.boards.Scoreboard;
import cz.johnslovakia.skywars.items.GhostPearl;
import cz.johnslovakia.skywars.listeners.*;
import cz.johnslovakia.skywars.perks.BlazingArrow;
import cz.johnslovakia.skywars.perks.Digger;
import cz.johnslovakia.skywars.perks.ResistanceBoost;
import cz.johnslovakia.skywars.perks.VoidScavenger;
import cz.johnslovakia.skywars.quests.daily.Survivor;
import cz.johnslovakia.skywars.utils.Commands;
import cz.johnslovakia.skywars.utils.DataHandler;
import lombok.Getter;
import me.zort.containr.Containr;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SkyWars extends JavaPlugin{

    @Getter
    private static SkyWars instance;

    @Override
    public void onEnable() {
        instance = this;
        Containr.init(this);

        GameAPI.getInstance().registerMinigame(new SkyWarsMinigame());


        File messagesFolder = new File(getDataFolder(), "languages");
        /*File czechScoreboard = new File(messagesFolder, "czech_scoreboard.yml");
        if (!czechScoreboard.exists()) {
            saveResource("languages/czech_scoreboard.yml", false);
        }*/
        File englishScoreboard = new File(messagesFolder, "english_scoreboard.yml");
        if (!englishScoreboard.exists()) {
            saveResource("languages/english_scoreboard.yml", false);
        }

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new GhostPearl(), SkyWars.getInstance());
        pm.registerEvents(new BlockBreakListener(), this);
        pm.registerEvents(new InventoryCloseListener(), this);
        pm.registerEvents(new InteractListener(), this);
        pm.registerEvents(new TaskListener(), this);
        pm.registerEvents(new TNTandFireballListener(), this);
        pm.registerEvents(new InventoryClickListener(), this);
        pm.registerEvents(new PotionSplashListener(), this);
        pm.registerEvents(new ItemConsumeListener(), this);
        pm.registerEvents(new PreparationStartListener(), this);
        pm.registerEvents(new MotdListener(), this);
        pm.registerEvents(new EnchantingTableListener(), this);
        pm.registerEvents(new GameResetListener(), this);
        pm.registerEvents(new JoinQuitListener(), this);
        pm.registerEvents(new Scoreboard(), this);

        pm.registerEvents(new BlazingArrow(), this);
        pm.registerEvents(new Digger(), this);
        pm.registerEvents(new ResistanceBoost(), this);
        pm.registerEvents(new VoidScavenger(), this);

        pm.registerEvents(new Survivor(), this);

        getCommand("skywars").setExecutor(new Commands());


        File config = new File(getDataFolder(), "config.yml");
        if (!config.exists()) {
            DataHandler.createMainConfig();
        }
    }


}
