package com.game.snakeladder.controller;

import com.game.snakeladder.model.Game;
import com.game.snakeladder.model.GameMove;
import com.game.snakeladder.service.GameService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/game")
public class GameController {
    private final GameService gameService;

    public GameController(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/start")
    public ResponseEntity<Game> startGame(@RequestBody String[] playerNames) {
        Game game = gameService.startNewGame(playerNames);
        return ResponseEntity.ok(game);
    }

    @PostMapping("/{gameId}/move/{playerId}")
    public ResponseEntity<GameMove> makeMove(
            @PathVariable String gameId,
            @PathVariable String playerId
    ) {
        GameMove move = gameService.makeMove(gameId, playerId);
        return ResponseEntity.ok(move);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<Game> getGame(@PathVariable String gameId) {
        return ResponseEntity.ok(gameService.getGame(gameId));
    }
}
