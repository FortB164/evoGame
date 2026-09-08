package populationBoard;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import miniPeople.MiniPerson;

public class PopulationBoard {

    final int sizeX;
    final int sizeY;
    final HashMap<CoordinatePair, MiniPerson> populationMap;

    public PopulationBoard(int sizeX, int sizeY) {
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

    public int getEmptySpacesCount(){
        return getBoardSize() - getPopulationSize();
    }

    public HashMap<CoordinatePair, MiniPerson> getPopulationMap() { return populationMap; }

    public ArrayList<CoordinatePair> getEmptySpaces(){
        ArrayList<CoordinatePair> emptySpaces = new ArrayList<>();

        for(int i = 0 ; i < sizeX ; i++){
            for(int j = 0 ; j < sizeY; j++){
                if(!populationMap.containsKey(new CoordinatePair(i, j))){
                    emptySpaces.add(new CoordinatePair(i, j));
                }
            }
        }

        return emptySpaces;
    }

    // Encapsulation methods - minimal logic, only data access
    public MiniPerson getAtCoordinates(CoordinatePair pair) {
        return populationMap.get(pair);
    }

    public void putAtCoordinates(MiniPerson person, CoordinatePair pair) {
        populationMap.put(pair, person);
    }

    public MiniPerson removeAtCoordinates(CoordinatePair pair) {
        return populationMap.remove(pair);
    }

    public boolean isOccupied(CoordinatePair pair) {
        return populationMap.containsKey(pair);
    }

    public boolean personExists(MiniPerson person) {
        return populationMap.containsValue(person);
    }
}