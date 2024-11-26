package org.example;

import java.util.Scanner;

public class GameManager {
    public void startGame() {
        Player winner = null; //승자 저장할 변수
        try (Scanner scanner = new Scanner(System.in)) {
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
                    System.out.println(card.printCard());
                }
                System.out.println();
            }

            int turn = 0;
            while (true) {
                //플레이어들이 블랙젝이 있는지 확인, 있으면 승자로 설정
                for (Player player : players) {
                    if (player.isBlackJack()) {
                        winner = player;
                        break;
                    }
                }

                Player currentPlayer = players[turn % (playerNum + 1)];
                if (currentPlayer.isHuman() && !currentPlayer.isStand() && !currentPlayer.isBust()) { //사람일 때+카드 더 받을 수 있을 때
                    System.out.println("Your turn! " + "Score : " + currentPlayer.getCardScore());

                    System.out.println("Do you want to hit or stand? (h/s): ");
                    String action = scanner.nextLine();
                    if (action.equals("h")) {
                        currentPlayer.addCard(deck.drawCard());
                    } else if (action.equals("s")) {
                        currentPlayer.setStand(true);
                    } else {
                        System.out.println("Invalid input. Please enter h or s.");
                    }
                    
                } else { //AI일 때
                    System.out.println(currentPlayer.getName() + "'s turn!" + "Score : " + currentPlayer.getCardScore());

                    if (currentPlayer.getCardScore() < 17 && !currentPlayer.isBust() && !currentPlayer.isStand()) {
                        currentPlayer.addCard(deck.drawCard());
                    } else {
                        if (!currentPlayer.isBust()) {
                            System.out.println(currentPlayer.getName() + " Bust!");
                            turn++;
                        } else {
                            currentPlayer.setStand(true);
                            System.out.println(currentPlayer.getName() + " stands!");
                            turn++;
                        }
                    }

                System.out.println(currentPlayer.getName() + "'s cards: ");
                for (Card card : currentPlayer.getHands()) {
                    System.out.println(card.printCard());
                }
                System.out.println();

                if (currentPlayer.getCardScore() > 21) {
                    currentPlayer.setBust(true);
                    System.out.println(currentPlayer.getName() + " is bust!");
                    System.out.println();
                }
                turn++;
                
                boolean allStandOrBust = true; //모든 플레이어가 stand 또는 bust인지 확인
                for (Player player : players) {
                    if (!player.isStand() && !player.isBust() && player.getCardScore() < 21) {
                        allStandOrBust = false;
                    }
                }
                if (allStandOrBust) {
                    break;
                }
            }

            //점수 비교후 승자 출력
            
            int maxScore = 0;
            for (Player player : players) {
                if (player.getCardScore() <= 21 && player.getCardScore() > maxScore) {
                    maxScore = player.getCardScore();
                    winner = player;
                }
            }

                if (winner != null) {
                    System.out.println("The winner is " + winner.getName() + " score : " + winner.getCardScore() + "!");
                    break;
                } else {
                    System.out.println("No winner!");
                    break;
                }
            }
        }
    }
}