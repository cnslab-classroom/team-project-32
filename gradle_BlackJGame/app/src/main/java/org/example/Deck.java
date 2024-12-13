package org.example;
import java.util.Collections;
import java.util.Stack;

public final class Deck {
    private final Stack<Card> cards;
    private int stacks; //덱의 카드 수
    private final int sets; //덱의 세트 수
    boolean flag = true; //플래그 변수

    public Deck(int sets) { //덱 생성자
        if (sets >= 1 && sets <= 4) { //입력한 세트 수가 1~4 사이일 때
            this.stacks = 52 * sets;
            this.sets = sets; 
        } else {
            this.sets = 2; //덱의 세트 수 default값 설정
            this.stacks = 52 * this.sets;
        }
        this.cards = new Stack<>();

        String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; //카드 숫자 배열

        for (int i = 0; i < sets; i++) { 
            for (String suit : suits) { 
                for (String rank : ranks) { 
                    cards.push(new Card(suit, rank));
                }
            }
        }
        shuffleDeck();
    }

    public synchronized Card drawCard() { //카드 뽑기
        if (cards.isEmpty()) {
            //카드 없으면 다시 채우기
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

    private void shuffleDeck() { //덱 섞기
        Collections.shuffle(cards);
    }

}