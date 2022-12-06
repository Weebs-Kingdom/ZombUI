package mindcollaps.zombui.logic;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.map.ZombieMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

public class GameSession {

    private ZombieMap map;
    private boolean sessionRunning = false;

    private ArrayList<Player> queue = new ArrayList<>();

    private int round = 0;
    private GamePlayer[] players;

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

        //add players to queue
        for (GamePlayer player : players) {
            queue.add(player.getPlayer());
        }

        //final countdown
        for (Player player : queue) {
            player.showTitle(
                    Title.title(Component.text("Game starting in 5 seconds!").color(TextColor.color(0xFF7F00)), Component.empty()));
            zombUi.getServer().getScheduler().runTaskLater(zombUi, () -> {
                player.showTitle(Title.title(Component.text("Game starting in 4 seconds!").color(TextColor.color(0xFF6F00)), Component.empty()));
                zombUi.getServer().getScheduler().runTaskLater(zombUi, () -> {
                    player.showTitle(Title.title(Component.text("Game starting in 3 seconds!").color(TextColor.color(0xFF5F00)), Component.empty()));
                    zombUi.getServer().getScheduler().runTaskLater(zombUi, () -> {
                        player.showTitle(Title.title(Component.text("Game starting in 2 seconds!").color(TextColor.color(0xFF4F00)), Component.empty()));
                        zombUi.getServer().getScheduler().runTaskLater(zombUi, () -> {
                            player.showTitle(Title.title(Component.text("Game starting in 1 seconds!").color(TextColor.color(0xFF3F00)), Component.empty()));
                            zombUi.getServer().getScheduler().runTaskLater(zombUi, () -> {
                                player.showTitle(Title.title(Component.text("Game starting in 0 seconds!").color(TextColor.color(0xFF2F00)), Component.empty()));
                                zombUi.getServer().getScheduler().runTaskLater(zombUi, () -> {
                                    player.showTitle(Title.title(Component.text("Let the game begin").color(TextColor.color(0xFF1F00)), Component.empty()));
                                    player.teleport(map.getPlayerSpawnPoint());
                                    player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.MASTER, 1, 1);
                                    startGame(zombUi);
                                }, 20);
                            }, 20);
                        }, 20);
                    }, 20);
                }, 20);
            }, 20);
        }
    }

    public void startNewRound(ZombUi zombUi) {
        round++;
        announceNewRound(zombUi);
    }

    private void announceNewRound(ZombUi zombUi) {
        for (GamePlayer player : players) {
            player.getPlayer().sendTitlePart(
                    TitlePart.TIMES,
                    Title.Times.times(
                            Duration.of(15, ChronoUnit.MILLIS),
                            Duration.of(30, ChronoUnit.MILLIS),
                            Duration.of(15, ChronoUnit.MILLIS)));
            player.getPlayer().sendTitlePart(
                    TitlePart.TITLE,
                    Component.text("Round " + round, TextColor.color(123, 0, 0))
            );
            //TODO: Check how it sounds
            player.getPlayer().playSound(player.getPlayer().getLocation().add(0, 10, 0), Sound.ENTITY_WARDEN_AMBIENT, SoundCategory.AMBIENT, 100, 1);
        }
    }

    public ZombieMap getMap() {
        return map;
    }

    public boolean isSessionRunning() {
        return sessionRunning;
    }

    public ArrayList<Player> getQueue() {
        return queue;
    }

    public int getRound() {
        return round;
    }

    public GamePlayer[] getPlayers() {
        return players;
    }
}
