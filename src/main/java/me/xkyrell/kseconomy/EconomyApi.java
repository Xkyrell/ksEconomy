package me.xkyrell.kseconomy;

import me.xkyrell.kseconomy.economy.EconomyResolver;
import me.xkyrell.kseconomy.player.service.PlayerResolver;
import me.xkyrell.kseconomy.player.service.PlayerService;

public interface EconomyApi {

    PlayerService getPlayerService();

    EconomyResolver getEconomyResolver();

    default PlayerResolver getPlayerResolver() {
        return getPlayerService().getResolver();
    }
}
