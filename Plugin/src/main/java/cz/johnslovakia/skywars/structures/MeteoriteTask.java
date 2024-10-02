package cz.johnslovakia.skywars.structures;

import cz.johnslovakia.gameapi.messages.MessageManager;
import cz.johnslovakia.gameapi.task.Task;
import cz.johnslovakia.gameapi.task.TaskInterface;
import cz.johnslovakia.gameapi.users.GamePlayer;

public class MeteoriteTask implements TaskInterface {

    @Override
    public void onStart(Task task) {

    }

    @Override
    public void onCount(Task task) {
        if (task.getCounter() == 15
            || task.getCounter() == 5) {
            for (GamePlayer gamePlayer : task.getGame().getParticipants()) {
                MessageManager.get(gamePlayer, "chat.meteorit_will_crash_in")
                        .replace("%time%", "" + task.getCounter())
                        .send();
            }
        }
    }

    @Override
    public void onEnd(Task task) {
        Meteorite meteorit = new Meteorite(task.getGame());
        meteorit.spawn();
    }
}
