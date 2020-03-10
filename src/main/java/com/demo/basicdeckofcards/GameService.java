package com.demo.basicdeckofcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    public void setGameRepository(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public Collection<Game> getList() {
        return this.gameRepository.findAll();
    }

    public Game getGameDetails(String name) throws Exception {
        Game game = this.gameRepository.findByName(name);
        if( game == null) {
            throw new GameNotFoundException();
        }
        return game;
    }

    public Game create(Game newGame) throws Exception {
        if( this.gameRepository.findByName(newGame.getName()) != null) {
            throw new GameAlreadyExistsException();
        }

        return this.gameRepository.save(newGame);
    }

    public void deleteByName(String name) {
        Game game = this.gameRepository.findByName(name);
        if( game == null) {
            throw new GameNotFoundException();
        }
        this.gameRepository.delete(game);
    }
}
