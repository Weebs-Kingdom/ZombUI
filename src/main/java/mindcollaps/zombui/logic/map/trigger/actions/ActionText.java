package mindcollaps.zombui.logic.map.trigger.actions;

import mindcollaps.zombui.ZombUi;
import mindcollaps.zombui.logic.GamePlayer;
import mindcollaps.zombui.logic.GameSession;
import mindcollaps.zombui.logic.map.enums.TextType;
import mindcollaps.zombui.logic.map.trigger.ZombieTriggerAction;
import mindcollaps.zombui.visual.CustomItem;
import mindcollaps.zombui.visual.Text;
import mindcollaps.zombui.visual.userInterface.ActionType;
import mindcollaps.zombui.visual.userInterface.GuiAction;
import mindcollaps.zombui.visual.userInterface.gui.GuiParameters;
import mindcollaps.zombui.visual.userInterface.gui.MCGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.ColorPickGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.DefaultGuis;
import mindcollaps.zombui.visual.userInterface.gui.generic.SelectorGui;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.CustomGoBack;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.ObjectSelector;
import mindcollaps.zombui.visual.userInterface.gui.generic.interfaces.SelectorAction;
import mindcollaps.zombui.visual.userInterface.parts.Button;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ActionText extends ZombieTriggerAction {

    private static final long serialVersionUID = 42L;
    private String text = "";
    private TextType textType = TextType.CHAT_TEXT;
    private String color = "&f";

    public ActionText(Location location, String name) {
        super(location, name);
    }

    @Override
    public void putEditor(GuiParameters parameters, Player player, CustomGoBack goBack, ZombUi zombUI) {
        parameters.getComponents().put(18, new Button(new CustomItem(Material.PAPER, "Text").lore(text), new GuiAction() {
            @Override
            public void onClick(ActionType actionType, MCGui gui) {
                DefaultGuis.generateStringGui(zombUI, player, text, new GuiAction() {
                    @Override
                    public void valueConfirmed(String value, MCGui gui) {
                        text = value;
                        goBack.goBack();
                    }

                    @Override
                    public void valueDenied(MCGui gui) {
                        goBack.goBack();
                    }
                });
            }
        }));

        parameters.getComponents().put(19, new Button(new CustomItem(Material.FEATHER, "Text type").lore(textType.name()), new GuiAction() {
            @Override
            public void onClick(ActionType actionType, MCGui gui) {
                SelectorGui<TextType> selectorGui = new SelectorGui<>(zombUI, new ObjectSelector<TextType>() {
                    @Override
                    public @NotNull List<TextType> getData() {
                        return List.of(new TextType[]{TextType.CHAT_TEXT, TextType.BROADCAST, TextType.TITLE, TextType.GLOBAL_TITLE});
                    }

                    @Override
                    public CustomItem getItem(TextType o) {
                        switch (o) {
                            case CHAT_TEXT -> {
                                return textType == TextType.CHAT_TEXT ? new CustomItem(Material.FEATHER, "Chat").lore("Selected") : new CustomItem(Material.FEATHER, "Chat");
                            }
                            case BROADCAST -> {
                                return textType == TextType.BROADCAST ? new CustomItem(Material.FEATHER, "Broadcast").lore("Selected") : new CustomItem(Material.FEATHER, "Broadcast");
                            }
                            case TITLE -> {
                                return textType == TextType.TITLE ? new CustomItem(Material.FEATHER, "Title").lore("Selected") : new CustomItem(Material.FEATHER, "Title");
                            }
                            case GLOBAL_TITLE -> {
                                return textType == TextType.GLOBAL_TITLE ? new CustomItem(Material.FEATHER, "Global Title").lore("Selected") : new CustomItem(Material.FEATHER, "Global Title");
                            }
                        }
                        return null;
                    }
                }, new SelectorAction<TextType>() {
                    @Override
                    public void selected(TextType o) {
                        textType = o;
                        goBack.goBack();
                    }

                    @Override
                    public void goBack(MCGui gui) {
                        goBack.goBack();
                    }
                }, player, "Select Text Type", false);

                selectorGui.generatePage(0);
            }
        }));

        parameters.getComponents().put(20,
                new Button(new CustomItem(Material.WHITE_DYE, "Color").lore(color.toString()),
                        new GuiAction() {
                            @Override
                            public void onClick(ActionType actionType, MCGui gui) {
                                ColorPickGui gui1 = new ColorPickGui(zombUI, color, player, new SelectorAction<String>() {
                                    @Override
                                    public void selected(String o) {
                                        color = o;
                                        goBack.goBack();
                                    }

                                    @Override
                                    public void goBack(MCGui gui) {
                                        goBack.goBack();
                                    }
                                });

                                gui1.generate();
                            }
                        }));
    }

    @Override
    public String getActionName() {
        return "Text";
    }

    @Override
    public void action(Player player, GameSession gameSession, ZombUi zombUI) {
        switch (textType) {
            case CHAT_TEXT -> player.sendMessage(text);

            case BROADCAST -> {
                for (GamePlayer gamePlayer : gameSession.getPlayers()) {
                    gamePlayer.getPlayer().sendMessage(text);
                }
            }
            case TITLE ->
                    player.showTitle(Title.title(Component.text(text).color(TextColor.color(0x00FF00)), Component.empty()));

            case GLOBAL_TITLE -> {
                for (GamePlayer gamePlayer : gameSession.getPlayers()) {
                    gamePlayer.getPlayer().showTitle(Title.title(Component.text(text).color(TextColor.color(0x00FF00)), Component.empty()));
                }
            }
        }
    }
}
