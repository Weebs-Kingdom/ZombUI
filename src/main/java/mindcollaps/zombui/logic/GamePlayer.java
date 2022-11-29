package mindcollaps.zombui.logic;

import org.bukkit.entity.Player;
import mindcollaps.zombui.logic.playerclass.PlayerClass;

public class GamePlayer {

    private Player player;
    private int kills = 0;
    private int deaths = 0;
    private int points = 0;
    private PlayerClass playerClass;

    public GamePlayer(Player player, PlayerClass playerClass) {
        this.player = player;
        this.playerClass = playerClass;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public PlayerClass getPlayerClass() {
        return playerClass;
    }

    public void setPlayerClass(PlayerClass playerClass) {
        this.playerClass = playerClass;
    }
}
