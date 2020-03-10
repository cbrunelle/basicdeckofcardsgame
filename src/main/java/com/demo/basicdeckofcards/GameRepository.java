package com.demo.basicdeckofcards;

import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.HashMap;

@Repository
public class GameRepository {

    private HashMap<String, Game> repository = new HashMap<>();

    public Game findByName(String name) {
        return this.repository.get(name);
    }

    public Game save(Game game) {
        this.repository.put(game.getName(), game);
        return game;
    }

    public void clear() {
        this.repository.clear();
    }

    public Collection<Game> findAll() {
        return this.repository.values();
    }

    public Game delete(Game game) {
        return this.repository.remove(game.getName());
    }
}
