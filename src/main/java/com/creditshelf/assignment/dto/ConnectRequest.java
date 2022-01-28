package com.creditshelf.assignment.dto;

import com.creditshelf.assignment.model.Player;
import lombok.Data;

@Data
public class ConnectRequest {

    private Player player;
    private String gameId;

}
