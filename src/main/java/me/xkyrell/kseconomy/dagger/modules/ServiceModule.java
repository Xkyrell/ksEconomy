package me.xkyrell.kseconomy.dagger.modules;

import dagger.Binds;
import dagger.Module;
import me.xkyrell.kseconomy.player.service.PlayerService;
import me.xkyrell.kseconomy.player.service.impl.SimplePlayerService;

@Module
public interface ServiceModule {

    @Binds
    PlayerService playerService(SimplePlayerService __);

}
