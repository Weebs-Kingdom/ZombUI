package zombui.zombui.commands;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.regions.Region;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import zombui.zombui.ZombUI;
import zombui.zombui.logic.map.ZombieMap;
import zombui.zombui.logic.map.spawner.ZombieSpawnPoint;
import zombui.zombui.logic.map.trigger.LocationTrigger;
import zombui.zombui.logic.map.trigger.RedstoneTrigger;
import zombui.zombui.logic.map.trigger.ZombieTrigger;
import zombui.zombui.visual.CustomItem;
import zombui.zombui.visual.userInterface.ActionType;
import zombui.zombui.visual.userInterface.GuiAction;
import zombui.zombui.visual.userInterface.gui.GuiParameters;
import zombui.zombui.visual.userInterface.gui.MCGui;
import zombui.zombui.visual.userInterface.gui.generic.PositionGui;
import zombui.zombui.visual.userInterface.gui.generic.SelectorGui;
import zombui.zombui.visual.userInterface.gui.generic.DefaultGuis;
import zombui.zombui.visual.userInterface.parts.Button;

import java.util.List;
import java.util.logging.Level;

public class CmdZombies extends ZombieCommand {
    public CmdZombies(@NotNull String name, ZombUI zombUI) {
        super(name, zombUI);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        return false;
    }

    private GuiParameters generateZombiesGui(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());
        if (map == null) {
            map = new ZombieMap(player.getWorld().getSpawnLocation());
            getZombUI().createZombieMap(map, player.getWorld());
        }

        GuiParameters parameters = new GuiParameters();
        parameters.setTitle("Zombies");

