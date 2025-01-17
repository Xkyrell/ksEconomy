package me.xkyrell.kseconomy.economy;

import java.util.Collection;
import java.util.Optional;

public interface EconomyResolver {

    Optional<Economy> resolve(String name);

    Collection<Economy> getEconomies();

}
