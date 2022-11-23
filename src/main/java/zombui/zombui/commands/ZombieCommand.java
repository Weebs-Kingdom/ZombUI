package zombui.zombui.commands;

import org.bukkit.command.CommandExecutor;
import org.jetbrains.annotations.NotNull;
import zombui.zombui.ZombUi;

public abstract class ZombieCommand implements CommandExecutor {

    private ZombUi zombUI;

    private String invoke;

    public ZombieCommand(@NotNull String invoke, ZombUi zombUI) {
        this.invoke = invoke;
        this.zombUI = zombUI;
    }

    public ZombUi getZombUI() {
        return zombUI;
    }

    public String getInvoke() {
        return invoke;
    }
}
