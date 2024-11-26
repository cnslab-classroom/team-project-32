package org.example;

import java.util.Scanner;

public class GameManager {
    public void startGame() {
        int[] winners = new int[4]; //승자가 여러명인 경우도 고려
        int index = 0; //승자 배열 인덱스

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
                players[i] = new Player("COM" + i, false);
            }

            Dealer dealer = new Dealer();
            Deck deck = new Deck(deckNum);
            dealer.dealInitialCards(players, deck);

            for (Player player : players) {
                player.displayHands();
                System.out.println();
            }

            int turn = 0;
            while (true) {
                //플레이어에게 블랙잭이 있는지 확인, 있으면 바로 승자로 설정, 여러명이 블랙잭 나온 경우도 고려, 게임 종료
                for (Player player : players) {
                    if (player.isBlackJack()) {
                        System.out.println(player.getName() + " BlackJack!");
                        player.setWin(true);
                        System.out.println(player.getName() + " wins!");
                        System.out.println();
                        return;
                    }
                }


                Player currentPlayer = players[turn % (playerNum + 1)];
                if (currentPlayer.isHuman()) { //사람일 때

                    if (!currentPlayer.isStand() && !currentPlayer.isBust()) {//카드 더 받을 수 있을 때
                        System.out.println(currentPlayer.getName() + "'s turn! " + "Score : " + currentPlayer.getCardScore());

                        System.out.println("Do you want to hit or stand? (h/s): ");
                        String action = scanner.nextLine();
                        if (action.equals("h")) {
                            currentPlayer.addCard(deck.drawCard());
                            turn++;
                        } else if (action.equals("s")) {
                            currentPlayer.setStand(true);
                            System.out.println(currentPlayer.getName() + " stands!");
                            turn++;
                        } else {
                        System.out.println("Invalid input. Please enter h or s.");
                        }

                    } else { //카드 더 받을 수 없을 때
                        if (currentPlayer.isBust()) { //bust일 때
                            System.out.println(currentPlayer.getName() + " Bust!");
                            turn++;
                        } else if (currentPlayer.isStand()) { //stand일 때
                            System.out.println(currentPlayer.getName() + " stands!");
                            turn++;
                        }
                    }

                } else { //AI일 때
                    System.out.println(currentPlayer.getName() + "'s turn!" + " Score : " + currentPlayer.getCardScore());

                    if (currentPlayer.getCardScore() < 17 && !currentPlayer.isBust() && !currentPlayer.isStand()) { //카드 더 받을 수 있을 때
                        currentPlayer.addCard(deck.drawCard());
                        turn++;
                    } else {
                        if (currentPlayer.isBust()) { //bust일 때
                            System.out.println(currentPlayer.getName() + " Bust!");
                            turn++;
                        } else {
                            currentPlayer.setStand(true);
                            System.out.println(currentPlayer.getName() + " stands!");
                            turn++;
                        }
                    }
                }
                currentPlayer.displayHands();
                System.out.println();

                if (currentPlayer.isBust()) {
                    System.out.println(currentPlayer.getName() + " is bust!");
                    System.out.println();
                }
                
                boolean allStandOrBust = true; //모든 플레이어가 stand 또는 bust인지 확인
                for (Player player : players) {
                    if (!player.isStand() && !player.isBust() && player.getCardScore() < 21) {
                        allStandOrBust = false;
                    }
                }
                if (allStandOrBust) { break; }
            }
            //점수 비교후 승자 출력
            int maxScore = 0;

            for (Player player : players) {
                int score = player.getCardScore();
            
                if (score <= 21) {
                    if (score > maxScore) {
                        maxScore = score;
                        index = 0;
                        winners[index] = score;
                        index++;
                    } else if (score == maxScore) {
                        winners[index] = score;
                        index++;
                    }
                } else {
                    winners[index] = 0;
                    index++;
                }
            }   

            for (Player player : players) {
                for (int i = 0; i < players.length; i++) {
                    if (winners[i] == player.getCardScore()) {
                        player.setWin(true);
                    }
                }
            }

            if (maxScore > 0) {
                for (Player player : players) {
                    if (player.isWin()) {
                        System.out.println(player.getName() + " wins!" + " Score: " + player.getCardScore());
                        System.out.println();
                    }  
                }   
            } else {
                System.out.println("No winner!");
            }
        }
    }
}


