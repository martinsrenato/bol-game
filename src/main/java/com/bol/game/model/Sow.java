package com.bol.game.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
@NoArgsConstructor
public class Sow implements Serializable {
    private PlayerTurn playerTurn;
    private Integer pitNumber;
}
