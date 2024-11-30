package org.example;

import java.util.concurrent.TimeUnit;

public class GameManager {
    private final Player user;
    private final Player Ai;
    public GameManager() {
        Frame.getInstance(); // Frame 초기화
        this.user = new Player("Human", true);
        this.Ai = new Player("AI", false);
    }

    public void startGame() {
        Frame.appendLog("Starting game...");

        // 게임 초기화 로그 출력
        Frame.appendLog("Dealing Cards....");
        Dealer dealer = Dealer.getInstance();
        // Ai 실행 후 User 실행
        for(int i = 0; i < 2; i++)
            dealer.dealCard(Ai);

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 2; i++)
            dealer.dealCard(user);

        if(checkInitialState()) {
            return;
        }

        Frame.appendLog("Game initialized. Ready to play.");
        Thread userThread = new Thread(user);
        Thread aiThread = new Thread(Ai);

        aiThread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userThread.start();

        //두 플레이어가 모두 끝날 때까지 join..
        try {
            userThread.join();
            aiThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //플레이어 행동 끝난 후 승패 결정
        determineWinner();
    }

    // 시작 후 블랙잭 판정
    private boolean checkInitialState() {
        boolean isEnded = false;

        if (user.isBlackJack() && Ai.isBlackJack()) {
            Frame.appendLog("Both players made BlackJack! It's a draw!");
            isEnded = true;
        } else if (user.isBlackJack()) {
            Frame.appendLog("You made BlackJack! You Win!");
            isEnded = true;
        } else if (Ai.isBlackJack()) {
            Frame.appendLog("AI made BlackJack! You Lose!");
            isEnded = true;
        }

        return isEnded;
    }

    private void determineWinner() {
        if (user.isBust()) {
            Frame.appendLog("You are bust! You Lose.");
        } else if (Ai.isBust()) {
            Frame.appendLog("AI is bust! You Win!");
        } else if (user.getCardScore() > Ai.getCardScore()) {
            Frame.appendLog("You Win!");
        } else if (user.getCardScore() < Ai.getCardScore()) {
            Frame.appendLog("You Lose!");
        } else {
            Frame.appendLog("It's a draw!");
        }
    }
}