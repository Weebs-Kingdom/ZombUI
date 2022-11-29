package mindcollaps.zombui.visual.userInterface.gui.generic;

import mindcollaps.zombui.util.ZombieUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.Text;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.parts.Button;

public class PositionGui {

    private final ZombUi plugin;

    //GUI
    private final PositionGuiAction action;
    private final String title;
    private final Player player;
    private MCGui mcGui;

    public PositionGui(ZombUi plugin, String title, Player player, PositionGuiAction action) {
        this.plugin = plugin;
        this.title = title;
        this.player = player;
        this.action = action;
    }

    public void generatePage(boolean area) {
        GuiParameters page = new GuiParameters();
        page.setTitle(title);
        page.setSlots(27);

        WorldEditPosition worldEditLocation = ZombieUtil.getWorldeditLocation(player);

        if (worldEditLocation != null) {
            page.getComponents().put(11, new Button
                    (
                            area ?
                                    new CustomItem(Material.WOODEN_AXE, "Selected WorldEdit Area")
                                            .lore("X: " + worldEditLocation.getLocation().getX(), "Y: " + worldEditLocation.getLocation().getY(), "Z: " + worldEditLocation.getLocation().getZ(), "Radius: " + worldEditLocation.getRadius()) :
                                    new CustomItem(Material.WOODEN_AXE, "Selected WorldEdit Location")
                                            .lore("X: " + worldEditLocation.getFirstLocation().getX(), "Y: " + worldEditLocation.getFirstLocation().getY(), "Z: " + worldEditLocation.getFirstLocation().getZ()),
                            new GuiAction() {
                                @Override
                                public void onClick(ActionType actionType, MCGui gui) {
                                    action.selected(new WorldEditPosition(worldEditLocation.getRadius(), worldEditLocation.getLocation(), worldEditLocation.getFirstLocation()), null);
                                }
                            }));
        } else {
            page.getComponents().put(11, new Button
                    (
                            new CustomItem(Material.WOODEN_AXE, Text.chatColor("&cNo WorldEdit Location found"))
                                    .lore("WorldEdit Location not found"),
                            new GuiAction() {
                            }));
        }

        page.getComponents().put(13, new Button(
                new CustomItem(Material.MAP, "Current Location")
                        .lore("X: " + player.getLocation().toBlockLocation().getX(), "Y: " + player.getLocation().toBlockLocation().getY(), "Z: " + player.getLocation().toBlockLocation().getZ()),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        if (area)
                            DefaultGuis.generateDoubleGui(plugin, player, 5, new GuiAction() {
                                @Override
                                public void valueConfirmed(String value, MCGui gui) {
                                    try {
                                        int radius = Integer.parseInt(value);
                                        action.selected(null, new PlayerPosition(player.getLocation().toBlockLocation()));
                                    } catch (Exception e) {
                                        player.sendMessage(Text.chatColor("&cInvalid radius"));
                                        generatePage(area);
                                    }
                                }

                                @Override
                                public void valueDenied(MCGui gui) {
                                    action.goBack(gui);
                                }
                            });
                        else
                            action.selected(null, new PlayerPosition(player.getLocation().toBlockLocation()));
                    }
                }
        ));

        page.getComponents().put(25, new Button(
                new CustomItem(Material.BARRIER, "Back"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        action.goBack(mcGui);
                    }
                }
        ));

        this.mcGui = new MCGui(plugin, player, page);
        this.mcGui.open();
    }

    public interface PositionGuiAction {
        void goBack(MCGui gui);

        void selected(WorldEditPosition wordEditPosition, PlayerPosition playerPosition);
    }

    public static class WorldEditPosition {
        private int radius;
        private Location location;

        private Location firstLocation;

        public WorldEditPosition(int radius, Location location, Location firstLocation) {
            this.radius = radius;
            this.location = location;
            this.firstLocation = firstLocation;
        }

        public int getRadius() {
            return radius;
        }

        public void setRadius(int radius) {
            this.radius = radius;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Location getFirstLocation() {
            return firstLocation;
        }

        public void setFirstLocation(Location firstLocation) {
            this.firstLocation = firstLocation;
        }
    }

    public class PlayerPosition {
        private Location location;


        public PlayerPosition(Location location) {
            this.location = location;
        }

        public Location getLocation() {
            return location;
        }

        public void setLocation(Location location) {
            this.location = location;
        }
    }
}
