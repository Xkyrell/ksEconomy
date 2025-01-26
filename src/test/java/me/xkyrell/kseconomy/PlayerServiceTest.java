package me.xkyrell.kseconomy;

import me.xkyrell.kseconomy.economy.*;
import me.xkyrell.kseconomy.economy.impl.SimpleEconomy;
import me.xkyrell.kseconomy.player.EconomyPlayer;
import me.xkyrell.kseconomy.player.impl.*;
import me.xkyrell.kseconomy.player.service.PlayerResolver;
import me.xkyrell.kseconomy.player.service.impl.SimplePlayerService;
import org.bukkit.Server;
import org.bukkit.plugin.Plugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    private Plugin plugin;

    @InjectMocks
    private SimplePlayerService playerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterByName() {
        String playerName = "TestPlayer";
        UUID uuid = UUID.randomUUID();
        when(plugin.getServer()).thenReturn(mock(Server.class));
        when(plugin.getServer().getPlayerUniqueId(playerName)).thenReturn(uuid);

        playerService.register(playerName, EconomyOnlinePlayer.class);

        assertEquals(1, playerService.getResolver().getPlayers().size());
    }

    @Test
    void testRegister() {
        UUID uuid = UUID.randomUUID();
        playerService.register(new EconomyOnlinePlayer(uuid));

        assertEquals(1, playerService.getResolver().getPlayers().size());
    }

    @Test
    void testUnregister() {
        UUID uuid = UUID.randomUUID();
        EconomyPlayer<?> economyPlayer = new EconomyOfflinePlayer(uuid);
        playerService.register(economyPlayer);
        playerService.unregister(economyPlayer.getUuid());

        PlayerResolver resolver = playerService.getResolver();
        var cachedPlayer = resolver.resolve(uuid);
        assertFalse(cachedPlayer.isPresent());
    }

    @Test
    void testResolver() {
        UUID uuid = UUID.randomUUID();
        playerService.register(new EconomyOfflinePlayer(uuid));

        PlayerResolver resolver = playerService.getResolver();
        var cachedPlayer = resolver.resolve(uuid);

        assertTrue(cachedPlayer.isPresent());

        EconomyPlayer<?> player = cachedPlayer.get();
        assertFalse(player.isOnline());
        assertNull(player.getEconomies());

        Economy economy = new SimpleEconomy(
                "TestEconomy", "*", null,
                null, 125, 1000
        );

        List<Economy> economies = List.of(economy);
        player.setEconomies(economies);

        assertNotNull(player.getEconomyBySymbol("*"));
        assertEquals(player.getEconomyBySymbol("*"), economy);
    }
}
