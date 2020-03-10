package com.demo.basicdeckofcards;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

@RestController
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/games")
    private Map<String, ArrayList<HashMap<String, String>>> getGames() throws Exception {
        Map<String,ArrayList<HashMap<String, String>>> response = new HashMap<>();

        ArrayList<HashMap<String, String>> games = new ArrayList<>();
        gameService.getList().forEach(game -> {
            HashMap<String, String> output = new HashMap<>();
            output.put("name", game.getName());
            output.put("href", "/games/"+game.getName());
            games.add(output);
        });
        response.put("games", games);
        return response;
    }

    @PostMapping("/games")
    private HashMap<String, String> postGame(@RequestBody Game newGame) throws Exception  {
        Game game = this.gameService.create(newGame);
        HashMap<String, String> output = new HashMap<>();
        output.put("name", game.getName());
        output.put("href", "/games/"+game.getName());
        return output;
    }

    @GetMapping("/games/{name}")
    private HashMap<String, String> getGame(@PathVariable String name) throws Exception  {
        Game game = this.gameService.getGameDetails(name);
        HashMap<String, String> output = new HashMap<>();
        output.put("name", game.getName());
        output.put("href", "/games/"+game.getName());
        return output;
    }

    @DeleteMapping("/games/{name}")
    private void deleteGame(@PathVariable String name) throws Exception  {
        this.gameService.deleteByName(name);
    }

    @GetMapping("/games/{name}/players")
    private Map<String, ArrayList<HashMap<String, Object>>> getPlayers(@PathVariable String name) throws Exception  {
        Game game = this.gameService.getGameDetails(name);
        return getPlayersMap(game);
    }

    @GetMapping("/games/{gameName}/players/{playerName}")
    private Map<String, Object> getPlayerByName(@PathVariable String gameName, @PathVariable String playerName) throws Exception  {
        Game game = this.gameService.getGameDetails(gameName);
        return getPlayerMap(playerName, game);
    }

    @PostMapping("/games/{name}/players")
    private Map<String, ArrayList<HashMap<String, Object>>> createPlayers(@PathVariable String name, @RequestBody Player newPlayer) throws Exception  {
        Game game = this.gameService.getGameDetails(name);
        game.addPlayer(newPlayer);
        return getPlayersMap(game);
    }

    @DeleteMapping("/games/{gameName}/players/{playerName}")
    private Map<String, ArrayList<HashMap<String, Object>>> deletePlayers(@PathVariable String gameName, @PathVariable String playerName) throws Exception  {
        Game game = this.gameService.getGameDetails(gameName);
        game.deletePlayerByName(playerName);
        return getPlayersMap(game);
    }

    @GetMapping("/games/{gameName}/deck")
    private Map<String, Object> getDeck(@PathVariable String gameName, @RequestParam(name = "group-by", required = false, defaultValue = "NONE") GroupByModes mode) throws Exception  {
        Game game = this.gameService.getGameDetails(gameName);
        Shoe shoe = game.getShoe();

        HashMap<String, Object> output = new HashMap<>();
        output.put("game", game.getName());
        output.put("remaining", shoe.getCount());
        if (mode == GroupByModes.CARD){
            output.put("cards", shoe.getCountPerCard());
        } else if (mode == GroupByModes.SUIT) {
            output.put("cards", shoe.getCountPerSuit());
        }
        return output;
    }

    @PutMapping("/games/{gameName}/deck/deal")
    private Map<String, Object> dealToPlayer(@PathVariable String gameName, @RequestParam(name = "player") String playerName) throws Exception  {
        Game game = this.gameService.getGameDetails(gameName);
        game.getShoe().deal(game.getPlayer(playerName), 1);
        return getPlayerMap(playerName, game);
    }

    @PutMapping("/games/{gameName}/deck/add")
    private Map<String, Object> addDeckToGame(@PathVariable String gameName) throws Exception  {
        Game game = this.gameService.getGameDetails(gameName);
        Shoe shoe = game.getShoe();
        shoe.addDeck(new Deck());

        HashMap<String, Object> output = new HashMap<>();
        output.put("game", game.getName());
        output.put("remaining", shoe.getCount());
        return output;
    }

    private Map<String, Object> getPlayerMap(@RequestParam(name = "player") String playerName, Game game) {
        Player player = game.getPlayer(playerName);
        HashMap<String, Object> output = new HashMap<>();
        output.put("name", player.getName());
        output.put("total", player.getTotalValue());
        output.put("cards", player.getCards());
        return output;
    }

    private Map<String, ArrayList<HashMap<String, Object>>> getPlayersMap(Game game) {
        Map<String, ArrayList<HashMap<String, Object>>> response = new HashMap<>();

        ArrayList<HashMap<String, Object>> players = new ArrayList<>();
        ArrayList<Player> playersSorted = new ArrayList<Player>(game.getPlayers());
        playersSorted.sort(Comparator.comparingInt(Player::getTotalValue).reversed());
        playersSorted.forEach(player -> {
            HashMap<String, Object> output = new HashMap<>();
            output.put("name", player.getName());
            output.put("total", player.getTotalValue());
            output.put("href", "/games/" + game.getName() + "/players/" + player.getName());
            players.add(output);
        });
        response.put("players", players);
        return response;
    }
}
