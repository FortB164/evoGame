package populationBoard;

import java.util.HashMap;
import miniPeople.miniPerson;

public class populationBoard {

    final int sizeX;
    final int sizeY;
    final HashMap<coordinatePair, miniPerson> populationMap;

    public populationBoard(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.populationMap = new HashMap<>();
    }

    public int getPopulationSize() {
        return populationMap.size();
    }

    public int getBoardSize() {
        return sizeX * sizeY;
    }

    public int getEmptySpaces(){
        return getBoardSize() - getPopulationSize();
    }


    // Encapsulation methods - minimal logic, only data access
    public miniPerson getAtCoordinates(coordinatePair pair) {
        return populationMap.get(pair);
    }

    public void putAtCoordinates(miniPerson person, coordinatePair pair) {
        populationMap.put(pair, person);
    }

    public miniPerson removeAtCoordinates(coordinatePair pair) {
        return populationMap.remove(pair);
    }

    public boolean isOccupied(coordinatePair pair) {
        return populationMap.containsKey(pair);
    }

    public boolean personExists(miniPerson person) {
        return populationMap.containsValue(person);
    }
}