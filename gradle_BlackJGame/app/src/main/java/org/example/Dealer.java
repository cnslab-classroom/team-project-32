package org.example;

//Dealer class 수정됨, 동적으로 deck 입력받기 위해
public class Dealer {
    private Deck deck;
    private static Dealer instance;
    private int deckCount;

    // private 생성자: 외부에서 인스턴스화하지 못하도록 제한
    private Dealer(int deckCount) {
        this.deckCount = deckCount;
        deck = new Deck(deckCount);
    }

    // 싱글턴 패턴, Dealer를 1명으로
    public static synchronized Dealer getInstance(int deckCount) {
        if (instance == null) {
            instance = new Dealer(deckCount);
        } else {
            // 덱 개수가 변경되면 덱을 재설정
            if (instance.deckCount != deckCount) {
                instance.deckCount = deckCount;
                instance.deck = new Deck(deckCount);
            }
        }
        return instance;
    }

    public synchronized void dealCard(Player player) {
        if (player != null && !player.isBust() && !player.isStand()) {
            Card card = deck.drawCard();
            player.addCard(card);
        }
    }

    // 게임 재시작 시 덱을 재설정하는 메소드
    public synchronized void resetDeck(int deckCount) {
        this.deckCount = deckCount;
        deck = new Deck(deckCount);
    }

    public int getDeckCount() {
        return deckCount;
    }
}
