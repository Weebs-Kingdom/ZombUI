package zombui.zombui.util;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import zombui.zombui.visual.userInterface.gui.generic.PositionGui;

public class ZombieUtil {
    public static String getLocationString(Location location) {
        location = location.toBlockLocation();

        return "x: " + location.getX() + " y: " + location.getY() + " z: " + location.getZ();
    }

    public static PositionGui.WorldEditPosition getWorldeditLocation(Player player) {
        boolean foundWorldEditLocation = true;
        Location worldEditLocation = null;
        Location firstWorldEditLocation = null;
        int radius = -1;
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
            Region region = session.getSelection(session.getSelectionWorld());
            Vector3 center = region.getCenter();
            radius = (int) Math.ceil(region.getMaximumPoint().distance(region.getMinimumPoint()) / 2);
            worldEditLocation = new Location(player.getWorld(), center.getX(), center.getY(), center.getZ());
            firstWorldEditLocation = new Location(player.getWorld(), region.getMinimumPoint().getX(), region.getMinimumPoint().getY(), region.getMinimumPoint().getZ());
        } catch (Exception e) {
            foundWorldEditLocation = false;
        }

        if (foundWorldEditLocation) {
            return new PositionGui.WorldEditPosition(radius, worldEditLocation, firstWorldEditLocation);
        } else {
            return null;
        }
    }
}
