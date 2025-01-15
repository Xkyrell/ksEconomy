package me.xkyrell.kseconomy.player;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import me.xkyrell.kseconomy.economy.Economy;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public abstract class AbstractEconomyPlayer<S extends OfflinePlayer> implements EconomyPlayer<S> {

    private final UUID uuid;

    @Setter
    private List<Economy> economies;

    @Override
    public @Nullable Economy getEconomyByName(@NonNull String name) {
        Preconditions.checkNotNull(economies, "The economies list cannot be null");
        return economies.stream()
                .filter(economy -> economy.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    @Override
    public @Nullable Economy getEconomyBySymbol(@NonNull String symbol) {
        Preconditions.checkNotNull(economies, "The economies list cannot be null");
        return economies.stream()
                .filter(economy -> economy.getSymbol() == symbol)
                .findFirst()
                .orElse(null);
    }
}
