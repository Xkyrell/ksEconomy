package me.xkyrell.kseconomy.command;

import lombok.AccessLevel;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.stream.Stream;

import static me.xkyrell.kseconomy.command.AbstractExecutable.AvailabilityType.HANDLER;

@Setter(AccessLevel.PROTECTED)
public abstract class AbstractCommand extends AbstractExecutable {

    private Consumer<CommandSender> unknownArgExecuting = __ -> { };
    private Consumer<CommandSender> unknownSubCommand = __ -> { };

    public AbstractCommand(@NotNull String name, @NotNull List<String> aliases) {
        super(name, aliases);
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!checkAvailabilities(sender, HANDLER)) {
            return true;
        }

        if (args.length == 0) {
            helpPage(sender, label);
            return true;
        }

        Optional<AbstractSubCommand> subCommandOptional = getSubCommands().stream()
                .filter(subCommand -> isSubCommand(subCommand, args, String::equals))
                .findFirst();

        if (subCommandOptional.isEmpty()) {
            unknownSubCommand.accept(sender);
            return true;
        }

        AbstractSubCommand subCommand = subCommandOptional.get();
        if (!subCommand.checkAvailabilities(sender, HANDLER)) {
            return true;
        }

        if (args.length < subCommand.getRequiredArgsCount()) {
            unknownArgExecuting.accept(sender);
            return true;
        }
        return subCommand.execute(sender, label, args);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) throws IllegalArgumentException {
        return getSubCommands().stream()
                .filter(subCommand -> subCommand.checkAvailabilities(sender) && checkAvailabilities(sender))
                .filter(subCommand -> (args.length == 0) || isSubCommand(subCommand, args, String::startsWith))
                .flatMap(subCommand -> (args.length <= 1)
                        ? Stream.of(subCommand.getLabel())
                        : subCommand.tabComplete(sender, label, args).stream()
                )
                .toList();
    }

    private boolean isSubCommand(AbstractSubCommand subCommand, String[] args, BiPredicate<String, String> comparison) {
        String label = subCommand.getLabel().toLowerCase();
        int index = subCommand.getMinArg();

        if (index >= args.length) {
            return false;
        }

        return comparison.test(label, args[index].toLowerCase());
    }

    protected void helpPage(CommandSender sender, String currentLabel) {
        String currentSyntax = ("/").concat(currentLabel);
        Component spacer = Component.space();
        sender.sendMessage(spacer);

        getSubCommands().stream()
                .map(subCommand -> spacer
                        .append(subCommand.getFormattedUsage(currentSyntax))
                        .color(NamedTextColor.RED)
                )
                .forEach(sender::sendMessage);

        sender.sendMessage(spacer);
    }

    public void unregister(Plugin plugin) {
        CommandMap commandMap = plugin.getServer().getCommandMap();
        Map<String, Command> knownCommands = commandMap.getKnownCommands();
        knownCommands.entrySet().removeIf(entry -> entry.getValue().equals(this));
        unregister(commandMap);
    }

    protected Set<AbstractSubCommand> getSubCommands() {
        return Collections.emptySet();
    }
}
