package zombui.zombui.logic.map.trigger.actions;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import zombui.zombui.ZombUI;
import zombui.zombui.logic.map.enums.RedstoneSignalType;
import zombui.zombui.logic.map.trigger.ZombieTriggerAction;

import java.io.Serializable;

public class ActionRedstoneSignal extends ZombieTriggerAction implements Serializable {

    private static final long serialVersionUID = 42L;

    private RedstoneSignalType signalType;

    public ActionRedstoneSignal(Location location, RedstoneSignalType signalType) {
        super(location);
        this.signalType = signalType;
    }

    @Override
    public void action(Player player, ZombUI zombUI) {
        World w = this.getLocation().getWorld();
        switch (signalType) {
            case ON:
                w.getBlockAt(this.getLocation()).setType(Material.REDSTONE_BLOCK);
                break;
            case OFF:
                w.getBlockAt(this.getLocation()).setType(Material.AIR);
                break;
            case TOGGLE:
                if (w.getBlockAt(this.getLocation()).getType() == Material.AIR) {
                    w.getBlockAt(this.getLocation()).setType(Material.REDSTONE_BLOCK);
                } else {
                    w.getBlockAt(this.getLocation()).setType(Material.AIR);
                }
                break;
            case PULSE_ON:
                w.getBlockAt(this.getLocation()).setType(Material.REDSTONE_BLOCK);
                zombUI.getServer().getScheduler().runTaskLater(zombUI, () -> w.getBlockAt(this.getLocation()).setType(Material.AIR), 20);
                break;
            case PULSE_OFF:
                w.getBlockAt(this.getLocation()).setType(Material.AIR);
                zombUI.getServer().getScheduler().runTaskLater(zombUI, () -> w.getBlockAt(this.getLocation()).setType(Material.REDSTONE_BLOCK), 20);
                break;
        }
    }
}
