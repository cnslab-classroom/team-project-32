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
    private static int logIdx = 1; // �α� �ε��� �ʱ�ȭ
    private static String userInput = null; // ����� �Է��� ������ ����

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private JPanel gamePanel;
    private JPanel setupPanel;
    private JComboBox<Integer> deckCountComboBox; // JComboBox�� ����
    private JButton startButton;

    private Frame() {
        // �⺻ ����
        setTitle("BlackJack Game");
        setSize(800, 600); // â ũ�� ����
        setResizable(false);  // â ũ�� ����
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // CardLayout ����
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // ���� �г� (�� ���� ����)
        setupPanel = new JPanel();
        setupPanel.setLayout(new GridBagLayout());

        //gridLayOut ������Ʈ ��ġ ���� ��ü
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);

        //deck ���� ���� �޺� �ڽ� �߰�
        JLabel deckLabel = new JLabel("Select number of decks (1-4): ");
        Integer[] deckOptions = {1, 2, 3, 4};
        deckCountComboBox = new JComboBox<>(deckOptions); // JComboBox ���
        deckCountComboBox.setSelectedIndex(0); // �⺻�� 1

        startButton = new JButton("Start Game");

        //�� ���� label
        gbc.gridx = 0;
        gbc.gridy = 0;
        setupPanel.add(deckLabel, gbc);

        //�� ���� combobox
        gbc.gridx = 1;
        setupPanel.add(deckCountComboBox, gbc);

        //gmaestart button
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        setupPanel.add(startButton, gbc);

        // �� ���� �г�
        gamePanel = new JPanel(new BorderLayout());

        // �ش� �κ��� �� �ȵ�..
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());
        try {
            URL imageUrl = getClass().getResource("/dealer.png");
            if (imageUrl != null) {
                ImageIcon dealerIcon = new ImageIcon(imageUrl);

                // �̹����� ũ�⿡ �°� ����
                Image scaledImage = dealerIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                ImageIcon scaledIcon = new ImageIcon(scaledImage);

                // �󺧿� ������ �̹��� �߰�
                JLabel dealerLabel = new JLabel(scaledIcon);
                topPanel.add(dealerLabel);
            }
        } catch (Exception e) {
            e.printStackTrace();
            topPanel.add(new JLabel("Error loading dealer image"));
        }
        gamePanel.add(topPanel, BorderLayout.NORTH);


        // borderLayOut �¿� playerLog, �쿡 AiLog
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(1, 2, 10, 10));
        

        // User �α� �г�
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BorderLayout());
        userPanel.setBorder(BorderFactory.createTitledBorder("User Log"));
        userLogArea = new JTextArea();
        userLogArea.setEditable(false);
        userLogArea.setLineWrap(true);  // �ؽ�Ʈ ���� �Ѿ�� �ٹٲ�
        userLogArea.setWrapStyleWord(true); // �ܾ� ������ �ٹٲ�
        JScrollPane userScrollPane = new JScrollPane(userLogArea);
        userPanel.add(userScrollPane, BorderLayout.CENTER);
        centerPanel.add(userPanel);

        // AI �α� �г�
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

        // �ϴ� �Է� �г�
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputField = new JTextField(30);
        JButton sendButton = new JButton("Enter");
        inputPanel.add(inputField);
        inputPanel.add(sendButton);

        gamePanel.add(inputPanel, BorderLayout.SOUTH);

        // ���� �г��� mainPanel�� �߰�
        mainPanel.add(setupPanel, "SETUP");
        mainPanel.add(gamePanel, "GAME");

        add(mainPanel, BorderLayout.CENTER);

        // �� ���� ���� �� ���� ��ư �׼� ������ �߰�
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int deckCount = (Integer) deckCountComboBox.getSelectedItem();
                
                // ī�� ���̾ƿ��� ���� ���� �гη� ��ȯ
                cardLayout.show(mainPanel, "GAME");
                mainPanel.revalidate(); // ������� ����
                mainPanel.repaint(); // ����� ���� �гο� ����
                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        // �ش� �۾� ������ ������
                        // combobox ���ý� player thread�� ���ư��� ���� �гη� �Ѿ�� ����
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

        // �Է� �ʵ� �׼� ������ �߰�
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (Frame.class) {
                    String input = inputField.getText().trim();
                    if (!input.isEmpty()) {
                        userInput = input;
                        Frame.appendUserLog("Player input: " + input);
                        Frame.class.notifyAll(); // ��� ���� ������ �����
                        inputField.setText("");
                    }
                }
            }
        });

        // ����Ű�ε� �Է� �����ϰ� ����
        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendButton.doClick();
            }
        });

        setVisible(true);
    }

    // �̱��� ����
    private static class SingletonHolder {
        private static final Frame INSTANCE = new Frame();
    }

    public static Frame getInstance() {
        return SingletonHolder.INSTANCE;
    }

    // ����� �α׿� �޽��� �߰� (�ε��� ����)
    public static synchronized void appendUserLog(String message) {
        userLogArea.append("[" + logIdx + "] " + message + "\n\n");
        logIdx++;
    }

    // AI �α׿� �޽��� �߰� (�ε��� ����)
    public static synchronized void appendAiLog(String message) {
        aiLogArea.append("[" + logIdx + "] " + message + "\n\n");
        logIdx++;
    }

    // ����� �Է��� �������� �޼ҵ�
    public static synchronized String getUserInput() {
        while (userInput == null) {
            try {
                Frame.class.wait(); // �Է��� �� ������ ���
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        String input = userInput;
        userInput = null; // �Է� ó�� �� �ʱ�ȭ
        return input;
    }

    // �α� �ʱ�ȭ �޼ҵ�
    public static synchronized void clearLogs() {
        userLogArea.setText("");
        aiLogArea.setText("");
        logIdx = 1;
    }
}
