package com.bol.game.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

import static com.bol.game.model.PlayerTurn.PLAYER_1;
import static java.util.UUID.randomUUID;

@Data
@Builder
public class Game implements Serializable {

    @Builder.Default
    private UUID id = randomUUID();
    private GameState gameState;
    @Builder.Default
    private PlayerTurn playerTurn = PLAYER_1;
    @Builder.Default
    private Player player1 = Player.builder().build();
    @Builder.Default
    private Player player2 = Player.builder().build();

    public Player getPlayer(PlayerTurn playerTurn) {
        switch (playerTurn) {
            case PLAYER_1: return getPlayer1();
            case PLAYER_2: return getPlayer2();
            default: throw new RuntimeException("Invalid turn");
        }
    }

    public Player getOpponent(PlayerTurn playerTurn) {
        switch (playerTurn) {
            case PLAYER_1: return getPlayer2();
            case PLAYER_2: return getPlayer1();
            default: throw new RuntimeException("Invalid turn");
        }
    }

    public void updatePlayers(Player player, Player opponent, PlayerTurn playerTurn) {
        switch (playerTurn) {
            case PLAYER_1:
                this.setPlayer1(player);
                this.setPlayer2(opponent);
                break;
            case PLAYER_2:
                this.setPlayer1(opponent);
                this.setPlayer2(player);
                break;
            default: throw new RuntimeException("Invalid turn");
        }
    }
}
