package com.creditshelf.assignment.dto;

import com.creditshelf.assignment.constants.Symbol;
import lombok.Data;

@Data
public class GamePlay {

    private String gameId;
    private Integer coordinateX;
    private Integer coordinateY;
    private Symbol turn;

}
