package me.xkyrell.kseconomy.command;

import com.google.common.base.Preconditions;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.jetbrains.annotations.NotNull;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class AbstractExecutable extends BukkitCommand {

    private static final String DESCRIPTION = "A command provided by ksPlugins.";

    private final Set<AvailabilityNode> nodes = new HashSet<>();

    protected AbstractExecutable(@NotNull String name, @NotNull List<String> aliases) {
        super(name, DESCRIPTION, ("/").concat(name), aliases);
    }

    protected final void addAvailability(Predicate<? super CommandSender> check, Component message) {
        nodes.add(new AvailabilityNode(check, message));
    }

    protected final void addAvailability(Predicate<? super CommandSender> check, Supplier<Component> message) {
        nodes.add(new AvailabilityNode(check, message.get()));
    }

    public final boolean checkAvailabilities(CommandSender sender, AvailabilityType type) {
        return nodes.stream().allMatch(node -> node.check(sender, type));
    }

    public final boolean checkAvailabilities(CommandSender sender) {
        return checkAvailabilities(sender, AvailabilityType.EMPTY);
    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    protected static class TabCompleter {

        private final Multimap<Integer, String> suggestions = ArrayListMultimap.create();

        private int startIndex = 0;

        public static TabCompleter create() {
            return new TabCompleter();
        }

        public TabCompleter from(int startIndex) {
            this.startIndex = startIndex;
            return this;
        }

        public TabCompleter supply(List<String> completions) {
            Preconditions.checkArgument(startIndex >= 0, "Index must be at least 0.");
            suggestions.putAll(++startIndex, completions);
            return this;
        }

        public TabCompleter supply(Supplier<List<String>> completions) {
            return supply(completions.get());
        }

        public List<String> toSuggestions(String[] args) {
            Collection<String> foundSuggestions = suggestions.get(args.length);
            return List.copyOf(foundSuggestions);
        }
    }

    private record AvailabilityNode(Predicate<? super CommandSender> condition, Component message) {

        private boolean check(CommandSender sender, AvailabilityType type) {
            if (condition.test(sender)) {
                type.action.accept(sender, message);
                return false;
            }
            return true;
        }
    }

    @RequiredArgsConstructor
    public enum AvailabilityType {
        EMPTY((__, ___) -> { }),
        HANDLER(Audience::sendMessage);

        private final BiConsumer<CommandSender, Component> action;

    }
}
