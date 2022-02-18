package com.adaptionsoft.games.uglytrivia;

public class Player {
    private final String name;
    private int position;
    private int score;
    private boolean isInPenaltyBox;

    public Player(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void move(int roll) {
        position += roll;
    }

    public int position() {
        return position % 12;
    }

    public void addCoin() {
        score++;
    }

    public int score() {
        return score;
    }

    boolean hasWon() {
        return score() == 6;
    }

    public void toPenaltyBox() {
        isInPenaltyBox = true;
    }

    public boolean isInPenaltyBox() {
        return isInPenaltyBox;
    }
}
