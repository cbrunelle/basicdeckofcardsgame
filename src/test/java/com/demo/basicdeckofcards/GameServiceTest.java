package com.demo.basicdeckofcards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;

@RunWith(SpringRunner.class)
class GameServiceTest {

    private GameService gameService = new GameService();

    private GameRepository gameRepository;

    @BeforeEach
    void setup() {
        gameRepository = new GameRepository();
        gameRepository.save(new Game("basicEntry"));
        gameService.setGameRepository(gameRepository);
    }

    @Test
    void getList_shouldReturnList() {
        assertThat(gameService.getList()).hasSize(1);
    }

    @Test
    void getGameDetails_shouldReturnBasicEntry() throws Exception{
        assertThat(gameService.getGameDetails("basicEntry").getName()).isEqualTo("basicEntry");
    }

    @Test
    void create_shouldReturnAndSaveNewEntry() throws Exception{
        Game newGame = new Game("newEntry");
        assertThat(gameService.create(newGame)).isEqualTo(newGame);
        assertThat(gameService.getList()).hasSize(2);
    }

    @Test
    void deleteByName_shouldDeleteEntry() throws Exception{
        Game game = new Game("deleteEntry");
        gameService.create(game);
        assertThat(gameService.getList()).hasSize(2);
        gameService.deleteByName("deleteEntry");
        assertThat(gameService.getList()).hasSize(1);
    }

    @Test
    void deleteByName_shouldThrowNotFound() throws Exception{
        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> {
                    gameService.deleteByName("deleteNotFound");
                });
    }

    @Test
    public void create_shouldThrowAlreadyExist() {
        assertThatExceptionOfType(GameAlreadyExistsException.class)
                .isThrownBy(() -> {
                    gameService.create(new Game("basicEntry"));
                });
    }

    @Test
    void getGameDetails_shouldReturnNewEntry() throws Exception{
        assertThat(gameService.getGameDetails("basicEntry").getName()).isEqualTo("basicEntry");
    }

    @Test
    public void getGameDetails_shouldThrowNotFound() {
        assertThatExceptionOfType(GameNotFoundException.class)
                .isThrownBy(() -> {
                    gameService.getGameDetails(anyString());
                });
    }
}