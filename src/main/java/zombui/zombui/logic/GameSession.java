package zombui.zombui.logic;

import org.bukkit.entity.Player;
import zombui.zombui.ZombUi;
import zombui.zombui.logic.map.ZombieMap;

import java.util.ArrayList;

public class GameSession {

    private ZombieMap map;
    private boolean sessionRunning = false;

    private ArrayList<Player> queue = new ArrayList<>();

    private int round = 0;
    private Player[] players;

    public GameSession(ZombieMap map) {
        this.map = map;
    }

    public void stopGame(ZombUi zombUi) {
        map.deactivateWorld(zombUi);
    }

    public void startGame(ZombUi zombUi) {
        map.initWorld(zombUi);
    }

    public void initQueue(ZombUi zombUi) {

    }
}
