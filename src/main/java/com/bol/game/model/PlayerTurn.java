package com.bol.game.model;

public enum PlayerTurn {
    PLAYER_1, PLAYER_2;

    public static PlayerTurn nextPlayerTurn(PlayerTurn currentPlayerTurn) {
        switch (currentPlayerTurn) {
            case PLAYER_1: return PLAYER_2;
            case PLAYER_2: return PLAYER_1;
            default: throw new RuntimeException("Invalid player turn");
        }
    }
}
