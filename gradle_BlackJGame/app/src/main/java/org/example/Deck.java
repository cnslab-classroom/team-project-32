package org.example;
import java.util.Collections;
import java.util.Stack;
import java.util.ArrayList;;

public class Deck {
    //private Card[] cards; //덱의 카드 배열
    private Stack<Card> cards;
    private int stacks; //덱의 카드 수
    private int sets; //덱의 세트 수

    public Deck(int sets) { //덱 생성자
        if (sets >= 1 && sets <= 4) { //입력한 세트 수가 1~4 사이일 때
            this.stacks = 52 * sets;
            this.sets = sets; 
        } else {
            this.sets = 2; //덱의 세트 수 default값 설정
            this.stacks = 52 * this.sets;
        }
        //this.cards = new Card[stacks]; //카드 배열 생성
        this.cards = new Stack<>();

        String[] suits = {"♠", "♣", "♥", "♦"}; //카드 모양 배열
        String[] ranks = {"A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K"}; //카드 숫자 배열

        //int index = 0; //카드 배열 인덱스
        for (int i = 0; i < sets; i++) { 
            for (String suit : suits) { 
                for (String rank : ranks) { 
                    //cards[index++] = new Card(suit, rank); //카드 생성
                    cards.push(new Card(suit, rank));
                }
            }
        }
    }

    public void suffle() { //덱 섞기
        ArrayList<Card> temp = new ArrayList<>(cards);
        Collections.shuffle(temp);
        cards.clear();
        cards.addAll(temp);
    }
}
