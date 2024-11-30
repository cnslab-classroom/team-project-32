package org.example;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {
    private static JTextArea logArea;
    private static JTextField inputField;

    private Frame() {
        //기본 설정
        setTitle("BlackJack Game");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 로그 출력 영역
        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);


        // 입력 영역
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new FlowLayout());

        inputField = new JTextField(20);
        inputField.setEnabled(true); // 입력 필드 기본 활성화
        inputPanel.add(inputField);

        add(inputPanel, BorderLayout.SOUTH);
        setVisible(true);
    }

    // 딜러와 마찬가지로 싱글턴, 객체가 1개만 존재하고 외부에서 접근하게
    private static class SingletonHolder {
        private static final Frame INSTANCE = new Frame();
    }

    public static Frame getInstance() {
        return SingletonHolder.INSTANCE;
    }

    //로그 출력
    public static void appendLog(String message) {
        SwingUtilities.invokeLater(() -> logArea.append(message + "\n"));
    }

    public static String getUserInput() {
        while (true) {
            if (!inputField.getText().isEmpty()) {
                String input = inputField.getText().trim();
                inputField.setText(""); // 입력 필드 초기화
                return input;
            }
            try {
                Thread.sleep(100); // 입력 대기
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
