package populationBoard.dataObjects;

import miniPeople.MiniPerson;
import java.util.HashMap;

public class Board {

    private final int sizeX;
    private final int sizeY;
    private final HashMap<CoordinatePair, MiniPerson> populationMap;

    public Board(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.populationMap = new HashMap<>();
    }

    public int getSizeX() {
        return sizeX;
    }
    public int getSizeY() {
        return sizeY;
    }
    public int getFullSize() {
        return sizeX * sizeY;
    }
    public HashMap<CoordinatePair, MiniPerson> getPopulationMap() {return  populationMap;}
}