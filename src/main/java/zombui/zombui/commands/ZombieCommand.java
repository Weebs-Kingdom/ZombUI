package zombui.zombui.commands;

import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;
import zombui.zombui.ZombUI;

public abstract class ZombieCommand extends Command {

    private ZombUI zombUI;

    public ZombieCommand(@NotNull String name, ZombUI zombUI) {
        super(name);
        this.zombUI = zombUI;
    }

    public ZombUI getZombUI() {
        return zombUI;
    }
}
