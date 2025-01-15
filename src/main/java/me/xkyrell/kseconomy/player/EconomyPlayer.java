package me.xkyrell.kseconomy.player;

import org.bukkit.OfflinePlayer;
import java.util.UUID;

public interface EconomyPlayer<S extends OfflinePlayer> extends EconomyOwner {

    UUID getUuid();

    S toBukkitPlayer();

    default String getName() {
        return toBukkitPlayer().getName();
    }

    default boolean isOnline() {
        return false;
    }
}
