package org.example;

public class Card {
    private final String suit; //카드의 모양(하트, 다이아몬드, 클럽, 스페이드)
    private final String rank; //카드의 숫자(2~10, J, Q, K, A)
    private final int value; //카드의 점수(1~11)
    
    public Card(String suit, String rank) { //카드 생성자
        this.suit = suit;
        this.rank = rank;
        
        switch (rank) { //카드 숫자에 따른 점수 부여
            case "A": //A는 11로 계산
                this.value = 11;
                break;
            case "J":
            case "Q":
            case "K":
                this.value = 10; //J, Q, K는 10으로 계산
                break;
            default:
                this.value = Integer.parseInt(rank); //나머지 숫자는 숫자 그대로 계산
                break;
        }
    }

    public String getSuit() { //카드모양 getter
        return suit;
    }

    public String getRank() { //카드숫자 getter
        return rank;
    }

    public int getValue() { //카드점수 getter
        return value;
    }

    public String printCard() { //카드 정보 출력
        return suit + " / " + rank;
    }
}
