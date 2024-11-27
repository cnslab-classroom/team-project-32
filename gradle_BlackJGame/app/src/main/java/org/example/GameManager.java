package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager {
    private static final int MIN_PLAYERS = 1;
    private static final int MAX_PLAYERS = 3;
    private static final int MIN_DECKS = 1;
    private static final int MAX_DECKS = 4;
    private static final int AI_STAND_THRESHOLD = 17;
    private static final int BLACKJACK_SCORE = 21;

    public void startGame() {
        try (Scanner scanner = new Scanner(System.in)) {
            //초기 게임 설정
            String playerName = promptPlayerName(scanner);
            int playerCount = promptPlayerCount(scanner);
            int deckCount = promptDeckCount(scanner);

            //플래이어, 딜러, 덱 생성
            List<Player> players = createPlayers(playerName, playerCount);
            Dealer dealer = new Dealer();
            Deck deck = new Deck(deckCount);

            //처음 카드 나눠주기 및 초기 카드 출력
            dealer.dealInitialCards(players.toArray(new Player[0]), deck);
            displayInitialHands(players);

            //게임 진행
            playGameRound(scanner, players, deck);

            //승자 결정
            determineWinners(players);
        }
    }

    private String promptPlayerName(Scanner scanner) { //이름 입력받기
        System.out.println("Welcome to BlackJack Game!");
        System.out.print("Enter your name: ");
        return scanner.nextLine();
    }

    private int promptPlayerCount(Scanner scanner) { //AI 플레이어 수 입력받기
        System.out.print("Enter the number of AI players (1-3): ");
        int playerNum = scanner.nextInt();
        scanner.nextLine(); 
        return Math.min(Math.max(playerNum, MIN_PLAYERS), MAX_PLAYERS);
    }

    private int promptDeckCount(Scanner scanner) { //덱 수 입력받기
        System.out.print("How many decks do you want to use? (1-4): ");
        int deckNum = scanner.nextInt();
        scanner.nextLine(); 
        return Math.min(Math.max(deckNum, MIN_DECKS), MAX_DECKS);
    }

    private List<Player> createPlayers(String playerName, int playerCount) { //플레이어 생성
        List<Player> players = new ArrayList<>();
        players.add(new Player(playerName, true));
        
        for (int i = 1; i <= playerCount; i++) {
            players.add(new Player("COM" + i, false));
        }
        return players;
    }

    private void displayInitialHands(List<Player> players) { //처음 카드 출력
        for (Player player : players) {
            player.displayHands();
            System.out.println();
        }
    }

    private void playGameRound(Scanner scanner, List<Player> players, Deck deck) { //게임 진행
        int turn = 0;
        while (!isGameRoundComplete(players)) {
            Player currentPlayer = players.get(turn % players.size());
            
            //블랙잭 나왔는지 체크
            if (checkForBlackJack(currentPlayer)) {
                return;
            }

            //플레이어 턴 진행
            handlePlayerTurn(scanner, currentPlayer, deck, turn);
            
            turn++;
        }
    }

    private boolean checkForBlackJack(Player player) { //블랙잭 체크 + 승자 확인하는 메서드
        if (player.isBlackJack()) {
            System.out.println(player.getName() + " BlackJack!");
            player.setWin(true);
            System.out.println(player.getName() + " wins!");
            System.out.println();
            return true;
        }
        return false;
    }

    private void handlePlayerTurn(Scanner scanner, Player player, Deck deck, int turn) { //플레이어 턴 진행
        if (player.isHuman()) {
            handleHumanPlayerTurn(scanner, player, deck);
        } else {
            handleAIPlayerTurn(player, deck);
        }
        
        player.displayHands();
        System.out.println();

        if (player.isBust()) { //버스트 됐을 때
            System.out.println(player.getName() + " is bust!");
            System.out.println();
        }
    }

    private void handleHumanPlayerTurn(Scanner scanner, Player player, Deck deck) { //사람 플레이어 턴 진행
        if (!player.isStand() && !player.isBust()) {
            System.out.println(player.getName() + "'s turn! Score: " + player.getCardScore());
            System.out.print("Do you want to hit or stand? (h/s): ");
            String action = scanner.nextLine();

            switch (action.toLowerCase()) {
                case "h": //hit (카드 뽑기)
                    player.addCard(deck.drawCard());
                    break;
                case "s": //stand (턴 종료)
                    player.setStand(true);
                    System.out.println(player.getName() + " stands!");
                    break;
                default: //잘못된 입력
                    System.out.println("Invalid input. Please enter h or s.");
            }
        } else if (player.isBust()) {
            System.out.println(player.getName() + " Bust!");
        } else if (player.isStand()) {
            System.out.println(player.getName() + " stands!");
        }
    }

    private void handleAIPlayerTurn(Player player, Deck deck) { //AI 플레이어 턴 진행
        System.out.println(player.getName() + "'s turn! Score: " + player.getCardScore());

        if (player.getCardScore() < AI_STAND_THRESHOLD && !player.isBust() && !player.isStand()) {
            player.addCard(deck.drawCard());
        } else if (player.isBust()) {
            System.out.println(player.getName() + " Bust!");
        } else {
            player.setStand(true);
            System.out.println(player.getName() + " stands!");
        }
    }

    private boolean isGameRoundComplete(List<Player> players) { //게임 라운드 종료 체크 (모든 플레이어가 할 수 있는게 없을 때)
        for (Player player : players) {
            if (!player.isStand() && !player.isBust() && player.getCardScore() < BLACKJACK_SCORE) {
                return false;
            }
        }
        return true;
    }

    private void determineWinners(List<Player> players) { //승자 결정
        int maxScore = calculateMaxValidScore(players);

        if (maxScore > 0) {
            announceWinners(players, maxScore);
        } else {
            System.out.println("No winner!");
        }
    }

    private int calculateMaxValidScore(List<Player> players) { //가장 높은 점수 계산
        int maxScore = 0;
        for (Player player : players) {
            int playerScore = player.getCardScore();
            if (playerScore <= BLACKJACK_SCORE && playerScore > maxScore) {
                maxScore = playerScore;
            }
        }
        return maxScore;
    }

    private void announceWinners(List<Player> players, int maxScore) { //승자 출력하는 메서드
        boolean hasWinner = false;
        for (Player player : players) {
            if (player.getCardScore() == maxScore) {
                player.setWin(true);
                System.out.println(player.getName() + " wins! Score: " + player.getCardScore());
                System.out.println();
                hasWinner = true;
            }
        }
        
        if (!hasWinner) {
            System.out.println("No winner!");
        }
    }
}