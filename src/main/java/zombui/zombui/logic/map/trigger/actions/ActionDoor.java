package zombui.zombui.logic.map.trigger.actions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import zombui.zombui.ZombUI;
import zombui.zombui.logic.map.enums.DoorType;
import zombui.zombui.logic.map.trigger.ZombieTriggerAction;

import java.io.Serializable;
import java.util.logging.Level;

public class ActionDoor extends ZombieTriggerAction implements Serializable {

    private static final long serialVersionUID = 42L;

    private DoorType doorType;

    public ActionDoor(Location location, DoorType doorType) {
        super(location);
        this.doorType = doorType;
    }

    @Override
    public void action(Player player, ZombUI zombUI) {
        World w = this.getLocation().getWorld();

        Door d = null;
        try {
            d = (Door) w.getBlockState(this.getLocation());
        } catch (Exception e) {
            zombUI.getLogger().log(Level.WARNING, "The block at " + this.getLocation().toString() + " is not a door!");
            return;
        }

        switch (doorType) {
            case OPEN:
                d.setOpen(true);
                break;
            case CLOSE:
                d.setOpen(false);
                break;
            case TOGGLE:
                d.setOpen(!d.isOpen());
                break;
        }
    }
}
