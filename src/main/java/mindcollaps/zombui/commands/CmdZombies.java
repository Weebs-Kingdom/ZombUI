package mindcollaps.zombui.commands;

import mindcollaps.zombui.logic.GameSession;
import mindcollaps.zombui.logic.map.ZombieMap;
import mindcollaps.zombui.logic.map.spawner.ZombieSpawnPoint;
import mindcollaps.zombui.logic.map.trigger.LocationTrigger;
import mindcollaps.zombui.logic.map.trigger.RedstoneTrigger;
import mindcollaps.zombui.logic.map.trigger.ZombieTrigger;
import mindcollaps.zombui.logic.map.trigger.ZombieTriggerAction;
import mindcollaps.zombui.logic.map.trigger.actions.ActionDoor;
import mindcollaps.zombui.logic.map.trigger.actions.ActionRedstoneSignal;
import mindcollaps.zombui.util.ZombieUtil;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.CustomGoBack;
import mindcollaps.zombui.visual.userInterface.gui.generic.DefaultGuis;
import mindcollaps.zombui.visual.userInterface.gui.generic.PositionGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.SelectorGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.ObjectSelector;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.SelectorAction;
import mindcollaps.zombui.visual.userInterface.parts.Button;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import mindcollaps.zombui.ZombUi;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class CmdZombies extends ZombieCommand {
    public CmdZombies(ZombUi zombUI) {
        super("zombie", zombUI);

        zombUI.getCommand(getInvoke()).setExecutor(this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            openGui(player, generateZombiesGui(player));
        } else {
            sender.sendMessage("This command be called by players only!");
        }

        return true;
    }

    private GuiParameters generateZombiesGui(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());
        if (map == null) {
            map = new ZombieMap(player.getWorld().getSpawnLocation());
            getZombUI().createZombieMap(map, player.getWorld());
        }

        GuiParameters parameters = new GuiParameters();
        parameters.setTitle("Zombies");
        GameSession session = getZombUI().getGameSession(player.getWorld());
        ZombieMap finalMap = map;
        parameters.getComponents().put(1,
                new Button(
                        session == null ? new CustomItem(Material.REDSTONE_BLOCK, "Test Game").lore("Start") : new CustomItem(Material.REDSTONE_BLOCK, "Test Game").lore("Stop"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                GameSession session = getZombUI().getGameSession(player.getWorld());
                                if (session == null) {
                                    session = new GameSession(finalMap);
                                    getZombUI().createGameSession(session, player.getWorld());
                                    session.startGame(getZombUI());
                                    player.sendMessage("Starting test game without initializing the queue!");
                                } else {
                                    session.stopGame(getZombUI());
                                    player.sendMessage("Stopping test game!");
                                }
                            }
                        }
                ));

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

        parameters.getComponents().put(22,
                new Button(
                        new CustomItem(Material.COMMAND_BLOCK, "Action"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                openActionSelection(player);
                            }
                        }
                ));

        return parameters;
    }

    private void openZombieSpawnerSelection(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<ZombieSpawnPoint> selectorGui = new SelectorGui<ZombieSpawnPoint>(
                getZombUI(),
                new ObjectSelector<ZombieSpawnPoint>() {
                    @Override
                    public List<ZombieSpawnPoint> getData() {
                        return map.getZombieSpawnPoints();
                    }

                    @Override
                    public CustomItem getItem(ZombieSpawnPoint o) {
                        return new CustomItem(Material.ZOMBIE_HEAD, o.getName());
                    }
                },
                new SelectorAction<ZombieSpawnPoint>() {
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
        parameters.setSlots(36);

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
                                switch (actionType) {
                                    case RIGHT -> player.teleport(zombieSpawnPoint.getSpawnerLocation());
                                    default -> {
                                        PositionGui positionGui = new PositionGui(getZombUI(), "Select Position", player, new PositionGui.PositionGuiAction() {
                                            @Override
                                            public void goBack(MCGui gui) {
                                                openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                            }

                                            @Override
                                            public void selected(PositionGui.WorldEditPosition wordEditPosition, PositionGui.PlayerPosition playerPosition) {
                                                if (wordEditPosition == null) {
                                                    zombieSpawnPoint.setSpawnerLocation(playerPosition.getLocation());
                                                    openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                                } else {
                                                    zombieSpawnPoint.setSpawnRadius(wordEditPosition.getRadius());
                                                    zombieSpawnPoint.setSpawnerLocation(wordEditPosition.getLocation());
                                                    openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                                }
                                            }
                                        });
                                        positionGui.generatePage(true);
                                    }
                                }

                            }
                        }
                )
        );

        parameters.getComponents().put(22,
                new Button(
                        new CustomItem(Material.TARGET, "Radius").lore("R: " + zombieSpawnPoint.getSpawnRadius()),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                SelectorGui<Integer> selectorGui = new SelectorGui<>(
                                        getZombUI(),
                                        new ObjectSelector<Integer>() {
                                            @Override
                                            public List<Integer> getData() {
                                                return List.of(new Integer[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10});
                                            }

                                            @Override
                                            public CustomItem getItem(Integer o) {
                                                return o == zombieSpawnPoint.getSpawnRadius() ? new CustomItem(Material.REDSTONE_LAMP, "R: " + o).lore("Selected") : new CustomItem(Material.TARGET, "R: " + o);
                                            }
                                        },
                                        new SelectorAction<>() {
                                            @Override
                                            public void selected(Integer o) {
                                                zombieSpawnPoint.setSpawnRadius(o);
                                                openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
                                            }

                                            @Override
                                            public void goBack(MCGui gui) {
                                                openGui(player, generateZombieSpawnPointEditor(player, zombieSpawnPoint));
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

        parameters.getComponents().put(35, new Button(
                new CustomItem(Material.BARRIER, "Back"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        openActionSelection(player);
                    }
                }
        ));

        parameters.getComponents().put(34, new Button(
                new CustomItem(Material.LAVA_BUCKET, "Remove"),
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
                new ObjectSelector<>() {
                    @Override
                    public @NotNull List<ZombieTrigger> getData() {
                        return map.getZombieTriggers();
                    }

                    @Override
                    public CustomItem getItem(ZombieTrigger o) {
                        return new CustomItem(Material.OBSERVER, o.getName());
                    }
                },
                new SelectorAction<>() {
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

    private void openActionSelection(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<ZombieTriggerAction> selectorGui = new SelectorGui<>(
                getZombUI(),
                new ObjectSelector<>() {
                    @Override
                    public @NotNull List<ZombieTriggerAction> getData() {
                        return map.getZombieTriggerActions();
                    }

                    @Override
                    public CustomItem getItem(ZombieTriggerAction o) {
                        return new CustomItem(Material.COMMAND_BLOCK, o.getName());
                    }
                },
                new SelectorAction<ZombieTriggerAction>() {
                    @Override
                    public void selected(ZombieTriggerAction o) {
                        openGui(player, generateActionEditor(player, o));
                    }

                    @Override
                    public void goBack(MCGui gui) {
                        openGui(player, generateZombiesGui(player));
                    }

                    @Override
                    public void create(MCGui gui) {
                        generateCreateAction(player);
                    }
                },
                player,
                "Action",
                true
        );

        selectorGui.generatePage(0);
    }

    private void generateCreateTrigger(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<String> selectorGui = new SelectorGui<>(
                getZombUI(),
                new ObjectSelector<String>() {
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
                new SelectorAction<String>() {
                    @Override
                    public void selected(String o) {
                        ZombieTrigger trigger = null;
                        switch (o) {
                            case "Location":
                                trigger = new LocationTrigger(ZombieTrigger.TriggerType.ONCE, player.getLocation(), "Location Trigger");
                                break;

                            case "Redstone":
                                trigger = new RedstoneTrigger(ZombieTrigger.TriggerType.ONCE, player.getLocation(), "Redstone Trigger");
                                break;
                        }

                        if (trigger != null) {
                            map.getZombieTriggers().add(trigger);
                            openGui(player, generateTriggerEditor(player, trigger));
                        } else {
                            player.sendMessage("The selected type is invalid!");
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

        selectorGui.generatePage(0);
    }

    private GuiParameters generateTriggerEditor(Player player, ZombieTrigger zombieTrigger) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        GuiParameters parameters = new GuiParameters();
        parameters.setTitle("Trigger Editor");
        parameters.setSlots(36);

        parameters.getComponents().put(11, new Button(
                new CustomItem(Material.NAME_TAG, "Name").lore(zombieTrigger.getName()),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        DefaultGuis.generateStringGui(getZombUI(), player, zombieTrigger.getName(), new GuiAction() {
                            @Override
                            public void valueConfirmed(String value, MCGui gui) {
                                zombieTrigger.setName(value);
                                openGui(player, generateTriggerEditor(player, zombieTrigger));
                            }

                            @Override
                            public void valueDenied(MCGui gui) {
                                openGui(player, generateTriggerEditor(player, zombieTrigger));
                            }
                        });
                    }
                }
        ));

        parameters.getComponents().put(12,
                new Button(
                        new CustomItem(Material.MAP, "Location")
                                .lore("Location: " + ZombieUtil.getLocationString(zombieTrigger.getLocation()),
                                        "LMB: Edit",
                                        "RMB: Teleport"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                switch (actionType) {
                                    case RIGHT -> player.teleport(zombieTrigger.getLocation());
                                    default -> {
                                        PositionGui positionGui = new PositionGui(getZombUI(), "Select Position", player, new PositionGui.PositionGuiAction() {
                                            @Override
                                            public void goBack(MCGui gui) {
                                                openGui(player, generateTriggerEditor(player, zombieTrigger));
                                            }

                                            @Override
                                            public void selected(PositionGui.WorldEditPosition wordEditPosition, PositionGui.PlayerPosition playerPosition) {
                                                if (wordEditPosition == null) {
                                                    zombieTrigger.setLocation(playerPosition.getLocation());
                                                    openGui(player, generateTriggerEditor(player, zombieTrigger));
                                                } else {
                                                    zombieTrigger.setLocation(wordEditPosition.getFirstLocation());
                                                    openGui(player, generateTriggerEditor(player, zombieTrigger));
                                                }
                                            }
                                        });
                                        positionGui.generatePage(false);
                                    }
                                }

                            }
                        }
                )
        );

        CustomItem triggerItem = null;

        if (zombieTrigger.getType() == ZombieTrigger.TriggerType.ONCE) {
            triggerItem = new CustomItem(Material.COMMAND_BLOCK, "Trigger Type").lore(zombieTrigger.getType().toString());
        } else {
            triggerItem = new CustomItem(Material.REPEATING_COMMAND_BLOCK, "Trigger Type").lore(zombieTrigger.getType().toString());
        }

        parameters.getComponents().put(13, new Button(
                triggerItem,
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        SelectorGui<ZombieTrigger.TriggerType> triggerTypeSelectorGui = new SelectorGui<>(
                                getZombUI(),
                                new ObjectSelector<ZombieTrigger.TriggerType>() {
                                    @Override
                                    public @NotNull List getData() {
                                        return List.of(new ZombieTrigger.TriggerType[]{
                                                ZombieTrigger.TriggerType.ONCE,
                                                ZombieTrigger.TriggerType.REPEAT,
                                                ZombieTrigger.TriggerType.PER_PLAYER
                                        });
                                    }

                                    @Override
                                    public CustomItem getItem(ZombieTrigger.TriggerType o) {
                                        switch (o) {
                                            case ONCE -> {
                                                return new CustomItem(Material.COMMAND_BLOCK, "Once").lore("Activates a trigger once");
                                            }
                                            case REPEAT -> {
                                                return new CustomItem(Material.REPEATING_COMMAND_BLOCK, "Repeat").lore("Activates trigger every time");
                                            }
                                            case PER_PLAYER -> {
                                                return new CustomItem(Material.CHAIN_COMMAND_BLOCK, "Per player").lore("Activates trigger for each player once");
                                            }

                                            default -> {
                                                getZombUI().getLogger().log(Level.SEVERE, "A Trigger type was not registered in the gui!");
                                                return new CustomItem(Material.BARRIER, "Type not found");
                                            }

                                        }
                                    }
                                },
                                new SelectorAction<ZombieTrigger.TriggerType>() {
                                    @Override
                                    public void selected(ZombieTrigger.TriggerType o) {
                                        zombieTrigger.setType(o);
                                        openGui(player, generateTriggerEditor(player, zombieTrigger));
                                    }

                                    @Override
                                    public void goBack(MCGui gui) {
                                        openGui(player, generateTriggerEditor(player, zombieTrigger));
                                    }
                                },
                                player,
                                "Select Trigger Type",
                                false
                        );

                        triggerTypeSelectorGui.generatePage(0);
                    }
                }
        ));

        parameters.getComponents().put(15, new Button(
                new CustomItem(Material.COMMAND_BLOCK, "Actions"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {

                        //TODO: put in extra function to recall it in selected()
                        SelectorGui<ZombieTriggerAction> actionSelectorGui = new SelectorGui<>(
                                getZombUI(),
                                new ObjectSelector<ZombieTriggerAction>() {
                                    @Override
                                    public @NotNull List<ZombieTriggerAction> getData() {
                                        return map.getZombieTriggerActions();
                                    }

                                    @Override
                                    public CustomItem getItem(ZombieTriggerAction o) {
                                        Material actionMaterial = null;
                                        String lore = "";
                                        if (List.of(zombieTrigger.getActions()).contains(o)) {
                                            actionMaterial = Material.GREEN_CONCRETE;
                                            lore = "Click to deselect";
                                        } else {
                                            actionMaterial = Material.COMMAND_BLOCK;
                                            lore = "Click to select";
                                        }

                                        return new CustomItem(actionMaterial, o.getName())
                                                .lore(lore,
                                                        "Action: " + o.getActionName(),
                                                        "Location: " + ZombieUtil.getLocationString(o.getLocation()));
                                    }
                                },
                                new SelectorAction<ZombieTriggerAction>() {
                                    @Override
                                    public void selected(ZombieTriggerAction o) {
                                        //If it contains the action, remove it
                                        if (List.of(zombieTrigger.getActions()).contains(o)) {
                                            ArrayList<ZombieTriggerAction> actions = new java.util.ArrayList<>(List.of(zombieTrigger.getActions()));
                                            actions.remove(o);
                                            zombieTrigger.setActions(actions.toArray(new ZombieTriggerAction[0]));
                                        } else {
                                            ArrayList<ZombieTriggerAction> actions = new java.util.ArrayList<>(List.of(zombieTrigger.getActions()));
                                            actions.add(o);
                                            zombieTrigger.setActions(actions.toArray(new ZombieTriggerAction[0]));
                                        }

                                        openGui(player, generateTriggerEditor(player, zombieTrigger));
                                    }

                                    @Override
                                    public void create(MCGui gui) {
                                        generateCreateAction(player);
                                    }

                                    @Override
                                    public void goBack(MCGui gui) {
                                        openGui(player, generateTriggerEditor(player, zombieTrigger));
                                    }
                                },
                                player,
                                "Select Actions",
                                true
                        );

                        actionSelectorGui.generatePage(0);
                    }
                }
        ));

        parameters.getComponents().put(34, new Button(
                new CustomItem(Material.LAVA_BUCKET, "Remove"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        map.getZombieTriggers().remove(zombieTrigger);
                        openTriggerSelection(player);
                    }
                }
        ));

        parameters.getComponents().put(35, new Button(
                new CustomItem(Material.BARRIER, "Back"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        openTriggerSelection(player);
                    }
                }
        ));

        zombieTrigger.putEditor(parameters, player, new CustomGoBack() {
                    @Override
                    public void goBack() {
                        openGui(player, generateTriggerEditor(player, zombieTrigger));
                    }
                },
                getZombUI());

        return parameters;
    }

    private void generateCreateAction(Player player) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        SelectorGui<String> selectorGui = new SelectorGui<>(
                getZombUI(),
                new ObjectSelector<String>() {
                    @Override
                    public @NotNull List<String> getData() {
                        return List.of(new String[]{
                                "Door",
                                "Redstone"
                        });
                    }

                    @Override
                    public CustomItem getItem(String o) {
                        switch (o) {
                            //TODO: add more triggers
                            case "Door":
                                return new CustomItem(Material.IRON_DOOR, "Door");
                            case "Redstone":
                                return new CustomItem(Material.REDSTONE, "Redstone");
                        }

                        getZombUI().getLogger().log(Level.SEVERE, "Unknown action type: " + o);
                        return new CustomItem(Material.BARRIER, "Unknown");
                    }
                },
                new SelectorAction<>() {
                    @Override
                    public void selected(String o) {
                        ZombieTriggerAction action = null;
                        switch (o) {
                            case "Door":
                                action = new ActionDoor(player.getLocation(), "Door Action");
                                break;

                            case "Redstone":
                                action = new ActionRedstoneSignal(player.getLocation(), "Redstone Action");
                                break;
                        }

                        if (action != null) {
                            map.getZombieTriggerActions().add(action);
                            openGui(player, generateActionEditor(player, action));
                        } else {
                            player.sendMessage("The selected type is invalid!");
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

        selectorGui.generatePage(0);
    }

    private GuiParameters generateActionEditor(Player player, ZombieTriggerAction zombieAction) {
        ZombieMap map = getZombUI().getZombieMap(player.getWorld());

        GuiParameters parameters = new GuiParameters();
        parameters.setTitle("Action Editor");
        parameters.setSlots(36);

        parameters.getComponents().put(11, new Button(
                new CustomItem(Material.NAME_TAG, "Name").lore(zombieAction.getName()),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        DefaultGuis.generateStringGui(getZombUI(), player, zombieAction.getName(), new GuiAction() {
                            @Override
                            public void valueConfirmed(String value, MCGui gui) {
                                zombieAction.setName(value);
                                openGui(player, generateActionEditor(player, zombieAction));
                            }

                            @Override
                            public void valueDenied(MCGui gui) {
                                openGui(player, generateActionEditor(player, zombieAction));
                            }
                        });
                    }
                }
        ));

        parameters.getComponents().put(13,
                new Button(
                        new CustomItem(Material.MAP, "Location").lore("Location:" + ZombieUtil.getLocationString(zombieAction.getLocation()), "LMB: Edit", "RMB: Teleport"),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                switch (actionType) {
                                    case RIGHT -> player.teleport(zombieAction.getLocation());
                                    default -> {
                                        PositionGui positionGui = new PositionGui(getZombUI(), "Select Position", player, new PositionGui.PositionGuiAction() {
                                            @Override
                                            public void goBack(MCGui gui) {
                                                openGui(player, generateActionEditor(player, zombieAction));
                                            }

                                            @Override
                                            public void selected(PositionGui.WorldEditPosition wordEditPosition, PositionGui.PlayerPosition playerPosition) {
                                                if (wordEditPosition == null) {
                                                    zombieAction.setLocation(playerPosition.getLocation());
                                                    openGui(player, generateActionEditor(player, zombieAction));
                                                } else {
                                                    zombieAction.setLocation(wordEditPosition.getFirstLocation());
                                                    openGui(player, generateActionEditor(player, zombieAction));
                                                }
                                            }
                                        });
                                        positionGui.generatePage(false);
                                    }
                                }

                            }
                        }
                )
        );

        parameters.getComponents().put(35, new Button(
                new CustomItem(Material.BARRIER, "Back"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        openActionSelection(player);
                    }
                }
        ));

        parameters.getComponents().put(34, new Button(
                new CustomItem(Material.LAVA_BUCKET, "Remove"),
                new GuiAction() {
                    @Override
                    public void onClick(ActionType actionType, MCGui gui) {
                        map.getZombieTriggerActions().remove(zombieAction);
                        openActionSelection(player);
                    }
                }
        ));

        zombieAction.putEditor(parameters, player, new CustomGoBack() {
                    @Override
                    public void goBack() {
                        openGui(player, generateActionEditor(player, zombieAction));
                    }
                },
                getZombUI());

        return parameters;
    }

    private void openGui(Player player, GuiParameters parameters) {
        new MCGui(getZombUI(), player, parameters).open();
    }
}
