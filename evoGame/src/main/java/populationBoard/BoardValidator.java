package populationBoard;

import miniPeople.MiniPerson;
import org.jetbrains.annotations.NotNull;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;

public class BoardValidator {

    Board board;

    public BoardValidator(Board board) {
        this.board = board;
    }

    public boolean coordinatesValid(@NotNull CoordinatePair pair) {
        return pair.getX() >= 0 && pair.getX() < board.getSizeX() && pair.getY() >= 0 && pair.getY() < board.getSizeY();
    }

    public boolean personExists(MiniPerson person) {
        return board.getPopulationMap().containsValue(person);
    }

    public boolean coordinatesOccupied(CoordinatePair pair) {
        return board.getPopulationMap().containsKey(pair);
    }

    public boolean spaceAvailable() {
        return board.getFullSize() > board.getPopulationMap().size();
    }
}