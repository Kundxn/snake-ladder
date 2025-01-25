package com.game.snakeladder.service;

import com.game.snakeladder.model.Game;
import com.game.snakeladder.model.GameMove;
import com.game.snakeladder.model.Player;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GameService {
    private final Map<String, Game> games = new HashMap<>();

    public Game startNewGame(String[] playerNames) {
        if (playerNames == null || playerNames.length < 2) {
            throw new IllegalArgumentException("A minimum of 2 players is required to start a game.");
        }

        Game game = new Game();
        game.setId(UUID.randomUUID().toString());

        List<Player> players = new ArrayList<>();
        for (String name : playerNames) {
            Player player = new Player();
            player.setId(UUID.randomUUID().toString());
            player.setName(name);
            player.setPosition(1);  // All players start at position 1
            players.add(player);
        }

        game.setPlayers(players);
        game.setSnakes(initializeSnakes());
        game.setLadders(initializeLadders());
        game.setCurrentPlayerId(players.get(0).getId());
        game.setFinished(false);

        games.put(game.getId(), game);
        return game;
    }

    public GameMove makeMove(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Game not found.");
        }
        if (!game.getCurrentPlayerId().equals(playerId)) {
            throw new IllegalStateException("It's not the player's turn.");
        }

        Player currentPlayer = game.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Player not found in the game."));

        int diceValue = rollDice();
        int newPosition = calculateNewPosition(currentPlayer.getPosition(), diceValue);

        GameMove move = new GameMove();
        move.setDiceValue(diceValue);
        move.setPreviousPosition(currentPlayer.getPosition());
        move.setNewPosition(newPosition);

        // Check for snakes or ladders
        if (game.getSnakes().containsKey(newPosition)) {
            newPosition = game.getSnakes().get(newPosition);
            move.setSnake(true);
        } else if (game.getLadders().containsKey(newPosition)) {
            newPosition = game.getLadders().get(newPosition);
            move.setLadder(true);
        }

        currentPlayer.setPosition(newPosition);
        if (newPosition == 100) {
            game.setFinished(true);
            game.setWinner(currentPlayer);
            move.setHasWon(true);
        } else {
            updateNextPlayer(game);
        }

        return move;
    }

    private Map<Integer, Integer> initializeSnakes() {
        return Map.of(
                99, 54,
                70, 55,
                52, 42,
                25, 2
        );
    }

    private Map<Integer, Integer> initializeLadders() {
        return Map.of(
                6, 25,
                11, 40,
                60, 85,
                46, 90
        );
    }

    private int rollDice() {
        return new Random().nextInt(6) + 1;
    }

    private int calculateNewPosition(int currentPosition, int diceValue) {
        int newPosition = currentPosition + diceValue;
        return Math.min(newPosition, 100);
    }

    private void updateNextPlayer(Game game) {
        List<Player> players = game.getPlayers();
        int currentPlayerIndex = players.indexOf(
                players.stream()
                        .filter(p -> p.getId().equals(game.getCurrentPlayerId()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalStateException("Current player not found."))
        );
        int nextPlayerIndex = (currentPlayerIndex + 1) % players.size();
        game.setCurrentPlayerId(players.get(nextPlayerIndex).getId());
    }

    public Game getGame(String gameId) {
        return games.get(gameId);
    }
}
