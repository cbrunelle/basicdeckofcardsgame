package com.demo.basicdeckofcards;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ShoeTest {

    private Shoe shoe;

    @BeforeEach
    void setUp() {
        shoe = new Shoe();
    }

    @Test
    void addDeck() {
        assertThat(shoe.getCount()).isEqualTo(0);
        shoe.addDeck(new Deck());
        assertThat(shoe.getCount()).isEqualTo(52);
    }

    @Test
    void getCountPerSuit() {
        shoe.addDeck(new Deck());

        Map<Suits, Integer> result = shoe.getCountPerSuit();

        for (Suits suit : Suits.values()) {
            assertThat(result.get(suit)).isEqualTo(13);
        }
    }

    @Test
    void getCountPerCard() {
        shoe.addDeck(new Deck());

        Map<Suits, EnumMap<Values, Integer>> result = shoe.getCountPerCard();

        for (Suits suit : Suits.values()) {
            for (Values value : Values.values()) {
                assertThat(result.get(suit).get(value)).isEqualTo(1);
            }
        }
    }

    @Test
    void deal() {
        Player player = new Player("Player One");
        shoe.addDeck(new Deck());
        shoe.addDeck(new Deck());

        shoe.deal(player, 1);
        assertThat(shoe.getCount()).isEqualTo(103);
    }

    @Test
    void cannotDealMoreCardThanInShoe() {
        Player player = new Player("Player One");
        shoe.addDeck(new Deck());

        shoe.deal(player, 1);
        assertThat(shoe.getCount()).isEqualTo(51);
        assertThatExceptionOfType(NoMoreCardsInShoeException.class).isThrownBy(() -> {
            shoe.deal(player, 52);
        });

        assertThat(shoe.getCount()).isEqualTo(1);
    }

    @Test
    void shuffle() {
        // To test if the shuffle algorithm is working, only the first 5 cards will be checked
        // It is known that it's statistically wrong but should be sufficient to never happen.
        shoe.addDeck(new Deck());

        shoe.shuffle();
        List<Card> shuffleCards = shoe.getCards();
        assertThat(shuffleCards.get(0).equals(new Card(Suits.HEARTS, Values.ACE))
                && shuffleCards.get(1).equals(new Card(Suits.HEARTS, Values.TWO))
                && shuffleCards.get(2).equals(new Card(Suits.HEARTS, Values.THREE))
                && shuffleCards.get(3).equals(new Card(Suits.HEARTS, Values.FOUR))
                && shuffleCards.get(4).equals(new Card(Suits.HEARTS, Values.FIVE))).isFalse();
    }
}