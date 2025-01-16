package me.xkyrell.kseconomy.util;

import me.xkyrell.kseconomy.economy.hook.Hook;

@FunctionalInterface
public interface UncheckedConsumer<T> {

    void accept(Hook hook) throws Throwable;

}
