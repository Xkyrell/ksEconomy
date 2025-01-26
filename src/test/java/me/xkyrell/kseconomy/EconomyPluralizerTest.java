package me.xkyrell.kseconomy;

import me.xkyrell.kseconomy.economy.EconomyPluralizer;
import org.junit.jupiter.api.Test;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

public class EconomyPluralizerTest {

    @Test
    void testValidArguments() {
        Function<Double, String> mapper = value -> value == 1
                ? " singular"
                : " plural";

        EconomyPluralizer pluralizer = new EconomyPluralizer(
                "unit", new String[] { "units" }, mapper
        );

        assertEquals("unit", pluralizer.singular());
        assertArrayEquals(new String[] { "units" }, pluralizer.plural());
        assertEquals(" singular", pluralizer.mapper().apply(1.0));
        assertEquals(" plural", pluralizer.mapper().apply(2.0));
    }

    @Test
    void testInvalidArguments() {
        assertInvalidArgument(null, new String[] { "units" }, value -> "");
        assertInvalidArgument("unit", null, value -> "");
        assertInvalidArgument("unit", new String[] {}, value -> "");
        assertInvalidArgument("unit", new String[] { "units" }, null);
    }

    private void assertInvalidArgument(String singular, String[] plural, Function<Double, String> mapper) {
        assertThrows(IllegalArgumentException.class, () ->
                new EconomyPluralizer(singular, plural, mapper)
        );
    }
}
