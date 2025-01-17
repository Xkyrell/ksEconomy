package me.xkyrell.kseconomy.economy.impl;

import lombok.RequiredArgsConstructor;
import me.xkyrell.kseconomy.economy.Economy;
import me.xkyrell.kseconomy.economy.EconomyResolver;
import java.util.*;

@RequiredArgsConstructor
public class SimpleEconomyResolver implements EconomyResolver {

    private final Map<String, Economy> economies;

    @Override
    public Optional<Economy> resolve(String name) {
        return Optional.ofNullable(economies.get(name));
    }

    @Override
    public Collection<Economy> getEconomies() {
        return economies.values();
    }
}
