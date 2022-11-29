package mindcollaps.zombui.visual.userInterface.gui.generic.interfaces;

import mindcollaps.zombui.visual.CustomItem;
import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * This interface should provide a value that is shown in the GUI as selector text
 */
public interface ObjectSelector<E> {
    @NotNull List<E> getData();

    CustomItem getItem(E o);
}
