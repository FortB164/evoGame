package populationBoard.boardLogic;

import java.util.ArrayList;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import entities.miniPeople.MiniPerson;
import populationBoard.boardLogic.boardValidation.BoardValidator;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.ListHelper;

public class BoardLocationService {

    Board board;
    BoardValidator validator;
    Random random = new Random();

    // this is a cache that gets updated every set/remove, avoiding re-computation every call
    ArrayList<CoordinatePair> emptySpaces;

    public BoardLocationService(Board board) {
        this.board = board;
        validator = new BoardValidator(board);
        emptySpaces = getEmptySpaces();
    }

    public MiniPerson getPerson(CoordinatePair pair) {
        return validator.coordinatesOccupied(pair) ?  board.getPopulationMap().get(pair) : null;
    }

    public int setPerson(CoordinatePair pair, MiniPerson person) {
        if(validator.coordinatesValid(pair)
                && validator.spaceAvailable()
                && !validator.personAlreadyExists(person)
                && !validator.coordinatesOccupied(pair))
        {
            emptySpaces = getEmptySpaces();
            board.getPopulationMap().put(pair, person);
            return 0;
        }
        return 1;
    }

    public MiniPerson removePerson(CoordinatePair pair) {
        if(validator.coordinatesValid(pair)
                && validator.coordinatesOccupied(pair))
        {
            emptySpaces.add(pair);
            return board.getPopulationMap().remove(pair);
        }

        return null;
    }

    public int movePerson(CoordinatePair oldPair, CoordinatePair newPair) {
        if(validator.coordinatesValid(oldPair)
                && validator.coordinatesValid(newPair)
                && validator.coordinatesOccupied(oldPair)
                && !validator.coordinatesOccupied(newPair)
        )
        {
            setPerson(newPair, removePerson(oldPair));
            return 0;
        }
        return 1;
    }

    public int movePersonByOne(CoordinatePair pair, @NotNull Direction direction) {
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);
        if(movePerson(pair, newPair) == 0) return 0;
        return 1;
    }


    public int movePersonBySpeed(CoordinatePair pair, @NotNull Direction direction) {
        int speed = (int) ListHelper.arrayListAverage(getPerson(pair).speed);
        int newX = pair.getX() + direction.getX(speed);
        int newY = pair.getY() + direction.getY(speed);
        CoordinatePair newPair = new CoordinatePair(newX, newY);
        if(movePerson(pair, newPair) == 0) return 0;
        return 1;
    }

    // gets unused spaces on the board
    // this only exists to recalculate cache in case it gets corrupted
    public ArrayList<CoordinatePair> getEmptySpaces(){
        ArrayList<CoordinatePair> emptySpaces = new ArrayList<>();
        for(int i = 0 ; i < board.getSizeX() ; i++){
            for(int j = 0 ; j < board.getSizeY(); j++){
                if (!validator.coordinatesOccupied(new CoordinatePair(i,j)))
                {
                    emptySpaces.add(new CoordinatePair(i, j));
                }
            }
        }
        return emptySpaces;
    }

    // generates random coordinates from available spaces
    public CoordinatePair generateRandomCoordinates() {
        return validator.spaceAvailable() ?
                emptySpaces.get(random.nextInt(emptySpaces.size())) :
                null;
    }

    public boolean hasAvailableSpace() {
        return (!emptySpaces.isEmpty() && validator.spaceAvailable());
    }
}