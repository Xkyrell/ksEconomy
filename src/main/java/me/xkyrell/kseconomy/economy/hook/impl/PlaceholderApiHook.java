package me.xkyrell.kseconomy.economy.hook.impl;

import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.xkyrell.kseconomy.economy.Economy;
import me.xkyrell.kseconomy.economy.hook.Hook;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import me.xkyrell.kseconomy.player.service.PlayerService;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.util.List;

@RequiredArgsConstructor
public final class PlaceholderApiHook extends PlaceholderExpansion implements Hook {

    private final PlayerService playerService;

    @Override
    public void register(Plugin plugin) throws Throwable {
        register();
    }

    @Override
    public void unregister(Plugin plugin) throws Throwable {
        unregister();
    }

    @Override
    public @Nullable String onRequest(OfflinePlayer player, @NotNull String params) {
        List<String> parts = List.of(params.split("_"));
        if (parts.size() < 2) {
            return null;
        }

        var cachedPlayer = playerService.getResolver().resolve(player.getUniqueId());
        if (cachedPlayer.isEmpty()) {
            return "Invalid economy player";
        }

        EconomyPlayer<?> economyPlayer = cachedPlayer.get();
        Economy economy = economyPlayer.getEconomyByName(parts.get(parts.size() - 1));
        if (economy == null) {
            return "Invalid economy";
        }

        return adaptRequest(parts, economy);
    }

    private @Nullable String adaptRequest(List<String> parts, Economy economy) {
        return switch (parts.get(0)) {
            case "balance" -> ("formatted").equals(parts.get(1))
                    ? economy.format()
                    : String.valueOf(economy.getBalance());
            case "max_balance" -> String.valueOf(economy.getMaxBalance());
            default -> null;
        };
    }

    @Override
    public @NotNull String getIdentifier() {
        return "kseconomy";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Xkyrell";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }
}
