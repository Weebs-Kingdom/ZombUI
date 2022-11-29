package mindcollaps.zombui.logic.map.trigger.actions;

import mindcollaps.zombui.logic.GameSession;
import mindcollaps.zombui.visual.userInterface.gui.generic.SelectorGui;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.data.type.Door;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.map.enums.DoorActionType;
import mindcollaps.zombui.logic.map.trigger.ZombieTriggerAction;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.parts.Button;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;

public class ActionDoor extends ZombieTriggerAction {

    @Serial
    private static final long serialVersionUID = 42L;

    private DoorActionType doorActionType = DoorActionType.OPEN;

    public ActionDoor(Location location, String name) {
        super(location, name);
    }

    @Override
    public void putEditor(GuiParameters parameters, Player player, SelectorGui.CustomGoBack goBack, ZombUi zombUI) {
        parameters.getComponents().put(22, new Button(
                new CustomItem(Material.IRON_DOOR, "Door type"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        SelectorGui<DoorActionType> selectorGui = new SelectorGui<>(
                                zombUI,
                                new SelectorGui.ObjectSelector<DoorActionType>() {
                                    @Override
                                    public @NotNull List<DoorActionType> getData() {
                                        return List.of(new DoorActionType[]{
                                                DoorActionType.OPEN,
                                                DoorActionType.CLOSE,
                                                DoorActionType.TOGGLE
                                        });
                                    }

                                    @Override
                                    public CustomItem getItem(DoorActionType o) {
                                        switch (o) {
                                            case OPEN -> {
                                                return doorActionType == DoorActionType.OPEN ? new CustomItem(Material.OAK_TRAPDOOR, "Open").lore("Selected") : new CustomItem(Material.OAK_TRAPDOOR, "Open");
                                            }
                                            case CLOSE -> {
                                                return doorActionType == DoorActionType.CLOSE ? new CustomItem(Material.IRON_TRAPDOOR, "Close").lore("Selected") : new CustomItem(Material.IRON_TRAPDOOR, "Close");
                                            }
                                            case TOGGLE -> {
                                                return doorActionType == DoorActionType.TOGGLE ? new CustomItem(Material.LEVER, "Toggle").lore("Selected") : new CustomItem(Material.LEVER, "Toggle");
                                            }
                                        }

                                        zombUI.getLogger().log(Level.SEVERE, "A Door type was not registered in the gui!");
                                        return new CustomItem(Material.BARRIER, "Type not found");
                                    }
                                },
                                new SelectorGui.SelectorAction<DoorActionType>() {
                                    @Override
                                    public void selected(DoorActionType o) {
                                        doorActionType = o;
                                        goBack.goBack();
                                    }

                                    @Override
                                    public void goBack(MCGui gui) {
                                        goBack.goBack();
                                    }
                                },
                                player,
                                "Select Door Type",
                                false
                        );

                        selectorGui.generatePage(0);
                    }
                }
        ));
    }

    @Override
    public String getActionName() {
        return "Door";
    }

    @Override
    public void action(Player player, GameSession gameSession, ZombUi zombUI) {
        World w = this.getLocation().getWorld();

        Block b = w.getBlockAt(getLocation());
        Door d = null;
        BlockState state = null;
        try {
            state = b.getState();
            d = (Door) state.getBlockData();
        } catch (Exception e) {
            zombUI.getLogger().log(Level.WARNING, "The block at " + this.getLocation().toString() + " is not a door!");
            return;
        }

        switch (doorActionType) {
            case OPEN -> d.setOpen(true);
            case CLOSE -> d.setOpen(false);
            case TOGGLE -> d.setOpen(!d.isOpen());
        }
        b.setBlockData(d);
        b.getState().update();
    }
}
