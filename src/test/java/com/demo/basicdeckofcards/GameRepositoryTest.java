package com.demo.basicdeckofcards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
class GameRepositoryTest {

    private GameRepository repository;

    @BeforeEach
    void setUp() {
        repository = new GameRepository();
        repository.save(new Game("basicFirstEntry"));
    }

    @Test
    void save() {
        repository.save(new Game("saveEntry"));
        assertThat(repository.findByName("saveEntry").getName()).isEqualTo("saveEntry");
    }

    @Test
    void findByName() {
        repository.save(new Game("testFindByName"));
        Game game = repository.findByName("testFindByName");

        assertThat(game.getName()).isEqualTo("testFindByName");
    }

    @Test
    void deleteByName() {
        Game game = new Game("deleteEntry");
        repository.save(game);
        repository.delete(game);
        assertThat(repository.findByName("deleteEntry")).isNull();
    }
}