package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Player implements Runnable{
    private final String name; //Player 이름
    private int score; //Player 점수
    private int cardScore; //Player 카드 점수의 총 합
    private final List<Card> hands; //Player 카드 리스트
    private final boolean isHuman; //사람인지 컴퓨터인지 check
    private boolean isBlackJack; //BlackJack인지 확인
    private boolean isBust; //Bust인지 확인
    private boolean isStand; //카드 그만 받을지 확인

    public Player(String name, boolean isHuman) { //Player 생성자
        this.name = name;
        this.score = 0;
        this.cardScore = 0;
        this.hands = new ArrayList<>();
        this.isHuman = isHuman;
        this.isBlackJack = false;
        this.isBust = false;
        this.isStand = false;
    }

    public String getName() { //Player 이름 getter
        return name;
    }

    public int getScore() { //Player 점수 getter
        return score;
    }
    public void setScore(int score) { //Player 점수 setter
        this.score = score;
    }

    public int getCardScore() { //Player 카드 점수 getter
        return cardScore;
    }
    public void setCardScore(int cardScore) { //Player 카드 점수 setter
        this.cardScore = cardScore;
    }

    public List<Card> getHands() { //Player 카드 리스트 getter
        return hands;
    }

    public boolean isHuman() { //사람인지 com인지 확인 , true면 사람
        return isHuman;
    }

    public boolean isBlackJack() { //BlackJack 상태 getter
        return isBlackJack;
    }
    public void setBlackJack(boolean blackJack) { //BlackJack 상태 setter
        isBlackJack = blackJack;
    }

    public boolean isBust() { //Bust 상태 getter
        return isBust;
    }
    public void setBust(boolean bust) { //Bust 상태 setter
        isBust = bust;
    }

    public boolean isStand() { //Stand 상태 getter
        return isStand;
    }
    public void setStand(boolean stand) { //Stand 상태 setter
        isStand = stand;
    }


    public void addCard(Card card) {//player hands에 카드 추가
        hands.add(card);
        cardScore += card.getValue();

        if (cardScore > 21) {
            for (Card hand : hands) {
                if (hand.getRank().equals("A")) { //총 점수가 21을 넘고, A가 있을 때 A를 1로 계산
                    cardScore -= 10; 
                }
            }
            if (cardScore > 21) { //A를 1로 계산했을 때에도 21을 넘으면 Bust
                isBust = true;
            }
        }
        if (hands.size() == 2 && cardScore == 21) { //카드가 2장이고 21일 때 BlackJack
            isBlackJack = true;
        }
    }

    public void displayHands() { //Player 카드 리스트 출력
        System.out.println(name + "'s hands: ");
        for (Card hand : hands) {
            System.out.println(hand.printCard());
        }
        System.out.println("Total score: " + cardScore);
    }

    @Override
    public void run() {
        if (!isHuman) {
            while (cardScore <= 17 && isBust == false) { //카드 점수가 17 이하일 때 카드 추가
                //addCard(deck.drawCard());
            }
        } else { //사람일 때
            while (true) {
                System.out.println("Choose hit or stand (Hit : 1 / Stand : 2)");
                try (Scanner scanner = new Scanner(System.in)) {
                    int answer = scanner.nextInt();

                    if (answer == 1) {
                        //addCard(deck.drawCard());
                        displayHands();
                        if (isBust) {
                            System.out.println("Bust!");
                            break;
                        }
                    } else if (answer == 2) {
                        break;
                    } else {
                        System.out.println("Invalid input. Please enter 1 or 2.");
                    }
                }
            }
            
    }
}

}
