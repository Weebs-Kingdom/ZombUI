package mindcollaps.zombui.logic.map.trigger;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.GameSession;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.CustomGoBack;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

public abstract class ZombieTrigger implements Serializable, Listener {

    private static final long serialVersionUID = 42L;

    //Transient
    private transient boolean triggered = false;
    private transient boolean registered = false;
    private transient ArrayList<UUID> triggeredPlayers;
    private transient ZombUi zombUI;

    private String name;
    private TriggerType type;
    private Location location;
    private ZombieTriggerAction[] actions = new ZombieTriggerAction[0];

    private Material customIcon;

    public ZombieTrigger(TriggerType type, Location location, String name) {
        this.type = type;
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
     * This function needs to be called when this trigger gets set active and the listeners should be activated
     *
     * @param zombUI The plugin instance
     */
    public void initListener(ZombUi zombUI) {
        if (!registered) {
            zombUI.getServer().getPluginManager().registerEvents(this, zombUI);
            this.zombUI = zombUI;
            registered = true;
            triggered = false;
            triggeredPlayers = new ArrayList<>();
        }
    }

    public void removeListener(ZombUi zombUI) {
        HandlerList.unregisterAll(this);
        this.registered = false;
    }

    /**
     * This function gets called once a trigger class notices a event that triggers the trigger
     *
     * @param player The player that triggers the trigger
     * @param zombUI The plugin instance
     */
    public void trigger(Player player, ZombUi zombUI) {
        if (triggered && type == TriggerType.ONCE) return;
        if (type == TriggerType.PER_PLAYER && triggeredPlayers.contains(player.getUniqueId())) return;

        triggeredPlayers.add(player.getUniqueId());
        triggered = true;
        GameSession gameSession = zombUI.getGameSession(player.getWorld());
        for (ZombieTriggerAction response : actions) {
            response.action(player, gameSession, zombUI);
        }

        if (type == TriggerType.ONCE) removeListener(zombUI);
    }


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public TriggerType getType() {
        return type;
    }

    public void setType(TriggerType type) {
        this.type = type;
    }

    public ZombieTriggerAction[] getActions() {
        return actions;
    }

    public void setActions(ZombieTriggerAction[] actions) {
        this.actions = actions;
    }

    public ZombUi getZombUI() {
        return zombUI;
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

    /**
     * ONCE = The trigger can get activated once per session
     * REPEAT = The trigger can get activated multiple times
     * PER_PLAYER = Each player in a session can trigger this trigger once
     */
    public enum TriggerType {
        ONCE, REPEAT, PER_PLAYER;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }
}
