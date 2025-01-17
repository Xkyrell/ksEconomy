package me.xkyrell.kseconomy.player.repository;

import lombok.NonNull;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import org.bukkit.entity.Player;
import java.util.concurrent.CompletableFuture;

public interface PlayerRepository {

    CompletableFuture<EconomyPlayer<?>> loadPlayer(@NonNull String playerName);

    CompletableFuture<Void> savePlayer(@NonNull EconomyPlayer<?> player);

    default CompletableFuture<EconomyPlayer<?>> loadPlayer(Player player) {
        return loadPlayer(player.getName());
    }
}
