package com.creditshelf.assignment.controller;

import com.creditshelf.assignment.constants.GameStates;
import com.creditshelf.assignment.constants.Symbol;
import com.creditshelf.assignment.dto.ConnectRequest;
import com.creditshelf.assignment.dto.GamePlay;
import com.creditshelf.assignment.dto.GamePlayResponse;
import com.creditshelf.assignment.exception.GameNotFoundException;
import com.creditshelf.assignment.exception.InvalidGameException;
import com.creditshelf.assignment.exception.InvalidParamException;
import com.creditshelf.assignment.model.Game;
import com.creditshelf.assignment.model.Player;
import com.creditshelf.assignment.service.GameService;
import com.creditshelf.assignment.util.JDBCUtil;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {

    @Autowired
    private GameService gameService;

    private final JDBCUtil util;

    @PostMapping(value = "/create")
    public ResponseEntity<Game> createGame(@NotNull @RequestBody Player player) {
        return ResponseEntity.ok(gameService.createGame(player));
    }

    @GetMapping(value = "/getGames")
    public List<Game> getAllGames() {
        return gameService.getGames();
    }

    @PostMapping(value = "/connect")
    public ResponseEntity<Game> connectExistingGame(@RequestBody ConnectRequest connectRequest)
            throws InvalidParamException, InvalidGameException {
        return ResponseEntity.ok(gameService.connectExistingGame(connectRequest.getPlayer(), connectRequest.getGameId()));
    }

    @PostMapping(value = "/connect/random")
    public ResponseEntity<Game> connectRandomGame(@RequestBody Player player)
            throws InvalidParamException, GameNotFoundException {
        return ResponseEntity.ok(gameService.connectRandomGame(player));
    }

    @PostMapping(value = "/gamePlay")
    public ResponseEntity<GamePlayResponse> gamePlay(@RequestBody GamePlay gamePlay) throws Exception {
        Game game = gameService.gamePlay(gamePlay);
        String message = "";
        if (game.getGameStatus().equals(GameStates.FINISHED.name()))
            message = "Game is finished. Player " + (game.getTurn().equals(Symbol.X.name()) ? game.getPlayer2() : game.getPlayer1()) +
                    " is winner";
        else if (game.getGameStatus().equals(GameStates.DRAW.name()))
            message = "Game ended in a draw";
        else
            message = "Game in progress. Player " + (game.getTurn().equals(Symbol.X.name()) ? game.getPlayer1() : game.getPlayer2())
                    +" to place symbol " + (game.getTurn().equals("X") ? Symbol.X : Symbol.O);
        GamePlayResponse response = new GamePlayResponse(game.getGameId(), game.getPlayer1(), game.getPlayer2(),
                game.getGameStatus(), util.getBoardById(game.getGameId()), message);
        return ResponseEntity.ok(response);
    }
}
