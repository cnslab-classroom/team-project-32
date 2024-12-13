package org.example;

public class Card {
    private final String suit; //ī���� ���(��Ʈ, ���̾Ƹ��, Ŭ��, �����̵�)
    private final String rank; //ī���� ����(2~10, J, Q, K, A)
    private final int value; //ī���� ����(1~11)
    
    public Card(String suit, String rank) { //ī�� ������
        this.suit = suit;
        this.rank = rank;
        
        switch (rank) { //ī�� ���ڿ� ���� ���� �ο�
            case "A": //A�� 11�� ���
                this.value = 11;
                break;
            case "J":
            case "Q":
            case "K":
                this.value = 10; //J, Q, K�� 10���� ���
                break;
            default:
                this.value = Integer.parseInt(rank); //������ ���ڴ� ���� �״�� ���
                break;
        }
    }

    public String getSuit() { //ī���� getter
        return suit;
    }

    public String getRank() { //ī����� getter
        return rank;
    }

    public int getValue() { //ī������ getter
        return value;
    }

    public String printCard() { //ī�� ���� ���
        return suit + " / " + rank;
    }
}