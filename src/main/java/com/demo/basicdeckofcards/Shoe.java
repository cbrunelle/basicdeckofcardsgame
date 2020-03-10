package com.demo.basicdeckofcards;

import java.util.*;

public class Shoe {

    private ArrayList<Deck> decks = new ArrayList<>();
    private ArrayList<Card> cards = new ArrayList<>();

    public void addDeck(Deck deck) {
        if (!decks.contains(deck)) {
            this.decks.add(deck);
            this.cards.addAll(deck.getCards());
        }
    }

    public int getCount() {
        return this.cards.size();
    }

    public EnumMap<Suits, Integer> getCountPerSuit() {
        EnumMap<Suits, Integer> result = new EnumMap<>(Suits.class);
        result.put(Suits.HEARTS, 0);
        result.put(Suits.SPADES, 0);
        result.put(Suits.CLUBS, 0);
        result.put(Suits.DIAMONDS, 0);

        for (Card card : this.cards) {
            result.put(card.getSuit(), result.get(card.getSuit()) + 1);
        }
        return result;
    }

    public EnumMap<Suits, EnumMap<Values, Integer>> getCountPerCard() {
        EnumMap<Suits, EnumMap<Values, Integer>> result = new EnumMap<>(Suits.class);
        for(Suits suit : Suits.values()) {
            EnumMap<Values, Integer> map = new EnumMap<>(Values.class);
            for(Values value : Values.values()) {
                map.put(value, 0);
            }
            result.put(suit, map);
        }

        for (Card card : this.cards) {
            EnumMap<Values, Integer> suit = result.get(card.getSuit());
            suit.put(card.getValue(), suit.get(card.getValue()) + 1);
        }
        return result;
    }

    public void deal(Player player, int count) {
        // I'm not sure if we should refuse the whole deal if we have no card left or just deal what we can.
        // To be validate with requirements -- Charles Brunelle (brunelle.ch@gmail.com)
        for (int i = 0; i < count; i++) {
            try {
                player.receive(this.cards.remove(0));
            } catch (IndexOutOfBoundsException ex) {
                throw new NoMoreCardsInShoeException();
            }
        }
    }

    public void shuffle() {
        // Implementation based on Fisher-Yates algorithm
        // https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle#The_modern_algorithm ( 2020-03-10 )
        Random random = new Random();

        // Start from the last element and swap one by one. We don't
        // need to run for the first element that's why i > 0
        for (int i = this.getCount() -1; i > 0; i--) {
            Collections.swap(this.cards, i, random.nextInt(i));
        }
    }

    public List<Card> getCards() {
        return this.cards;
    }
}
