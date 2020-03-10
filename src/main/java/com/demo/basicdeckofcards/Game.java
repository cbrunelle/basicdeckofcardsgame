package com.demo.basicdeckofcards;

import java.util.ArrayList;
import java.util.Collection;

public class Game {

    private String name;
    private final ArrayList<Player> players = new ArrayList<>();
    private final Shoe shoe = new Shoe();

    public Game() {
        this("Default");
    }

    public Game(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Shoe getShoe() {
        return shoe;
    }

    public Collection<Player> getPlayers() {
        return players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
    }

    public void deletePlayerByName(String playerName) {
        this.players.removeIf(player -> player.getName().equals(playerName));
    }

    public Player getPlayer(String name) {
        for (Player player : this.players) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        throw new PlayerNotFoundException();
    }
}
