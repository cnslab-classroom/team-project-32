package org.example;

//Dealer class ������, �������� deck �Է¹ޱ� ����
public class Dealer {
    private Deck deck;
    private static Dealer instance;
    private int deckCount;

    // private ������: �ܺο��� �ν��Ͻ�ȭ���� ���ϵ��� ����
    private Dealer(int deckCount) {
        this.deckCount = deckCount;
        deck = new Deck(deckCount);
    }

    // �̱��� ����, Dealer�� 1������
    public static synchronized Dealer getInstance(int deckCount) {
        if (instance == null) {
            instance = new Dealer(deckCount);
        } else {
            // �� ������ ����Ǹ� ���� �缳��
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

    // ���� ����� �� ���� �缳���ϴ� �޼ҵ�
    public synchronized void resetDeck(int deckCount) {
        this.deckCount = deckCount;
        deck = new Deck(deckCount);
    }

    public int getDeckCount() {
        return deckCount;
    }
}
