package me.xkyrell.kseconomy.dagger;

import dagger.Component;
import me.xkyrell.kseconomy.EconomyApi;
import me.xkyrell.kseconomy.command.registry.CommandRegistry;
import me.xkyrell.kseconomy.dagger.modules.*;
import me.xkyrell.kseconomy.database.ConnectionPool;
import me.xkyrell.kseconomy.economy.hook.HookRegistry;
import me.xkyrell.kseconomy.listener.PlayerListener;

@Component(modules = { DefaultModule.class, ServiceModule.class })
public interface EconomyComponent extends EconomyApi {

    ConnectionPool getConnectionPool();

    PlayerListener getPlayerListener();

    CommandRegistry getCommandRegistry();

    HookRegistry getHookRegistry();

}
