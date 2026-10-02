package populationBoard.dataObjects;

import entities.miniPeople.MiniPerson;
import interfaces.Copyable;

import java.util.HashMap;
import java.util.Map;

public class Board implements Copyable<Board> {
    private final int sizeX;
    private final int sizeY;
    private final HashMap<CoordinatePair, MiniPerson> populationMap = new HashMap<>();

    public Board(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    private Board(Board board){
        this.sizeX = board.sizeX;
        this.sizeY = board.sizeY;
        board.populationMap.forEach((key, value) ->
                this.populationMap.put(key, value.copy())
        );
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

    @Override
    public Board copy() {
        return new Board(this);
    }
}