package project;

import java.util.LinkedList;

public class Player {
    LinkedList<Card> hands = new LinkedList<>();

    void clear() {
        hands.clear();
    }

    void drawing() {
        hands.addAll(Deck.table);
        for (int i = 0; i < 2; ++i) {
            hands.add(Deck.draw());
        }
    }

    void getHands() {
        for (int i = 0; i < hands.size(); ++i) {
            System.out.println(hands.get(i).suit + " " + hands.get(i).number);
        }
    }
}
