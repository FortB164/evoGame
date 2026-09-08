package populationBoard;

import java.util.Random;
import miniPeople.MiniMan;
import miniPeople.MiniPerson;
import miniPeople.MiniWoman;
import org.jetbrains.annotations.NotNull;
import utilities.util;

public class PopulationBoardController {

    private final PopulationBoard board;

    public PopulationBoardController(PopulationBoard board) {
        this.board = board;
    }

    // Validation methods
    private void validateCoordinate(CoordinatePair pair) {
        if (pair == null) {
            throw new IllegalArgumentException("Coordinate pair cannot be null");
        }
        if (pair.getX() < 0 || pair.getX() >= board.sizeX ||
                pair.getY() < 0 || pair.getY() >= board.sizeY) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
    }

    private void validatePersonExists(MiniPerson person) {
        if (board.personExists(person)) {
            throw new IllegalArgumentException("Person already exists");
        }
    }

    private void validateOccupied(CoordinatePair pair) {
        validateCoordinate(pair);
        if (!board.isOccupied(pair)) {
            throw new IllegalArgumentException("Coordinates are empty");
        }
    }

    private void validateEmpty(CoordinatePair pair) {
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
    public MiniPerson getPerson(CoordinatePair pair) {
        validateOccupied(pair);
        return board.getAtCoordinates(pair);
    }

    public void setPerson(MiniPerson person, CoordinatePair pair) {
        validateSpaceAvailable();
        validatePersonExists(person);
        validateEmpty(pair);
        board.putAtCoordinates(person, pair);
    }

    public MiniPerson removePerson(CoordinatePair pair) {
        validateOccupied(pair);
        return board.removeAtCoordinates(pair);
    }

    public void movePerson(CoordinatePair oldPair, CoordinatePair newPair) {
        validateOccupied(oldPair);
        validateEmpty(newPair);

        MiniPerson person = board.removeAtCoordinates(oldPair);
        board.putAtCoordinates(person, newPair);
    }

    public boolean movePersonByOne(CoordinatePair pair, @NotNull Direction direction) {
        // validateOccupied(pair);

        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);

        try{
            validateEmpty(newPair);
        } catch (IllegalArgumentException e) {
            return false; // only returns false if new location is occupied, letting us deal with collisions
        }

        movePerson(pair, newPair);
        return true; // true if success
    }

    public void initializePopulation(int count) {
        if (count > board.getBoardSize()) {
            throw new IllegalArgumentException("Cannot generate more people than board holds");
        }

        for (int i = 0; i < count; i++) {
            CoordinatePair pair;
            do {
                pair = generateRandomCoordinates();
            } while (board.isOccupied(pair));
            setPerson(generateMiniPerson(), pair);
        }
    }

    public MiniPerson generateMiniPerson() {
        MiniPerson mp;

        if ((int)(Math.random() * 2) == 0) {
            mp = new MiniMan(
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100)
            );
        } else {
            mp = new MiniWoman(
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100),
                    util.generateDoubleArrayList(5, 10, 100)
            );
        }

        return mp;
    }

    public CoordinatePair generateRandomCoordinates() {
        Random random = new Random();
        int randX = random.nextInt(board.sizeX);
        int randY = random.nextInt(board.sizeY);
        return new CoordinatePair(randX, randY);
    }

}