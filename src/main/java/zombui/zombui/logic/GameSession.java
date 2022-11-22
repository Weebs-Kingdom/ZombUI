package zombui.zombui.logic;

import org.bukkit.entity.Player;
import zombui.zombui.logic.map.ZombieMap;

import java.util.ArrayList;

public class GameSession {

    private ZombieMap map;

    private boolean sessionRunning;

    private ArrayList<Player> queue;

    private int round;
    private Player[] players;


}
