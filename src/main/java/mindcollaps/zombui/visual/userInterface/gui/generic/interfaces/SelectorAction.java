package mindcollaps.zombui.visual.userInterface.gui.generic.interfaces;

import mindcollaps.zombui.visual.userInterface.gui.MCGui;

/**
 * This interface should be used to make further steps after a selection has been taken
 */
public interface SelectorAction<E> {
    void selected(E o);

    default void create(MCGui gui) {
    }

    void goBack(MCGui gui);
}
