package org.example;

import java.util.Scanner;

public class GameManager {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to BlackJack Game!");

        System.out.println("Enter your name: ");
        String name = scanner.nextLine();

        System.out.println("Enter the number of AI players (1~3): ");
        int playerNum = scanner.nextInt();
        scanner.nextLine();
        if (playerNum < 1 || playerNum > 3) {
            playerNum = 1;
        } 

        System.out.println("How many decks do you want to use? (1~4): ");
        int deckNum = scanner.nextInt();
        scanner.nextLine();
        if (deckNum < 1 || deckNum > 4) {
            deckNum = 1;
        }

        Player[] players = new Player[playerNum + 1];
        players[0] = new Player(name, true);
        for (int i = 1; i <= playerNum; i++) {
            players[i] = new Player("AI " + i, false);
        }

        Dealer dealer = new Dealer();
        Deck deck = new Deck(deckNum);
        dealer.dealInitialCards(players, deck);

        for (Player player : players) {
            System.out.println(player.getName() + "'s cards: ");
            for (Card card : player.getHands()) {
                System.out.println(card.getSuit() + card.getRank());
            }
            System.out.println();
        }

    }
}
