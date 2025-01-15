package me.xkyrell.kseconomy.command;

import lombok.Getter;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import java.util.Collections;
import java.util.List;

@Getter
public abstract class AbstractSubCommand extends AbstractExecutable {

    private final int minArg;
    private final int requiredArgsCount;

    protected AbstractSubCommand(@NotNull String name, int minArg, int requiredArgsCount) {
        super(name, Collections.emptyList());

        this.minArg = minArg;
        this.requiredArgsCount = requiredArgsCount;
    }

    protected AbstractSubCommand(@NotNull String name, int requiredArgsCount) {
        this(name, 0, requiredArgsCount);
    }

    public Component getFormattedUsage(String label) {
        return Component.text(label)
                .append(Component.space())
                .append(Component.text(getUsage()));
    }

    @NotNull
    @Override
    public List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public @NotNull String getUsage() {
        return getLabel();
    }
}