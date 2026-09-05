package populationBoard;
import java.util.HashMap;

import miniPeople.miniPerson;

public class populationBoard {

    private int sizeX;
    private int sizeY;

    HashMap <coordinatePair, miniPerson> populationMap;

    public populationBoard(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        populationMap = new HashMap<>();

    }

    // basic methods to get the size and population of the board
    // get how many people are on the board and how many empty spaces are left
    public int getPopulationSize() {
        return populationMap.size();
    }
    public int getEmptySpaces() {
        return sizeX * sizeY - getPopulationSize();
    }


    // helper method to validate coordinates
    public void validateCoordinate(coordinatePair pair) {
        if (pair.getX() < 0 || pair.getX() >= sizeX || pair.getY() < 0 || pair.getY() >= sizeY) {
            throw new IllegalArgumentException("Coordinates out of bounds");
        }
    }
    
    // helper method to check if coordinate occupied
    public boolean isOccupied(coordinatePair pair) {
		validateCoordinate(pair);
		return populationMap.containsKey(pair);
	}

    // dont use these. These are only encapsulations for the board controller
    public miniPerson getAtCoordinates(coordinatePair pair) {
        return populationMap.get(pair);
    }

    public void putAtCoordinates( miniPerson person, coordinatePair pair) {
        populationMap.put(pair, person);
    }

    public miniPerson removeAtCoordinates(coordinatePair pair) {
        return populationMap.remove(pair);
    }

}