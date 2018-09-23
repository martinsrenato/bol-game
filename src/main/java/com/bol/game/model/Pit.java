package com.bol.game.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class Pit implements Serializable {
    private Integer stones;

    public void addStone() {
        this.stones++;
    }

    public void addStones(Integer newStones) {
        this.stones += newStones;
    }

    public void clearPit() {
        this.stones = 0;
    }
}
