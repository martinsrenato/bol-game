package com.bol.game.service;

import com.bol.game.model.BoardSide;
import com.bol.game.model.Game;
import com.bol.game.model.GameState;
import com.bol.game.model.Player;
import com.bol.game.model.PlayerTurn;
import com.bol.game.model.Sow;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.bol.game.model.GameState.GAME_TIED;
import static com.bol.game.model.GameState.IN_PROGRESS;
import static com.bol.game.model.GameState.PLAYER_1_WON;
import static com.bol.game.model.GameState.PLAYER_2_WON;

@Service
@CacheConfig(cacheNames = "games")
public class GameService {

    @Cacheable(key = "#gameId")
    public Game getGame(UUID gameId) {
        return Game.builder()
                .id(gameId)
                .gameState(IN_PROGRESS)
                .build();
    }

    @CachePut(key = "#result.id")
    public Game updateSowedGame(UUID gameId, Player player, Player opponent, Sow sow, BoardSide side, Integer lastPitSowed) {
        Game game = getGame(gameId);
        game.updatePlayers(player, opponent, sow.getPlayerTurn());

        game = checkCapture(game, sow, side, lastPitSowed);
        game = updateGameState(game, sow);
        game = updatePlayerTurn(game, sow, side, lastPitSowed);

        return game;
    }

    private Game checkCapture(Game game, Sow sow, BoardSide side, Integer lastPitSowed) {
        Player player = game.getPlayer(sow.getPlayerTurn());
        Player opponent = game.getOpponent(sow.getPlayerTurn());

        if(side == BoardSide.PLAYER
                && lastPitSowed < 6
                && player.getStonesOnPit(lastPitSowed) == 1) {
            Integer capturedStones = 1 + opponent.getStonesOnPit(5 - lastPitSowed);
            opponent.clearPit(5 - lastPitSowed);
            player.clearPit(lastPitSowed);
            player.addStonesToMainPit(capturedStones);

            game.updatePlayers(player, opponent, sow.getPlayerTurn());
        }

        return game;
    }

    private Game updateGameState(Game game, Sow sow) {
        Player player = game.getPlayer(sow.getPlayerTurn());
        Player opponent = game.getOpponent(sow.getPlayerTurn());

        GameState gameState = IN_PROGRESS;

        Boolean gameOver = isGameOver(player, opponent);
        if(gameOver) {
            player.finishGame();
            opponent.finishGame();

            if(player.getStonesOnMainPit() > opponent.getStonesOnMainPit()) {
                gameState = sow.getPlayerTurn().equals(PlayerTurn.PLAYER_1) ? PLAYER_1_WON : PLAYER_2_WON;
            } else if (player.getStonesOnMainPit() < opponent.getStonesOnMainPit()) {
                gameState = sow.getPlayerTurn().equals(PlayerTurn.PLAYER_1) ? PLAYER_2_WON : PLAYER_1_WON;
            } else {
                gameState = GAME_TIED;
            }
        }

        game.updatePlayers(player, opponent, sow.getPlayerTurn());
        game.setGameState(gameState);

        return game;
    }


    private boolean isGameOver(Player player, Player opponent) {
        return player.getPits().stream().allMatch(pit -> pit.getStones() == 0)
                || opponent.getPits().stream().allMatch(pit -> pit.getStones() == 0);
    }

    private Game updatePlayerTurn(Game game, Sow sow, BoardSide side, Integer lastPitSowed) {
        Boolean playAgain = (side == BoardSide.PLAYER && lastPitSowed == 6);
        PlayerTurn nextPlayerTurn;
        if(playAgain) {
            nextPlayerTurn = sow.getPlayerTurn();
        } else {
            nextPlayerTurn = PlayerTurn.nextPlayerTurn(sow.getPlayerTurn());
        }

        game.setPlayerTurn(nextPlayerTurn);

        return game;
    }
}
