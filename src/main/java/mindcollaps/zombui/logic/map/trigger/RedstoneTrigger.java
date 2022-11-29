package mindcollaps.zombui.logic.map.trigger;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.map.enums.RedstoneSignalType;
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
import org.bukkit.block.data.Powerable;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RedstoneTrigger extends ZombieTrigger {

    private static final long serialVersionUID = 42L;

    private RedstoneSignalType redstoneSignalType = RedstoneSignalType.ON;

    public RedstoneTrigger(TriggerType type, Location location, String name) {
        super(type, location, name);
        setCustomIcon(Material.COMPARATOR);
    }

    @Override
    public void putEditor(GuiParameters parameters, Player player, CustomGoBack goBack, ZombUi zombUI) {
        parameters.getComponents().put(22, new Button(
                new CustomItem(Material.REDSTONE, "Signal Type").lore("Type: " + redstoneSignalType.name()),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        SelectorGui<RedstoneSignalType> typeSelectorGui = new SelectorGui<>(
                                zombUI,
                                new ObjectSelector<RedstoneSignalType>() {
                                    @Override
                                    public @NotNull List<RedstoneSignalType> getData() {
                                        return List.of(new RedstoneSignalType[]{
                                                RedstoneSignalType.ON,
                                                RedstoneSignalType.OFF,
                                                RedstoneSignalType.TOGGLE
                                        });
                                    }

                                    @Override
                                    public CustomItem getItem(RedstoneSignalType o) {
                                        switch (o) {
                                            case ON:
                                                return new CustomItem(Material.REDSTONE_TORCH, "On");
                                            case OFF:
                                                return new CustomItem(Material.REDSTONE_TORCH, "Off");
                                            case TOGGLE:
                                                return new CustomItem(Material.LEVER, "Toggle");
                                        }

                                        return new CustomItem(Material.BARRIER, "Error");
                                    }
                                },
                                new SelectorAction<RedstoneSignalType>() {
                                    @Override
                                    public void selected(RedstoneSignalType o) {
                                        redstoneSignalType = o;
                                        goBack.goBack();
                                    }

                                    @Override
                                    public void goBack(MCGui gui) {
                                        goBack.goBack();
                                    }
                                },
                                player,
                                "Select Signal Type",
                                false
                        );
                    }
                }
        ));
    }

    @EventHandler
    public void onInteraction(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null)
            System.out.println(event.getClickedBlock().getLocation().distance(getLocation()));
        if (event.getClickedBlock() != null)
            if (event.getClickedBlock().getLocation().distance(getLocation()) < 1) {
                if (event.getClickedBlock().getBlockData() instanceof Powerable) {
                    Powerable s = (Powerable) event.getClickedBlock().getBlockData();
                    if (s.isPowered() && redstoneSignalType == RedstoneSignalType.ON) {
                        trigger(event.getPlayer(), getZombUI());
                    } else if (!s.isPowered() && redstoneSignalType == RedstoneSignalType.OFF) {
                        trigger(null, getZombUI());
                    } else if (redstoneSignalType == RedstoneSignalType.TOGGLE) {
                        trigger(event.getPlayer(), getZombUI());
                    }
                }
            }
    }
}
