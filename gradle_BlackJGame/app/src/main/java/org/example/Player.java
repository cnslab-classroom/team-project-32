package org.example;

import java.util.ArrayList;
import java.util.List;

public class Player implements Runnable {
    private int score; //Player Á¡¼ö
    private String name;
    private int cardScore; //Player Ä«µå Á¡¼öÀÇ ÃÑ ÇÕ
    private final List<Card> hands; //Player Ä«µå ¸®½ºÆ®
    private final boolean isHuman; //»ç¶÷ÀÎÁö ÄÄÇ»ÅÍÀÎÁö check
    private boolean isBlackJack; //BlackJackÀÎÁö È®ÀÎ
    private boolean isBust; //BustÀÎÁö È®ÀÎ
    private boolean isStand; //Ä«µå ±×¸¸ ¹ŞÀ»Áö È®ÀÎ
    private Object lock = new Object();
<<<<<<< HEAD
    
    public Player(String name, boolean isHuman) { //Player ìƒì„±ì
      //  this.name = name;
=======

    public Player(String name, boolean isHuman) { //Player »ı¼ºÀÚ
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

    public String getName() { //Player ÀÌ¸§ getter
        return name;
    }

    public int getScore() { //Player Á¡¼ö getter
        return score;
    }
    public void setScore(int score) { //Player Á¡¼ö setter
        this.score = score;
    }

    public int getCardScore() { //Player Ä«µå Á¡¼ö getter
        return cardScore;
    }
    public void setCardScore(int cardScore) { //Player Ä«µå Á¡¼ö setter
        this.cardScore = cardScore;
    }

    public List<Card> getHands() { //Player Ä«µå ¸®½ºÆ® getter
        return hands;
    }

    public boolean isHuman() { //»ç¶÷ÀÎÁö comÀÎÁö È®ÀÎ , true¸é »ç¶÷
        return isHuman;
    }

    public boolean isBlackJack() { //BlackJack »óÅÂ getter
        return isBlackJack;
    }
    public void setBlackJack(boolean blackJack) { //BlackJack »óÅÂ setter
        isBlackJack = blackJack;
    }

    public boolean isBust() { //Bust »óÅÂ getter
        return isBust;
    }
    public void setBust(boolean bust) { //Bust »óÅÂ setter
        isBust = bust;
    }

    public boolean isStand() { //Stand »óÅÂ getter
        return isStand;
    }
    public void setStand(boolean stand) { //Stand »óÅÂ setter
        isStand = stand;
    }

<<<<<<< HEAD
    synchronized public void addCard(Card card) {//player handsì— ì¹´ë“œ ì¶”ê°€
=======
    synchronized public void addCard(Card card) {//player hands¿¡ Ä«µå Ãß°¡
>>>>>>> a56d42c9b0a9d74514de7209f201e47f26c64b6f
        int aceCount = 0;
        hands.add(card);
        
        // A°¡ ÀÖÀ» ¶§ aceCount Áõ°¡
        if (card.getRank().equals("A")) { aceCount++; }
        cardScore += card.getValue();

        if (cardScore > 21) {
            for (Card hand : hands) {
                if (hand.getRank().equals("A") && aceCount > 0) { // ÃÑ Á¡¼ö°¡ 21À» ³Ñ°í, A°¡ ÀÖÀ» ¶§ A¸¦ 1·Î °è»ê
                    cardScore -= 10;
                    aceCount--;
                }
            }
            if (cardScore > 21) { // A¸¦ 1·Î °è»êÇßÀ» ¶§¿¡µµ 21À» ³ÑÀ¸¸é Bust
                setBust(true);
            }
        }
        if (hands.size() == 2 && cardScore == 21) // Ä«µå°¡ 2ÀåÀÌ°í 21ÀÏ ¶§ BlackJack
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

    // °ÔÀÓ Àç½ÃÀÛ ½Ã ÇÃ·¹ÀÌ¾î »óÅÂ ÃÊ±âÈ­ ¸Ş¼Òµå
    public void reset() {
        this.score = 0;
        this.cardScore = 0;
        this.hands.clear();
        this.isBlackJack = false;
        this.isBust = false;
        this.isStand = false;
    }
}
