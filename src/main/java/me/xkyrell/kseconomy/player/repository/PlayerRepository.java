package me.xkyrell.kseconomy.player.repository;

import lombok.NonNull;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface PlayerRepository {

    CompletableFuture<EconomyPlayer<?>> loadPlayer(@NonNull UUID uuid, boolean online);

    CompletableFuture<Void> savePlayer(@NonNull EconomyPlayer<?> player);

    default CompletableFuture<EconomyPlayer<?>> loadPlayer(Player player) {
        return loadPlayer(player.getUniqueId(), true);
    }

    default CompletableFuture<EconomyPlayer<?>> loadPlayer(OfflinePlayer player) {
        return loadPlayer(player.getUniqueId(), false);
    }
}
