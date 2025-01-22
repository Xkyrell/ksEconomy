package me.xkyrell.kseconomy.util;

import lombok.experimental.UtilityClass;
import java.util.Optional;

@UtilityClass
public class Numbers {

    public Optional<Double> parseAmount(String amount) {
        try {
            return Optional.of(Double.parseDouble(amount));
        }
        catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
