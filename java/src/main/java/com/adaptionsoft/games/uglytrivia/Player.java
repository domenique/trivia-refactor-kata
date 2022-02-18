package com.adaptionsoft.games.uglytrivia;

public class Player {
    private final String name;
    private int position;

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
}
