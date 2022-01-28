package com.creditshelf.assignment.util;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
@AllArgsConstructor
public class JDBCUtil {

    @Autowired
    private Environment environment;

    public void setCurrentBoardState(Integer[][] board, String gameId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("update games set board = ? where game_id = ?");
        Array array = connection.createArrayOf("integer", board);
        statement.setArray(1, array);
        statement.setString(2, gameId);
        statement.execute();
        statement.close();
        connection.close();
    }

    public Integer[][] getBoardById(String gameId) throws Exception {
        Connection connection = getConnection();
        PreparedStatement statement = connection.prepareStatement("select board as array from games where game_id = ?");
        statement.setString(1, gameId);
        ResultSet resultSet = statement.executeQuery();
        Integer[][] boardArr = new Integer[3][3];
        while (resultSet.next()) {
            Array outputArray = resultSet.getArray(1);
            boardArr = (Integer[][]) outputArray.getArray();
        }
        connection.close();
        return boardArr;
    }

    private Connection getConnection() throws Exception {
        Class.forName(environment.getProperty("spring.datasource.driver"));
        return DriverManager.getConnection(
                environment.getProperty("spring.datasource.url"), environment.getProperty("spring.datasource.username"),
                environment.getProperty("spring.datasource.password"));

    }
}
