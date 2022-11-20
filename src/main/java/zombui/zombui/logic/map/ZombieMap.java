package zombui.zombui.logic.map;

import org.bukkit.Location;
import org.bukkit.World;

import java.io.Serializable;
import java.util.ArrayList;

public class ZombieMap implements Serializable {

    private static final long serialVersionUID = 42L;

    //World stuff
    private String worldName;

    //Zombie stuff
    private ArrayList<ZombieSpawnPoint> zombieSpawnPoints;

    //Player stuff
    private Location playerSpawnPoint;
    private Location redstoneSignalLocation; //Places a Redstone block somewhere when the game starts

}
