package org.example;

import java.util.concurrent.TimeUnit;

public class GameManager {
    private static GameManager instance;
    private Player user;
    private Player Ai;
    private Dealer dealer;
    private int deckCount;

    private GameManager() {
        //�̱��� ����
    }

    public static synchronized GameManager getInstance() {
        if (instance == null) {
            instance = new GameManager();
        }
        return instance;
    }

    public void initializeGame(int deckCount) {
        this.deckCount = deckCount;
        dealer = Dealer.getInstance(deckCount);
        if (user == null && Ai == null) {
            this.user = new Player("Human", true);
            this.Ai = new Player("AI", false);
        }
        else 
            resetGame();
        startGame();
}

    public void resetGame() {
        user.reset();
        Ai.reset();
        dealer.resetDeck(deckCount);
    }

    public void startGame() {
        Frame.appendUserLog("Starting game...");

        // ���� �ʱ�ȭ �α� ���
        Frame.appendUserLog("Dealing Cards....");
        
        // �������� �� ���� ī�� ���
        for(int i = 0; i < 2; i++) {
            dealer.dealCard(Ai);
        }

        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < 2; i++) {
            dealer.dealCard(user);
        }

        if(checkInitialState()) {
            return;
        }

        Frame.appendUserLog("Game initialized. Ready to play.");
        Thread userThread = new Thread(user);
        Thread aiThread = new Thread(Ai);

        aiThread.start();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        userThread.start();

        // �� �÷��̾ ��� ���� ������ join..
        try {
            userThread.join();
            aiThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // �÷��̾� �ൿ ���� �� ���� ����
        determineWinner();

        // ���� ���� �� ����� ���� Ȯ��
        restart();
    }

    // ���� �� ������ ����
    private boolean checkInitialState() {
        boolean isEnded = false;

        if (user.isBlackJack() && Ai.isBlackJack()) {
            Frame.appendUserLog("Both players made BlackJack! It's a draw!");
            isEnded = true;
        } else if (user.isBlackJack()) {
            Frame.appendUserLog("You made BlackJack! You Win!");
            isEnded = true;
        } else if (Ai.isBlackJack()) {
            Frame.appendAiLog("AI made BlackJack! You Lose!");
            isEnded = true;
        }

        return isEnded;
    }

    private void determineWinner() {
        if (user.isBust()) {
            Frame.appendUserLog("You are bust! You Lose.");
        } else if (Ai.isBust()) {
            Frame.appendUserLog("AI is bust! You Win!");
        } else if (user.getCardScore() > Ai.getCardScore()) {
            Frame.appendUserLog("You Win!");
        } else if (user.getCardScore() < Ai.getCardScore()) {
            Frame.appendUserLog("You Lose!");
        } else {
            Frame.appendUserLog("It's a draw!");
        }
    }
<<<<<<< HEAD
}
=======

    private void restart() {
        Frame.appendUserLog("Press 'r' to restart the game.");
        Frame.appendAiLog("Press 'r' to restart the game.");

        String input = Frame.getUserInput();
        if (input.equalsIgnoreCase("r")) {
            Frame.clearLogs();
            initializeGame(deckCount);
        } else {
            Frame.appendUserLog("Exiting game.");
            System.exit(0);
        }
    }
        public int getDeckCount() {
        return this.deckCount;
    }
}
>>>>>>> a56d42c9b0a9d74514de7209f201e47f26c64b6f
