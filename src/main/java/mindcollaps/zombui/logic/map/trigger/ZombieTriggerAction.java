package mindcollaps.zombui.logic.map.trigger;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.GameSession;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.CustomGoBack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.io.Serializable;

public abstract class ZombieTriggerAction implements Serializable {

    private static final long serialVersionUID = 42L;

    private String name;
    private Location location;

    private Material customIcon;

    public ZombieTriggerAction(Location location, String name) {
        this.location = location;
        this.name = name;
    }

    /**
     * The trigger specific fields should be located from 18 - 26
     *
     * @param parameters The Gui Parameters where the editor stuff gets added to
     */
    public abstract void putEditor(GuiParameters parameters, Player player, CustomGoBack goBack, ZombUi zombUI);

    /**
     * @return Returns the name of the action like "Door", "Redstone Signal" etc.
     */
    public abstract String getActionName();

    /**
     * This function gets called when a trigger gets triggered and an action should be performed for the specific player
     *
     * @param player The player that triggered the trigger this action is located to
     * @param zombUI The plugin instance
     */
    public abstract void action(Player player, GameSession gameSession, ZombUi zombUI);

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Material getCustomIcon() {
        return customIcon;
    }

    public void setCustomIcon(Material customIcon) {
        this.customIcon = customIcon;
    }
}
