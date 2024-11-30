package org.example;

public class Dealer {
    
    public void dealInitialCards(Player[] players, Deck deck) { //처음 카드 나눠주는 메서드
        for (Player player : players) {
            player.addCard(deck.drawCard());
            player.addCard(deck.drawCard());
        }
    }
}
