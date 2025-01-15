package me.xkyrell.kseconomy.player.impl;

import me.xkyrell.kseconomy.player.AbstractEconomyPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import java.util.UUID;

public final class EconomyOnlinePlayer extends AbstractEconomyPlayer<Player> {

    public EconomyOnlinePlayer(UUID uuid) {
        super(uuid);
    }

    @Override
    public Player toBukkitPlayer() {
        return Bukkit.getPlayer(getUuid());
    }

    @Override
    public boolean isOnline() {
        return true;
    }
}
