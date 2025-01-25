package com.game.snakeladder.model;

import lombok.Data;

@Data
public class GameMove {
    private int diceValue;
    private int previousPosition;
    private int newPosition;
    private boolean snake;
    private boolean ladder;
    private boolean hasWon;
}
