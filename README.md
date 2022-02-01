# tic-tac-toe-springboot-postgres
Tic Tac Toe Game implementation using Spring Boot with PostgreSQL database

# about-the-app
The application is a simple implementation of the tic-tac-toe game between 2 players on a 3 X 3 board. The first player can create a game and the second player can connect to the game or to any game from existing games in the database. The gameplay is pretty simple with both the players placing "X" and "O" symbols in the different squares of the grid and the winner is the one who successfully makes a linear/diagonal progression with the same symbol. The game ends up as a draw in case no winner is decided after completion of all the suares of the grid.

# technologies-used
- Java v8
- Maven
- Spring Boot v2+ 
- PostgreSQL v14

# technical-summary-data-model
As of today, we have a server which enables a user to play the tic-tac-toe game with 2 players on a 3 X 3 board. By hitting some API's in order, a successful gameplay is carried out. There is exception handling in place for erraneous scenarios. Visit the 'happy-flow-demo' section to witness the API's in action. One player can either create a game or connect to any random game present in the database. After 2 players are in a game which is in progress, the players can place symbols on the grid and try to beat the other by managing to create a straight line which falls under the winning combinations. The game_id parameter is the one being used to manage the state and progress of a game.

The current implementation has limitless possibilities to extend it to multiple players or board lengths and also have tornaments and leaderboards in the game. But, for the sake of the problem statement at hand, the implementation focusses on the conduct of gameplays between 2 players on 3 x 3 grid. One very interesting point to note here is how the next turn is determined. The transient parameter turn is the one which is determining the same in conjunction with game_status. In a game which is not ended either as a draw or with the win of one particular player, the turn parameter contains the value of the next symbol to be placed on the board. The user would see a message in the response stating the player and the symbol to be placed in the upcoming turn. The gamePlay API will take the values corresponding to the next turn and will render error if the parameters are not proper or the move made is invalid.

For the purpose of understanding and ease of implementation, the symbol 'X' is equivalent to **1** on the grid and the symbol 'O' is **2**. The number 0 means that cell is empty and no symbol has been placed in that particular place. PostgeSQL database stores the current state of the board after each move is made. The user can see the board state in the reponse after every move and then decide the next move accordingly.

# happy-flow-demo

>STEP-1: Create a new game
```
Request URL - http://localhost:8080/game/create
Request Type - POST
Content:
{
  "login" : "developer" 
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": null,
    "gameStatus": "NEW",
    "board": [
        [0, 0, 0],
        [0, 0, 0],
        [0, 0, 0]
    ],
    "turn": null
}
```

>STEP-2: Let player connect to this game
```
Request URL - http://localhost:8080/game/connect
Request Type - POST
Content:
{
  "player" : 
  {
    "login" : "tester"
  },
  "gameId" : "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b"
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": "tester",
    "gameStatus": "PLAYING",
    "board": [
        [0, 0, 0],
        [0, 0, 0],
        [0, 0, 0]
    ],
    "turn": null
}
```

>STEP-3: Let the game begin. Assume “developer” to place ‘X’ and “tester” to place ‘O’ on the board
```
Request URL - http://localhost:8080/game/gamePlay
Request Type - POST
Content:
{
  "turn" : "X",
  "coordinateX" : 0,
  "coordinateY" : 0,
  "gameId" : "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b"
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": "tester",
    "status": "PLAYING",
    "board": [
        [1, 0, 0],
        [0, 0, 0],
        [0, 0, 0]
    ],
    "next": "Game in progress. Player tester to place symbol O"
}
```

>STEP-4: Next turn – “tester” will place ‘O’ on the board
```
Request URL - http://localhost:8080/game/gamePlay
Request Type - POST
Content:
{
  "turn" : "O",
  "coordinateX" : 0,
  "coordinateY" : 1,
  "gameId" : "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b"
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": "tester",
    "status": "PLAYING",
    "board": [
        [1, 2, 0],
        [0, 0, 0],
        [0, 0, 0]
    ],
    "next": "Game in progress. Player developer to place symbol X"
}
```

>STEP-5: Next turn – “developer” to place ‘X’
```
Request URL - http://localhost:8080/game/gamePlay
Request Type - POST
Content:
{
  "turn" : "X",
  "coordinateX" : 1,
  "coordinateY" : 1,
  "gameId" : "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b"
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": "tester",
    "status": "PLAYING",
    "board": [
        [1, 2, 0],
        [0, 1, 0],
        [0, 0, 0]
    ],
    "next": "Game in progress. Player tester to place symbol O"
}
```

>STEP-6: Next turn – “tester” to place ‘O’
```
Request URL - http://localhost:8080/game/gamePlay
Request Type - POST
Content:
{
  "turn" : "O",
  "coordinateX" : 1,
  "coordinateY" : 0,
  "gameId" : "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b"
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": "tester",
    "status": "PLAYING",
    "board": [
        [1, 2, 0],
        [2, 1, 0],
        [0, 0, 0]
    ],
    "next": "Game in progress. Player developer to place symbol X"
}
```

>STEP-7: Next turn – “developer” to place ‘X’
```
Request URL - http://localhost:8080/game/gamePlay
Request Type - POST
Content:
{
  "turn" : "X",
  "coordinateX" : 2,
  "coordinateY" : 2,
  "gameId" : "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b"
}
Response:
{
    "gameId": "2f5cdcdb-b4e5-4596-bf39-a7d5c236df8b",
    "player1": "developer",
    "player2": "tester",
    "status": "FINISHED",
    "board": [
        [1, 2, 0],
        [2, 1, 0],
        [0, 0, 1]
    ],
    "next": "Game is finished. Player developer is winner"
}
```