        parameters.getComponents().put(12,
                new Button(
                        new CustomItem(Material.ZOMBIE_HEAD, "Zombie Spawner"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                openZombieSpawnerSelection(player);
                            }
                        }));

        parameters.getComponents().put(14,
                new Button(
                        new CustomItem(Material.OBSERVER, "Trigger"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                openTriggerSelection(player);
                            }
                        }
                ));

        return parameters;
    }

    private void openZombieSpawnerSelection(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<ZombieSpawnPoint> selectorGui = new SelectorGui<ZombieSpawnPoint>(
                getZombUI(),
                new SelectorGui.ObjectSelector<ZombieSpawnPoint>() {
                    @Override
                    public List<ZombieSpawnPoint> getData() {
                        return map.getZombieSpawnPoints();
                    }

                    @Override
                    public CustomItem getItem(ZombieSpawnPoint o) {
                        return new CustomItem(Material.ZOMBIE_HEAD, o.getName());
                    }
                },
                new SelectorGui.SelectorAction<ZombieSpawnPoint>() {
                    @Override
                    public void selected(ZombieSpawnPoint o) {
                        openGui(player, generateZombieSpawnPointEditor(player, o));
                    }

                    @Override
                    public void goBack(MCGui gui) {
                        openGui(player, generateZombiesGui(player));
                    }

                    @Override
                    public void create(MCGui gui) {
                        ZombieSpawnPoint o = new ZombieSpawnPoint("Zombie Spawn Point", player.getLocation());
                        map.getZombieSpawnPoints().add(o);
                        openGui(player, generateZombieSpawnPointEditor(player, o));
                    }
                },
                player,
                "Zombie Spawner",
                true
        );

        selectorGui.generatePage(0);
    }

    private GuiParameters generateZombieSpawnPointEditor(Player player, ZombieSpawnPoint zombieSpawnPoint) {
        GuiParameters parameters = new GuiParameters();
        parameters.setTitle("Zombie Spawner");

        parameters.getComponents().put(11,
                new Button(
                        new CustomItem(Material.NAME_TAG, "Name").lore(zombieSpawnPoint.getName()),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                new DefaultGuis().generateStringGui(getZombUI(), player, zombieSpawnPoint.getName(), new GuiAction() {
                                    @Override
                                    public void valueConfirmed(String value, MCGui gui) {
                                        zombieSpawnPoint.setName(value);
                                        openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                    }

                                    @Override
                                    public void valueDenied(MCGui gui) {
                                        openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                    }
                                });
                            }
                        }
                ));


        parameters.getComponents().put(12,
                new Button(
                        new CustomItem(Material.MAP, "Location").lore(zombieSpawnPoint.getSpawnerLocation().toString(), "LMB: Edit", "RMB: Teleport"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                switch (actionType){
                                    case RIGHT ->
                                        player.teleport(zombieSpawnPoint.getSpawnerLocation());
                                    default -> {
                                        PositionGui positionGui = new PositionGui(getZombUI(), "Select Position", player, new PositionGui.PositionGuiAction() {
                                            @Override
                                            public void goBack(MCGui gui) {
                                                openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                            }

                                            @Override
                                            public void selected(PositionGui.WorldEditPosition wordEditPosition, PositionGui.PlayerPosition playerPosition) {
                                                if (wordEditPosition == null) {
                                                    zombieSpawnPoint.setSpawnRadius(playerPosition.getRadius());
                                                    zombieSpawnPoint.setSpawnerLocation(playerPosition.getLocation());
                                                } else {
                                                    zombieSpawnPoint.setSpawnRadius(wordEditPosition.getRadius());
                                                    zombieSpawnPoint.setSpawnerLocation(wordEditPosition.getLocation());
                                                }
                                            }
                                        });
                                        positionGui.generatePage();
                                    }
                                }

                            }
                        }
                )
        );

        parameters.getComponents().put(21, new Button(
                new CustomItem(Material.BARRIER, "Remove"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        getZombUI().getZombieMap(player.getWorld()).getZombieSpawnPoints().remove(zombieSpawnPoint);
                        openGui(player, generateZombiesGui(player));
                    }
                }
        ));

        return parameters;
    }

    private void openTriggerSelection(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<ZombieTrigger> selectorGui = new SelectorGui<>(
                getZombUI(),
                new SelectorGui.ObjectSelector<ZombieTrigger>() {
                    @Override
                    public @NotNull List<ZombieTrigger> getData() {
                        return map.getZombieTriggers();
                    }

                    @Override
                    public CustomItem getItem(ZombieTrigger o) {
                        return new CustomItem(Material.OBSERVER, o.getName());
                    }
                },
                new SelectorGui.SelectorAction<ZombieTrigger>() {
                    @Override
                    public void selected(ZombieTrigger o) {
                        openGui(player, generateTriggerEditor(player, o));
                    }

                    @Override
                    public void goBack(MCGui gui) {
                        openGui(player, generateZombiesGui(player));
                    }

                    @Override
                    public void create(MCGui gui) {
                        generateCreateTrigger(player);
                    }
                },
                player,
                "Trigger",
                true
        );

        selectorGui.generatePage(0);
    }

    private void generateCreateTrigger(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<String> selectorGui = new SelectorGui<>(
                getZombUI(),
                new SelectorGui.ObjectSelector<String>() {
                    @Override
                    public @NotNull List<String> getData() {
                        return List.of(new String[]{
                                "Location",
                                "Redstone"
                        });
                    }

                    @Override
                    public CustomItem getItem(String o) {
                        switch (o) {
                            //TODO: add more triggers
                            case "Location":
                                return new CustomItem(Material.MAP, "Location");
                            case "Redstone":
                                return new CustomItem(Material.REDSTONE, "Redstone");
                        }

                        getZombUI().getLogger().log(Level.SEVERE, "Unknown trigger type: " + o);
                        return new CustomItem(Material.BARRIER, "Unknown");
                    }
                },
                new SelectorGui.SelectorAction<String>() {
                    @Override
                    public void selected(String o) {
                        ZombieTrigger trigger = null;
                        switch (o) {
                            case "Location":
                                trigger = new LocationTrigger(ZombieTrigger.TriggerType.ONCE, player.getLocation(), 5, "Location Trigger");
                                break;

                            case "Redstone":
                                trigger = new RedstoneTrigger(ZombieTrigger.TriggerType.ONCE, player.getLocation(), "Redstone Trigger");
                                break;
                        }

                        if (trigger != null) {
                            map.getZombieTriggers().add(trigger);
                            generateTriggerEditor(player, trigger);
                        }
                    }

                    @Override
                    public void goBack(MCGui gui) {
                        openTriggerSelection(player);
                    }
                },
                player,
                "Create Trigger",
                false
        );


    }

    private GuiParameters generateTriggerEditor(Player player, ZombieTrigger zombieTrigger) {
        GuiParameters parameters = new GuiParameters();
        parameters.setTitle("Trigger Editor");

        return parameters;
    }

    private void openGui(Player player, GuiParameters parameters) {
        new MCGui(getZombUI(), player, parameters).open();
    }

}
