package populationBoard;

import java.util.Random;
import miniPeople.miniMan;
import miniPeople.miniPerson;
import miniPeople.miniWoman;
import utilities.util;

public class populationBoardController {

    private final populationBoard board;

    public populationBoardController(populationBoard board) {
        this.board = board;
    }

    // Validation methods
    private void validateCoordinate(coordinatePair pair) {
        if (pair == null) {
            throw new IllegalArgumentException("Coordinate pair cannot be null");
        }
        if (pair.getX() < 0 || pair.getX() >= board.sizeX ||
                pair.getY() < 0 || pair.getY() >= board.sizeY) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
    }

    private void validatePersonExists(miniPerson person) {
        if (board.personExists(person)) {
            throw new IllegalArgumentException("Person already exists");
        }
    }

    private void validateOccupied(coordinatePair pair) {
        validateCoordinate(pair);
        if (!board.isOccupied(pair)) {
            throw new IllegalArgumentException("Coordinates are empty");
        }
    }

    private void validateEmpty(coordinatePair pair) {
        validateCoordinate(pair);
        if (board.isOccupied(pair)) {
            throw new IllegalArgumentException("Coordinates are occupied");
        }
    }

    private void validateSpaceAvailable() {
        if (board.getPopulationSize() >= board.getBoardSize()) {
            throw new IllegalArgumentException("There is no more space left");
        }
    }

    // Public methods
    public miniPerson getPerson(coordinatePair pair) {
        validateOccupied(pair);
        return board.getAtCoordinates(pair);
    }

    public void setPerson(miniPerson person, coordinatePair pair) {
        validateSpaceAvailable();
        validatePersonExists(person);
        validateEmpty(pair);
        board.putAtCoordinates(person, pair);
    }

    public miniPerson removePerson(coordinatePair pair) {
        validateOccupied(pair);
        return board.removeAtCoordinates(pair);
    }

    public void movePerson(coordinatePair oldPair, coordinatePair newPair) {
        validateOccupied(oldPair);
        validateEmpty(newPair);

        miniPerson person = board.removeAtCoordinates(oldPair);
        board.putAtCoordinates(person, newPair);
    }

    public void movePersonByOne(coordinatePair pair, Direction direction) {
        validateOccupied(pair);

        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        coordinatePair newPair = new coordinatePair(newX, newY);

        validateEmpty(newPair);
        movePerson(pair, newPair);
    }

    public void initializePopulation(int count) {
        if (count > board.getBoardSize()) {
            throw new IllegalArgumentException("Cannot generate more people than board holds");
        }

        for (int i = 0; i < count; i++) {
            setPerson(generateMiniPerson(), generateRandomCoordinates());
        }
    }

    public miniPerson generateMiniPerson() {
        miniPerson mp;

        if ((int)(Math.random() * 2) == 0) {
            mp = new miniMan(
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100)
            );
        } else {
            mp = new miniWoman(
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100)
            );
        }

        return mp;
    }

    public coordinatePair generateRandomCoordinates() {
        Random random = new Random();
        int randX = random.nextInt(board.sizeX);
        int randY = random.nextInt(board.sizeY);
        return new coordinatePair(randX, randY);
    }

}