package zombui.zombui.logic.map.trigger;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockRedstoneEvent;
import zombui.zombui.ZombUI;
import zombui.zombui.logic.map.enums.RedstoneSignalType;

import java.io.Serializable;

public class RedstoneTrigger extends ZombieTrigger {

    private static final long serialVersionUID = 42L;

    private RedstoneSignalType redstoneSignalType;

    public RedstoneTrigger(TriggerType type, Location location, String name) {
        super(type, location, name);
    }

    @EventHandler
    public void OnRedstone(BlockRedstoneEvent event) {
        if (event.getBlock().getLocation().equals(getLocation())) {
            if (event.getBlock().isBlockPowered() && redstoneSignalType == RedstoneSignalType.ON) {
                trigger(null, getZombUI());
            } else if (!event.getBlock().isBlockPowered() && redstoneSignalType == RedstoneSignalType.OFF) {
                trigger(null, getZombUI());
            }
        }
    }
}
