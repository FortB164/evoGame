package populationBoard.boardLogic;

import java.util.ArrayList;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import entities.miniPeople.MiniPerson;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.ListHelper;

public class BoardLocationService {

    Board board;
    BoardValidator validator;

    // this is a cache that gets updated every set/remove, avoiding re-computation every call
    ArrayList<CoordinatePair> emptySpaces;

    public BoardLocationService(Board board) {
        this.board = board;
        validator = new BoardValidator(board);
        emptySpaces = getEmptySpaces();
    }

    public MiniPerson getPerson(CoordinatePair pair) {
        if(!validator.coordinatesValid(pair)) {
            throw new IllegalArgumentException("Cannot get person. Invalid coordinates");
        }
        return board.getPopulationMap().get(pair);
    }

    public void setPerson(CoordinatePair pair, MiniPerson person) {
        if(!validator.coordinatesValid(pair)) {
            throw new IllegalArgumentException("Cannot set person. Invalid coordinates");
        }
        if(!validator.spaceAvailable()){
            throw new IllegalArgumentException("Cannot set person. No available space to set person");
        }
        if(validator.personExists(person)) {
            throw new IllegalArgumentException("Cannot set person. This person already exists");
        }
        if(validator.coordinatesOccupied(pair)){
            throw new IllegalArgumentException("Cannot set. These coordinates are already occupied");
        }

        emptySpaces.remove(pair);
        board.getPopulationMap().put(pair, person);
    }

    public MiniPerson removePerson(CoordinatePair pair) {
        if(!validator.coordinatesValid(pair)) {
            throw new IllegalArgumentException("Cannot remove. Invalid coordinates");
        }
        if(!validator.coordinatesOccupied(pair)) {
            throw new IllegalArgumentException("Cannot remove. Coordinates are empty");
        }

        emptySpaces.add(pair);
        return board.getPopulationMap().remove(pair);
    }

    public void movePerson(CoordinatePair oldPair, CoordinatePair newPair) {
        if(!validator.coordinatesValid(oldPair)) {
            throw new IllegalArgumentException("Cannot move person. Invalid old coordinates");
        }
        if(!validator.coordinatesValid(newPair)) {
            throw new IllegalArgumentException("Cannot move person. Invalid new coordinates");
        }
        if(!validator.coordinatesOccupied(oldPair)) {
            throw new IllegalArgumentException("Cannot move person. Coordinates are empty");
        }
        if(validator.coordinatesOccupied(newPair)) {
            throw new IllegalArgumentException("Cannot move person. Target coordinates are occupied");
        }
        setPerson(newPair, removePerson(oldPair));
    }

    public void movePersonByOne(CoordinatePair pair, @NotNull Direction direction) {
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);
        movePerson(pair, newPair);
    }

    public void movePersonBySpeed(CoordinatePair pair, @NotNull Direction direction) {
        int speed = (int) ListHelper.arrayListAverage(getPerson(pair).speed);
        int newX = pair.getX() + direction.getX(speed);
        int newY = pair.getY() + direction.getY(speed);
        CoordinatePair newPair = new CoordinatePair(newX, newY);
        movePerson(pair, newPair);
    }

    // gets unused spaces on the board
    public ArrayList<CoordinatePair> getEmptySpaces(){
        ArrayList<CoordinatePair> emptySpaces = new ArrayList<>();
        for(int i = 0 ; i < board.getSizeX() ; i++){
            for(int j = 0 ; j < board.getSizeY(); j++){
                if(!validator.coordinatesOccupied(new CoordinatePair(i, j))){
                    emptySpaces.add(new CoordinatePair(i, j));
                }
            }
        }
        return emptySpaces;
    }

    // generates random coordinates from available spaces
    public CoordinatePair generateRandomCoordinates() {
        if(!hasAvailableSpace()) {
            throw new IllegalStateException("No available space to generate random coordinates");
        }
        Random random = new Random();
        return emptySpaces.get(random.nextInt(emptySpaces.size()));
    }

    public boolean hasAvailableSpace() {
        return (!emptySpaces.isEmpty() && validator.spaceAvailable());
    }

}