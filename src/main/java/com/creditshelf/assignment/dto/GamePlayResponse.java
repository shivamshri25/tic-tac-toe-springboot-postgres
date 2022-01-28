package com.creditshelf.assignment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GamePlayResponse {

    private String gameId;
    private String player1;
    private String player2;
    private String status;
    private Integer[][] board;
    private String next;
}
