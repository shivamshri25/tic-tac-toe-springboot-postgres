package com.creditshelf.assignment.util;

import com.creditshelf.assignment.constants.Symbol;
import org.springframework.stereotype.Component;

@Component
public class GamePlayUtil {

    public boolean isBoardComplete(Integer[][] board) {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (board[i][j] == 0)
                    return false;
            }
        }
        return true;
    }

    public boolean checkWinner(Integer[][] board, Symbol turn) {
        int[] boardArray = new int[9];
        int counterIndex = 0;

        for (int i = 0 ; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                boardArray[counterIndex++] = board[i][j];
            }
        }
        // winning combinations
        int[][] winCombos = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
        for (int i = 0 ; i < winCombos.length; i++) {
            int counter = 0;
            for (int j = 0; j < winCombos[i].length; j++) {
                if (boardArray[winCombos[i][j]] == turn.getValue()) {
                    counter++;
                    if (counter == 3) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
