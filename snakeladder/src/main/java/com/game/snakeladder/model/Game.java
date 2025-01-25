package com.game.snakeladder.model;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class Game {
    private String id;
    private List<Player> players;
    private Map<Integer, Integer> snakes;
    private Map<Integer, Integer> ladders;
    private String currentPlayerId;
    private boolean finished;
    private Player winner;

    // Manually add the getter and setter for id
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Add other getters and setters as needed for other fields
}

