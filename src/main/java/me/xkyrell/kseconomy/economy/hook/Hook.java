package me.xkyrell.kseconomy.economy.hook;

import org.bukkit.plugin.Plugin;

public interface Hook {

    void register(Plugin plugin) throws Throwable;

    void unregister(Plugin plugin) throws Throwable;

}
