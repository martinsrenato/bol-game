package com.bol.game.service;

import com.bol.game.model.Game;
import org.springframework.stereotype.Service;

import static com.bol.game.model.GameState.NEW_GAME;

@Service
public class GameService {

    public Game startNewGame() {
        return Game.builder()
                .gameState(NEW_GAME)
                .build();
    }
}
