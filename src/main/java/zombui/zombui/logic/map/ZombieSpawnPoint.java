package zombui.zombui.logic.map;

import org.bukkit.Location;

import java.io.Serializable;

public class ZombieSpawnPoint implements Serializable {

    private static final long serialVersionUID = 42L;

    private Location spawnerLocation;
    private int spawnRadius;
}
