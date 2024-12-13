package org.example;
import java.util.Collections;
import java.util.Stack;

public final class Deck {
    private final Stack<Card> cards;
    private int stacks; //���� ī�� ��
    private final int sets; //���� ��Ʈ ��
    boolean flag = true; //�÷��� ����

    public Deck(int sets) { //�� ������
        if (sets >= 1 && sets <= 4) { //�Է��� ��Ʈ ���� 1~4 ������ ��
            this.stacks = 52 * sets;
            this.sets = sets; 
        } else {
            this.sets = 2; //���� ��Ʈ �� default�� ����
            this.stacks = 52 * this.sets;
        }
        this.cards = new Stack<>();

        String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; //ī�� ���� �迭

        for (int i = 0; i < sets; i++) { 
            for (String suit : suits) { 
                for (String rank : ranks) { 
                    cards.push(new Card(suit, rank));
                }
            }
        }
        shuffleDeck();
    }

    public synchronized Card drawCard() { //ī�� �̱�
        if (cards.isEmpty()) {
            //ī�� ������ �ٽ� ä���
            for (int i = 0; i < sets; i++) {
                for (Card card : cards) {
                    cards.push(card);
                }
            }
            shuffleDeck();
        }
        
        stacks--;
        return cards.pop();
        
    }

    private void shuffleDeck() { //�� ����
        Collections.shuffle(cards);
    }

}