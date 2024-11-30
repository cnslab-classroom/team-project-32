package org.example;

public class Dealer {
    private final Deck deck;

    // private 생성자: 외부에서 인스턴스화하지 못하도록 제한
    private Dealer(int deckCount) {
        deck = new Deck(deckCount);
    }

    // 싱글턴 패턴, Dealer를 1명으로
    private static class SingletonHolder {
        private static final Dealer INSTANCE = new Dealer(1); // 기본적으로 덱 수 1개
    }

    public static Dealer getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public synchronized void dealCard(Player player) {
        if (player != null && !player.isBust() && !player.isStand()) {
            Card card = deck.drawCard();
            player.addCard(card);
        }
    }
}
