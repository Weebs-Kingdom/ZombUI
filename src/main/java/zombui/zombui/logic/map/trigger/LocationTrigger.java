package zombui.zombui.logic.map.trigger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import zombui.zombui.ZombUi;
import zombui.zombui.visual.CustomItem;
import zombui.zombui.visual.userInterface.ActionType;
import zombui.zombui.visual.userInterface.GuiAction;
import zombui.zombui.visual.userInterface.gui.GuiParameters;
import zombui.zombui.visual.userInterface.gui.MCGui;
import zombui.zombui.visual.userInterface.gui.generic.SelectorGui;
import zombui.zombui.visual.userInterface.parts.Button;

import java.util.List;

public class LocationTrigger extends ZombieTrigger {

    private static final long serialVersionUID = 42L;
    private int radius = 5;
    //Transient
    private transient boolean triggered = false;
    private transient Player trigger;

    public LocationTrigger(TriggerType type, Location location, String name) {
        super(type, location, name);
    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent event) {
        if (event.getTo().distance(getLocation()) <= radius) {
            if (!triggered) {
                trigger(event.getPlayer(), getZombUI());
                triggered = true;
                trigger = event.getPlayer();
            }
        } else {
            if (trigger != null) {
                if (trigger.equals(event.getPlayer()) || !trigger.isOnline()) {
                    triggered = false;
                    trigger = null;
                }
            }
        }
    }

    @Override
    public void putEditor(GuiParameters parameters, Player player, SelectorGui.CustomGoBack goBack, ZombUi zombUI) {
        parameters.getComponents().put(22,
                new Button(
                        new CustomItem(Material.TARGET, "Radius").lore("R: " + radius),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                SelectorGui<Integer> selectorGui = new SelectorGui<>(
                                        zombUI,
                                        new SelectorGui.ObjectSelector<Integer>() {
                                            @Override
                                            public List<Integer> getData() {
                                                return List.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
                                            }

                                            @Override
                                            public CustomItem getItem(Integer o) {
                                                return o == radius ? new CustomItem(Material.REDSTONE_LAMP, "R: " + o).lore("Selected") : new CustomItem(Material.TARGET, "R: " + o);
                                            }
                                        },
                                        new SelectorGui.SelectorAction<Integer>() {
                                            @Override
                                            public void selected(Integer o) {
                                                radius = o;
                                                goBack.goBack();
                                            }

                                            @Override
                                            public void goBack(MCGui gui) {
                                                goBack.goBack();
                                            }
                                        },
                                        player,
                                        "Select a radius",
                                        false
                                );
                                selectorGui.generatePage(0);
                            }
                        }
                ));
    }
}
