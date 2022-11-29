package mindcollaps.zombui.logic.map.trigger.actions;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.GameSession;
import mindcollaps.zombui.logic.map.enums.RedstoneSignalType;
import mindcollaps.zombui.logic.map.trigger.ZombieTriggerAction;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.SelectorGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.CustomGoBack;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.ObjectSelector;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.SelectorAction;
import mindcollaps.zombui.visual.userInterface.parts.Button;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.List;

public class ActionRedstoneSignal extends ZombieTriggerAction {

    @Serial
    private static final long serialVersionUID = 42L;

    private RedstoneSignalType signalType = RedstoneSignalType.TOGGLE;

    public ActionRedstoneSignal(Location location, String name) {
        super(location, name);
        setCustomIcon(Material.REDSTONE_BLOCK);
    }

    @Override
    public void putEditor(GuiParameters parameters, Player player, CustomGoBack goBack, ZombUi zombUI) {
        parameters.getComponents().put(22, new Button(
                new CustomItem(Material.REDSTONE_LAMP, "Signal type"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        SelectorGui<RedstoneSignalType> selectorGui = new SelectorGui<>(
                                zombUI,
                                new ObjectSelector<RedstoneSignalType>() {
                                    @Override
                                    public @NotNull List<RedstoneSignalType> getData() {
                                        return List.of(new RedstoneSignalType[]{
                                                RedstoneSignalType.TOGGLE,
                                                RedstoneSignalType.ON,
                                                RedstoneSignalType.OFF,
                                                RedstoneSignalType.PULSE_ON,
                                                RedstoneSignalType.PULSE_OFF
                                        });
                                    }

                                    @Override
                                    public CustomItem getItem(RedstoneSignalType o) {
                                        switch (o) {
                                            case TOGGLE -> {
                                                return signalType == RedstoneSignalType.TOGGLE ? new CustomItem(Material.LEVER, "Toggle").lore("Selected") : new CustomItem(Material.REDSTONE_TORCH, "Toggle");
                                            }
                                            case ON -> {
                                                return signalType == RedstoneSignalType.ON ? new CustomItem(Material.REDSTONE_TORCH, "On").lore("Selected") : new CustomItem(Material.REDSTONE_TORCH, "On");
                                            }
                                            case OFF -> {
                                                return signalType == RedstoneSignalType.OFF ? new CustomItem(Material.REDSTONE_TORCH, "Off").lore("Selected") : new CustomItem(Material.REDSTONE_TORCH, "Off");
                                            }
                                            case PULSE_ON -> {
                                                return signalType == RedstoneSignalType.PULSE_ON ? new CustomItem(Material.OAK_BUTTON, "Pulse On").lore("Selected") : new CustomItem(Material.REDSTONE_TORCH, "Pulse On");
                                            }
                                            case PULSE_OFF -> {
                                                return signalType == RedstoneSignalType.PULSE_OFF ? new CustomItem(Material.OAK_BUTTON, "Pulse Off").lore("Selected") : new CustomItem(Material.REDSTONE_TORCH, "Pulse Off");
                                            }
                                        }
                                        return null;
                                    }
                                },
                                new SelectorAction<RedstoneSignalType>() {
                                    @Override
                                    public void selected(RedstoneSignalType o) {
                                        signalType = o;
                                        goBack.goBack();
                                    }

                                    @Override
                                    public void goBack(MCGui gui) {
                                        goBack.goBack();
                                    }
                                },
                                player,
                                "Select signal type",
                                false
                        );
                        selectorGui.generatePage(0);
                    }
                }
        ));
    }

    @Override
    public String getActionName() {
        return "Redstone Signal";
    }

    @Override
    public void action(Player player, GameSession gameSession, ZombUi zombUI) {
        System.out.println("DEBUG! Redstone action triggered");
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
