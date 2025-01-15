package me.xkyrell.kseconomy.economy;

import java.util.function.Function;

public record EconomyPluralizer(
        String singular, String[] plural,
        Function<Double, String> mapper
) {
    public EconomyPluralizer {
        if (singular == null || plural == null) {
            throw new IllegalArgumentException("Singular and plural forms cannot be null");
        }

        if (plural.length == 0) {
            throw new IllegalArgumentException("Plural forms array cannot be empty");
        }

        if (mapper == null) {
            throw new IllegalArgumentException("Mapper function cannot be null");
        }
    }
}
