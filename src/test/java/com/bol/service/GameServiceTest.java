package com.bol.service;

import com.bol.game.model.BoardSide;
import com.bol.game.model.Game;
import com.bol.game.model.GameState;
import com.bol.game.model.Player;
import com.bol.game.model.PlayerTurn;
import com.bol.game.model.Sow;
import com.bol.game.service.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class GameServiceTest {

    @InjectMocks
    GameService gameService;

    private Game game;

    @Before
    @Test
    public void createNewGame() {
        UUID newGameId = randomUUID();
        game = gameService.getGame(newGameId);

        assertEquals(newGameId, game.getId());
        assertEquals(GameState.IN_PROGRESS, game.getGameState());
        assertEquals(PlayerTurn.PLAYER_1, game.getPlayerTurn());
    }

    @Test
    public void getExistingGame() {
        Game retrievedGame = gameService.getGame(game.getId());
        assertEquals(retrievedGame, game);
    }

    @Test
    public void updatedGamePlayerCaptured() {
        Player player = Player.builder().build();
        player.getPits().get(0).setStones(1);
        Player opponent = Player.builder().build();
        opponent.getPits().get(5).setStones(10);

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(0);

        Game updatedGame = gameService.updateSowedGame(game.getId(), player, opponent, sow, BoardSide.PLAYER, 0);

        assertEquals(GameState.IN_PROGRESS, updatedGame.getGameState());
        assertEquals(0, (int)updatedGame.getPlayer1().getStonesOnPit(0));
        assertEquals(0, (int)updatedGame.getPlayer2().getStonesOnPit(5));
        assertEquals(11, (int)updatedGame.getPlayer1().getStonesOnMainPit());
    }

    @Test
    public void updatedGamePlayer1Won() {
        Player player = Player.builder().build();
        player.getPits().forEach(pit -> pit.setStones(0));
        player.addStonesToMainPit(10);
        Player opponent = Player.builder().build();
        opponent.getPits().forEach(pit -> pit.setStones(0));
        opponent.addStonesToMainPit(1);

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(1);

        Game updatedGame = gameService.updateSowedGame(game.getId(), player, opponent, sow, BoardSide.OPPONENT, 0);

        assertEquals(GameState.PLAYER_1_WON, updatedGame.getGameState());
    }

    @Test
    public void updatedGamePlayer2Won() {
        Player player = Player.builder().build();
        player.getPits().forEach(pit -> pit.setStones(0));
        player.addStonesToMainPit(1);
        Player opponent = Player.builder().build();
        opponent.getPits().forEach(pit -> pit.setStones(0));
        opponent.addStonesToMainPit(10);

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(1);

        Game updatedGame = gameService.updateSowedGame(game.getId(), player, opponent, sow, BoardSide.OPPONENT, 0);

        assertEquals(GameState.PLAYER_2_WON, updatedGame.getGameState());
    }

    @Test
    public void updatedGameTied() {
        Player player = Player.builder().build();
        player.getPits().forEach(pit -> pit.setStones(0));
        player.addStonesToMainPit(1);
        Player opponent = Player.builder().build();
        opponent.getPits().forEach(pit -> pit.setStones(0));
        opponent.addStonesToMainPit(1);

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(1);

        Game updatedGame = gameService.updateSowedGame(game.getId(), player, opponent, sow, BoardSide.OPPONENT, 0);

        assertEquals(GameState.GAME_TIED, updatedGame.getGameState());
    }

    @Test
    public void updatedGamePlayerContinuesTurn() {
        Player player = Player.builder().build();
        Player opponent = Player.builder().build();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(0);

        Game updatedGame = gameService.updateSowedGame(game.getId(), player, opponent, sow, BoardSide.PLAYER, 6);

        assertEquals(GameState.IN_PROGRESS, updatedGame.getGameState());
        assertEquals(PlayerTurn.PLAYER_1, updatedGame.getPlayerTurn());
    }

    @Test
    public void updatedGameChangedPlayerTurn() {
        Player player = Player.builder().build();
        Player opponent = Player.builder().build();

        Sow sow = new Sow();
        sow.setPlayerTurn(PlayerTurn.PLAYER_1);
        sow.setPitNumber(1);

        Game updatedGame = gameService.updateSowedGame(game.getId(), player, opponent, sow, BoardSide.OPPONENT, 0);

        assertEquals(GameState.IN_PROGRESS, updatedGame.getGameState());
        assertEquals(PlayerTurn.PLAYER_2, updatedGame.getPlayerTurn());
    }
}
