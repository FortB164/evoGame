package game;

import populationBoard.populationBoard;
import populationBoard.populationBoardController;

public class Game {
    populationBoard board = new populationBoard(100, 100);
    populationBoardController controller = new populationBoardController(board);

}
