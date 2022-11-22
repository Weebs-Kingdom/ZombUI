package zombui.zombui.visual.userInterface.gui.generic;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import zombui.zombui.ZombUI;
import zombui.zombui.visual.CustomItem;
import zombui.zombui.visual.Text;
import zombui.zombui.visual.userInterface.ActionType;
import zombui.zombui.visual.userInterface.GuiAction;
import zombui.zombui.visual.userInterface.gui.GuiParameters;
import zombui.zombui.visual.userInterface.gui.MCGui;
import zombui.zombui.visual.userInterface.parts.Button;

public class PositionGui {

    private final ZombUI plugin;

    //GUI
    private final PositionGuiAction action;
    private final String title;
    private final Player player;
    private MCGui mcGui;

    public PositionGui(ZombUI plugin, String title, Player player, PositionGuiAction action) {
        this.plugin = plugin;
        this.title = title;
        this.player = player;
        this.action = action;
    }

    public void generatePage() {
        GuiParameters page = new GuiParameters();
        page.setTitle(title);
        page.setSlots(27);

        boolean foundWorldEditLocation = true;
        Location worldEditLocation = null;
        int radius = -1;
        try {
            LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(player.getName());
            Region region = session.getSelection(session.getSelectionWorld());
            Vector3 center = region.getCenter();
            radius = (int) Math.ceil(region.getMaximumPoint().distance(region.getMinimumPoint()) / 2);
            worldEditLocation = new Location(player.getWorld(), center.getX(), center.getY(), center.getZ());
        } catch (Exception e) {
            foundWorldEditLocation = false;
        }

        if (foundWorldEditLocation) {
            int finalRadius = radius;
            Location finalWorldEditLocation = worldEditLocation;
            page.getComponents().put(11, new Button
                    (
                            new CustomItem(Material.WOODEN_AXE, "Selected Wordedit Location")
                                    .lore("X: " + worldEditLocation.getX(), "Y: " + worldEditLocation.getY(), "Z: " + worldEditLocation.getZ(), "Radius: " + radius),
                            new GuiAction() {
                                @Override
                                public void onClick(ActionType actionType, MCGui gui) {
                                    action.selected(new WorldEditPosition(finalRadius, finalWorldEditLocation), null);
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
                        .lore("X: " + player.getLocation().getX(), "Y: " + player.getLocation().getY(), "Z: " + player.getLocation().getZ()),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        DefaultGuis.generateDoubleGui(plugin, player, 5, new GuiAction() {
                            @Override
                            public void valueConfirmed(String value, MCGui gui) {
                                try {
                                    int radius = Integer.parseInt(value);
                                    action.selected(null, new PlayerPosition(radius, player.getLocation()));
                                } catch (Exception e) {
                                    player.sendMessage(Text.chatColor("&cInvalid radius"));
                                    generatePage();
                                }
                            }

                            @Override
                            public void valueDenied(MCGui gui) {
                                generatePage();
                            }
                        });
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

    public class WorldEditPosition {
        private int radius;
        private Location location;

        public WorldEditPosition(int radius, Location location) {
            this.radius = radius;
            this.location = location;
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
    }

    public class PlayerPosition {
        private int radius;
        private Location location;

        public PlayerPosition(int radius, Location location) {
            this.radius = radius;
            this.location = location;
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
    }
}
