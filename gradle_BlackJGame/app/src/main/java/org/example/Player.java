package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
    private int score; //Player ����
    private String name;
    private int cardScore; //Player ī�� ������ �� ��
    private final List<Card> hands; //Player ī�� ����Ʈ
    private final boolean isHuman; //������� ��ǻ������ check
    private boolean isBlackJack; //BlackJack���� Ȯ��
    private boolean isBust; //Bust���� Ȯ��
    private boolean isStand; //ī�� �׸� ������ Ȯ��
    private Object lock = new Object();
<<<<<<< HEAD
    
    public Player(String name, boolean isHuman) { //Player 생성자
      //  this.name = name;
=======

    public Player(String name, boolean isHuman) { //Player ������
>>>>>>> a56d42c9b0a9d74514de7209f201e47f26c64b6f
        this.score = 0;
        this.cardScore = 0;
        this.hands = new ArrayList<>();
        this.name = name;
        this.isHuman = isHuman;
        this.isBlackJack = false;
        this.isBust = false;
        this.isStand = false;
    }

    public String getName() { //Player �̸� getter
        return name;
    }

    public int getScore() { //Player ���� getter
        return score;
    }
    public void setScore(int score) { //Player ���� setter
        this.score = score;
    }

    public int getCardScore() { //Player ī�� ���� getter
        return cardScore;
    }
    public void setCardScore(int cardScore) { //Player ī�� ���� setter
        this.cardScore = cardScore;
    }

    public List<Card> getHands() { //Player ī�� ����Ʈ getter
        return hands;
    }

    public boolean isHuman() { //������� com���� Ȯ�� , true�� ���
        return isHuman;
    }

    public boolean isBlackJack() { //BlackJack ���� getter
        return isBlackJack;
    }
    public void setBlackJack(boolean blackJack) { //BlackJack ���� setter
        isBlackJack = blackJack;
    }

    public boolean isBust() { //Bust ���� getter
        return isBust;
    }
    public void setBust(boolean bust) { //Bust ���� setter
        isBust = bust;
    }

    public boolean isStand() { //Stand ���� getter
        return isStand;
    }
    public void setStand(boolean stand) { //Stand ���� setter
        isStand = stand;
    }

<<<<<<< HEAD
    synchronized public void addCard(Card card) {//player hands에 카드 추가
=======
    synchronized public void addCard(Card card) {//player hands�� ī�� �߰�
>>>>>>> a56d42c9b0a9d74514de7209f201e47f26c64b6f
        int aceCount = 0;
        hands.add(card);
        
        // A�� ���� �� aceCount ����
        if (card.getRank().equals("A")) { aceCount++; }
        cardScore += card.getValue();

        if (cardScore > 21) {
            for (Card hand : hands) {
                if (hand.getRank().equals("A") && aceCount > 0) { // �� ������ 21�� �Ѱ�, A�� ���� �� A�� 1�� ���
                    cardScore -= 10;
                    aceCount--;
                }
            }
            if (cardScore > 21) { // A�� 1�� ������� ������ 21�� ������ Bust
                setBust(true);
            }
        }
        if (hands.size() == 2 && cardScore == 21) // ī�尡 2���̰� 21�� �� BlackJack
            setBlackJack(true);
        
        if (isHuman)
            Frame.appendUserLog(name + " drew: " + card.printCard() + " (Total: " + cardScore + ")");
        else
            Frame.appendAiLog(name + " drew: " + card.printCard() + " (Total: " + cardScore + ")");
    }

    @Override
    public void run() {
        Dealer dealer = Dealer.getInstance(GameManager.getInstance().getDeckCount());
        synchronized (lock) {
           while(!isStand && !isBust) {
                if (isHuman) {
                    Frame.appendUserLog("Type 'hit' or 'stand':");
                    String action = Frame.getUserInput();
                    if(action.equalsIgnoreCase("h")) {
                        dealer.dealCard(this);
                    } else if (action.equalsIgnoreCase("s")) {
                        isStand = true;
                        Frame.appendUserLog("You stand!\n");
                        break;
                    }
                } else {
                    if(cardScore < 17) {
                        dealer.dealCard(this);
                    } else {
                        isStand = true;
                        Frame.appendAiLog("(AI) stands!\n");
                        
                    }
                }

            }
        }
    }

    // ���� ����� �� �÷��̾� ���� �ʱ�ȭ �޼ҵ�
    public void reset() {
        this.score = 0;
        this.cardScore = 0;
        this.hands.clear();
        this.isBlackJack = false;
        this.isBust = false;
        this.isStand = false;
    }
}
