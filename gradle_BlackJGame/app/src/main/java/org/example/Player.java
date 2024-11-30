package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
   // private final String name; //Player 이름
    private int score; //Player 점수
    private String name;
    private int cardScore; //Player 카드 점수의 총 합
    private final List<Card> hands; //Player 카드 리스트
    private final boolean isHuman; //사람인지 컴퓨터인지 check
    private boolean isBlackJack; //BlackJack인지 확인
    private boolean isBust; //Bust인지 확인
    private boolean isStand; //카드 그만 받을지 확인
    private boolean isWin; //승리 확인
    private Object lock = new Object();
    
    public Player(String name, boolean isHuman) { //Player 생성자
      //  this.name = name;
        this.score = 0;
        this.cardScore = 0;
        this.hands = new ArrayList<>();
        this.name = name;
        this.isHuman = isHuman;
        this.isBlackJack = false;
        this.isBust = false;
        this.isStand = false;
        this.isWin = false;
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

    // public boolean isWin() { //승리 상태 getter
    //     return isWin;
    // }
    // public void setWin(boolean win) { //승리 상태 setter
    //     isWin = win;
    // }


    synchronized public void addCard(Card card) {//player hands에 카드 추가
        int aceCount = 0;
        hands.add(card);
        
        //A가 있을 때 aceCount 증가
        if (card.getRank().equals("A")) {aceCount++;}
        cardScore += card.getValue();

        if (cardScore > 21) {
            for (Card hand : hands) {
                if (hand.getRank().equals("A") && aceCount > 0) { //총 점수가 21을 넘고, A가 있을 때 A를 1로 계산
                    cardScore -= 10;
                    aceCount--;
                }
            }
            if (cardScore > 21) { //A를 1로 계산했을 때에도 21을 넘으면 Bust
                setBust(true);
            }
        }
        if (hands.size() == 2 && cardScore == 21) { //카드가 2장이고 21일 때 BlackJack
            setBlackJack(true);
        }
        Frame.appendLog(name + " drew: " + card.printCard() + " (Total: " + cardScore + ")");
    }

    @Override
    public void run() {
        Dealer dealer = Dealer.getInstance();
        synchronized (lock) {
           while(!isStand && !isBust) {
                if (isHuman) {
                    Frame.appendLog("Type 'hit' or 'stand':");
                    String action = Frame.getUserInput();
                    if(action.equals("h")) {
                        dealer.dealCard(this);
                    } else if (action.equals("s")) {
                        isStand = true;
                        Frame.appendLog("You stands!\n");
                        break;
                    }
                } else {
                    if(cardScore < 17) {
                        dealer.dealCard(this);
                    } else {
                        isStand = true;
                        Frame.appendLog("(AI) stands!\n");
                        
                    }
                }

            }
        }
    }
}
