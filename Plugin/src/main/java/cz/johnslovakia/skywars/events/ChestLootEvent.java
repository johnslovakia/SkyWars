package cz.johnslovakia.skywars.events;

import cz.johnslovakia.gameapi.users.GamePlayer;
import cz.johnslovakia.skywars.structures.Meteorite;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class ChestLootEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean isCancelled;

    private GamePlayer gamePlayer;
    private Meteorite meteorite;

    public ChestLootEvent(GamePlayer gamePlayer, Meteorite meteorite){
        this.gamePlayer = gamePlayer;
        this.meteorite = meteorite;

    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList(){
        return handlers;
    }

}