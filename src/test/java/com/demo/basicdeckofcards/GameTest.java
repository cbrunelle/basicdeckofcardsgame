package com.demo.basicdeckofcards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    private Game game;

    @BeforeEach
    public void setUp() {
        game = new Game("Test");
    }

    @Test
    void addPlayer() {
        Player newPlayer = new Player("Add");

        assertThat(game.getPlayers()).doesNotContain(newPlayer);
        game.addPlayer(newPlayer);
        assertThat(game.getPlayers()).contains(newPlayer);
    }

    @Test
    void deletePlayerByName() {
        Player newPlayer = new Player("Remove");

        game.addPlayer(newPlayer);
        assertThat(game.getPlayers()).contains(newPlayer);
        game.deletePlayerByName("Remove");
        assertThat(game.getPlayers()).doesNotContain(newPlayer);
    }

    @Test
    void getPlayer() {
        Player newPlayer = new Player("find");

        game.addPlayer(newPlayer);
        assertThat(game.getPlayer("find")).isEqualTo(newPlayer);
    }

    @Test
    void getPlayer_shouldThrowNotFound() {
        assertThatExceptionOfType(PlayerNotFoundException.class)
                .isThrownBy(() -> {
                    game.getPlayer("find");
                });
    }
}