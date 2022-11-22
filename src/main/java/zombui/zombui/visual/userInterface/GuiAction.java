package zombui.zombui.visual.userInterface;

import zombui.zombui.visual.userInterface.gui.MCGui;

public interface GuiAction {

    default void onClick(ActionType actionType, MCGui gui) {
    }

    default void valueConfirmed(String value, MCGui gui) {
    }

    default void valueDenied(MCGui gui) {
    }
}
