package project;

import java.util.Collections;
import java.util.LinkedList;

public class Deck {
    public final String[] SUIT = { "스페이드", "클로버", "다이아몬드", "하트" };
    public static LinkedList<Card> deck = new LinkedList<>();
    public static LinkedList<Card> table = new LinkedList<>();

    public Deck() {
        initialize();
    }

    void initialize() {
        deck.clear();
        table.clear();
        deckGenerate();
        Collections.shuffle(deck);
    }

    void deckGenerate() {
        for (int i = 0; i < SUIT.length; ++i) {
            for (int j = 0; j < 13; ++j) {
                Card card = new Card();
                card.suit = SUIT[i];
                card.number = j + 1;
                deck.add(card);
            }
        }
    }

    void tableSet() {
        for (int i = 0; i < 5; ++i) {
            table.add(draw());
        }
    }

    static Card draw() {
        Card card = deck.getLast();
        deck.removeLast();
        return card;
    }
}
