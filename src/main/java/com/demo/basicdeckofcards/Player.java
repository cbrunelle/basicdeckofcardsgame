package com.demo.basicdeckofcards;

import java.util.ArrayList;
import java.util.List;

public class Player {

    private String name;
    private ArrayList<Card> cards = new ArrayList<>();

    public Player() {
        this("NoName");
    }

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void receive(Card card) {
        this.cards.add(card);
    }

    public int getTotalValue() {
        int total = 0;
        for (Card card : this.cards) {
            total += card.getValue().Value();
        }
        return total;
    }

    public List<Card> getCards() {
        return this.cards;
    }
}
