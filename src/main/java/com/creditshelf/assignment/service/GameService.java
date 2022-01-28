package com.creditshelf.assignment.service;

import com.creditshelf.assignment.constants.ErrorMessages;
import com.creditshelf.assignment.constants.GameStates;
import com.creditshelf.assignment.constants.Symbol;
import com.creditshelf.assignment.dto.GamePlay;
import com.creditshelf.assignment.exception.GameNotFoundException;
import com.creditshelf.assignment.exception.InvalidGameException;
import com.creditshelf.assignment.exception.InvalidMoveException;
import com.creditshelf.assignment.exception.InvalidParamException;
import com.creditshelf.assignment.model.Game;
import com.creditshelf.assignment.model.Player;
import com.creditshelf.assignment.repository.GameRepository;
import com.creditshelf.assignment.util.GamePlayUtil;
import com.creditshelf.assignment.util.JDBCUtil;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class GameService {

    @Autowired
    private GameRepository gameRepository;

    private final GamePlayUtil util;

    private final JDBCUtil jdbcUtil;

    public Game createGame(Player player) {
        Game game = new Game();
        game.setBoard(new int[3][3]);
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(player.getLogin());
        game.setGameStatus(GameStates.NEW.name());
        //game.setTurn(Symbol.X.name());
        //store game in database
        gameRepository.save(game);
        return game;
    }

    public List<Game> getGames() {
        return gameRepository.findAll();
    }

    public Game connectExistingGame(Player player2, String gameId) throws InvalidParamException, InvalidGameException {
        // check if game exists
        if (gameRepository.findById(gameId) == null || !gameRepository.findById(gameId).isPresent()) {
            throw new InvalidParamException(ErrorMessages.GAME_DOES_NOT_EXIST);
        }

        // check if game is valid
        Game game = gameRepository.findById(gameId).get();
        if (game.getPlayer2() != null) {
            throw new InvalidGameException(ErrorMessages.GAME_INVALID);
        }

        if (game.getPlayer1().equals(player2.getLogin()))
            throw new InvalidParamException(ErrorMessages.GAME_INVALID_PLAYER);

        game.setPlayer2(player2.getLogin());
        game.setGameStatus(GameStates.PLAYING.name());
        gameRepository.save(game);
        return game;
    }

    public Game connectRandomGame(Player player2) throws GameNotFoundException, InvalidParamException {
        Game game = getGames().stream().filter(g -> g.getPlayer2().isEmpty() && g.getGameStatus().equals("NEW")).findAny()
                .orElseThrow(() -> new GameNotFoundException(ErrorMessages.GAME_NOT_FOUND));

        if (game.getPlayer1().equals(player2.getLogin()))
            throw new InvalidParamException(ErrorMessages.GAME_INVALID_PLAYER);

        game.setPlayer2(player2.getLogin());
        game.setGameStatus(GameStates.PLAYING.name());
        gameRepository.save(game);
        return game;
    }

    public Game gamePlay(GamePlay gamePlay) throws Exception {
        Game game = gameRepository.findById(gamePlay.getGameId()).
                orElseThrow(() -> new GameNotFoundException(ErrorMessages.GAME_NOT_FOUND));

        //validations
        if (game.getPlayer2().isEmpty() || game.getPlayer2() == null ||
                game.getGameStatus().equals(GameStates.FINISHED.name()) || game.getGameStatus().equals(GameStates.DRAW.name()))
            throw new InvalidGameException(ErrorMessages.GAME_INVALID_OR_FINISHED);

        Integer[][] board = jdbcUtil.getBoardById(gamePlay.getGameId());
        if (board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()].equals(Symbol.X.getValue()) ||
                board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()].equals(Symbol.O.getValue())) {
            throw new InvalidMoveException(ErrorMessages.GAME_MOVE_INVALID);
        }
        // making a move
        board[gamePlay.getCoordinateX()][gamePlay.getCoordinateY()] = gamePlay.getTurn().getValue();

        if (util.checkWinner(board, gamePlay.getTurn()))
            game.setGameStatus(GameStates.FINISHED.name());
        else if (util.isBoardComplete(board))
            game.setGameStatus(GameStates.DRAW.name());

        game.setTurn(gamePlay.getTurn().equals(Symbol.X) ? Symbol.O.name() : Symbol.X.name());

        gameRepository.save(game);
        jdbcUtil.setCurrentBoardState(board, gamePlay.getGameId());
        return game;
    }

}
