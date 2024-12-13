package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

public class Frame extends JFrame {
    private static JTextArea userLogArea;
    private static JTextArea aiLogArea;
    private static JTextField inputField;
    private static int logIdx = 1; // 로그 인덱스 초기화
    private static String userInput = null; // 사용자 입력을 저장할 변수

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel setupPanel;
    private JComboBox<Integer> deckCountComboBox; // JComboBox로 변경
    private JButton startButton;

    private Frame() {
        // 기본 설정
        setTitle("BlackJack Game");
        setSize(800, 600); // 창 크기 조정
        setResizable(false);  // 창 크기 고정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // CardLayout 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 설정 패널 (덱 개수 선택)
        setupPanel = new JPanel();
        setupPanel.setLayout(new GridBagLayout());

        //gridLayOut 컴포넌트 위치 제어 객체
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        //deck 개수 고르는 콤보 박스 추가
        JLabel deckLabel = new JLabel("Select number of decks (1-4): ");
        Integer[] deckOptions = {1, 2, 3, 4};
        deckCountComboBox = new JComboBox<>(deckOptions); // JComboBox 사용
        deckCountComboBox.setSelectedIndex(0); // 기본값 1

        startButton = new JButton("Start Game");

        //덱 개수 label
        gbc.gridx = 0;
        gbc.gridy = 0;
        setupPanel.add(deckLabel, gbc);

        //덱 개수 combobox
        gbc.gridx = 1;
        setupPanel.add(deckCountComboBox, gbc);

        //gmaestart button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        setupPanel.add(startButton, gbc);

        // 본 게임 패널
        gamePanel = new JPanel(new BorderLayout());

        // 해당 부분이 잘 안됨..
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        try {
            URL imageUrl = getClass().getResource("/dealer.png");
            if (imageUrl != null) {
                ImageIcon dealerIcon = new ImageIcon(imageUrl);

                // 이미지를 크기에 맞게 조정
                Image scaledImage = dealerIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                // 라벨에 조정된 이미지 추가
                JLabel dealerLabel = new JLabel(scaledIcon);
                topPanel.add(dealerLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            topPanel.add(new JLabel("Error loading dealer image"));
        }
        gamePanel.add(topPanel, BorderLayout.NORTH);


        // borderLayOut 좌에 playerLog, 우에 AiLog
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        

        // User 로그 패널
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("User Log"));
        userLogArea = new JTextArea();
        userLogArea.setEditable(false);
        userLogArea.setLineWrap(true);  // 텍스트 길이 넘어가면 줄바꿈
        userLogArea.setWrapStyleWord(true); // 단어 단위로 줄바꿈
        JScrollPane userScrollPane = new JScrollPane(userLogArea);
        userPanel.add(userScrollPane, BorderLayout.CENTER);
        centerPanel.add(userPanel);

        // AI 로그 패널
        JPanel aiPanel = new JPanel();
        aiPanel.setLayout(new BorderLayout());
        aiPanel.setBorder(BorderFactory.createTitledBorder("AI Log"));
        aiLogArea = new JTextArea();
        aiLogArea.setEditable(false);
        aiLogArea.setLineWrap(true);
        aiLogArea.setWrapStyleWord(true);
        JScrollPane aiScrollPane = new JScrollPane(aiLogArea);
        aiPanel.add(aiScrollPane, BorderLayout.CENTER);
        centerPanel.add(aiPanel);

        gamePanel.add(centerPanel, BorderLayout.CENTER);

        // 하단 입력 패널
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputField = new JTextField(30);
        JButton sendButton = new JButton("Enter");
        inputPanel.add(inputField);
        inputPanel.add(sendButton);

        gamePanel.add(inputPanel, BorderLayout.SOUTH);

        // 게임 패널을 mainPanel에 추가
        mainPanel.add(setupPanel, "SETUP");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel, BorderLayout.CENTER);

        // 덱 개수 선택 후 시작 버튼 액션 리스너 추가
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int deckCount = (Integer) deckCountComboBox.getSelectedItem();
                
                // 카드 레이아웃을 통해 게임 패널로 전환
                cardLayout.show(mainPanel, "GAME");
                mainPanel.revalidate(); // 변경사항 적용
                mainPanel.repaint(); // 변경된 내용 패널에 적용
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // 해당 작업 해주지 않으면
                        // combobox 선택시 player thread만 돌아가서 다음 패널로 넘어가지 않음
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                GameManager.getInstance().initializeGame(deckCount);
                            }
                        }).start();
                    }
                });
            }
        });

        // 입력 필드 액션 리스너 추가
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (Frame.class) {
                    String input = inputField.getText().trim();
                    if (!input.isEmpty()) {
                        userInput = input;
                        Frame.appendUserLog("Player input: " + input);
                        Frame.class.notifyAll(); // 대기 중인 스레드 깨우기
                        inputField.setText("");
                    }
                }
            }
        });

        // 엔터키로도 입력 가능하게 설정
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        setVisible(true);
    }

    // 싱글턴 패턴
    private static class SingletonHolder {
        private static final Frame INSTANCE = new Frame();
    }

    public static Frame getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // 사용자 로그에 메시지 추가 (인덱스 포함)
    public static synchronized void appendUserLog(String message) {
        userLogArea.append("[" + logIdx + "] " + message + "\n\n");
        logIdx++;
    }

    // AI 로그에 메시지 추가 (인덱스 포함)
    public static synchronized void appendAiLog(String message) {
        aiLogArea.append("[" + logIdx + "] " + message + "\n\n");
        logIdx++;
    }

    // 사용자 입력을 가져오는 메소드
    public static synchronized String getUserInput() {
        while (userInput == null) {
            try {
                Frame.class.wait(); // 입력이 올 때까지 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String input = userInput;
        userInput = null; // 입력 처리 후 초기화
        return input;
    }

    // 로그 초기화 메소드
    public static synchronized void clearLogs() {
        userLogArea.setText("");
        aiLogArea.setText("");
        logIdx = 1;
    }
}
