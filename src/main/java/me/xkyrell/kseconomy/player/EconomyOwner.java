package me.xkyrell.kseconomy.player;

import lombok.NonNull;
import me.xkyrell.kseconomy.economy.Economy;
import javax.annotation.Nullable;
import java.util.List;

public interface EconomyOwner {

    @Nullable
    Economy getEconomyByName(@NonNull String name);

    @Nullable
    Economy getEconomyBySymbol(@NonNull String symbol);

    List<Economy> getEconomies();

    void setEconomies(List<Economy> economies);

}
