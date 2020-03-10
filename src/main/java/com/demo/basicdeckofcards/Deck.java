package com.demo.basicdeckofcards;

import java.util.ArrayList;
import java.util.Collection;

public class Deck {
    public ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        for (Suits suit : Suits.values()) {
            for (Values value : Values.values()) {
                cards.add(new Card(suit, value));
            }
        }
    }

    public int getCount() {
        return cards.size();
    }

    public Collection<Card> getCards() {
        return cards;
    }
}
