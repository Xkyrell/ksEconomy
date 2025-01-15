package me.xkyrell.kseconomy.player.impl;

import me.xkyrell.kseconomy.player.AbstractEconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import java.util.UUID;

public final class EconomyOfflinePlayer extends AbstractEconomyPlayer<OfflinePlayer> {

    public EconomyOfflinePlayer(UUID uuid) {
        super(uuid);
    }

    @Override
    public OfflinePlayer toBukkitPlayer() {
        return Bukkit.getOfflinePlayer(getUuid());
    }
}
