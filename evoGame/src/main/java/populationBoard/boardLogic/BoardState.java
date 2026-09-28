package populationBoard.boardLogic;

import entities.miniPeople.MiniPerson;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;

import java.util.*;

public class BoardState {

    // this class takes in the history of a board sim
    // each state is a snapshot taken each iteration
    Board board;
    LinkedList<HashMap<CoordinatePair, MiniPerson>> states = new LinkedList<>();

    public BoardState(Board board) {
        this.board = board;
    }

    public void addState(){
        states.addFirst(getState());
    }

    public HashMap<CoordinatePair, MiniPerson> getState(){
        HashMap<CoordinatePair, MiniPerson> state = new HashMap<>();
        for(Map.Entry<CoordinatePair, MiniPerson> entry : board.getPopulationMap().entrySet()){
            CoordinatePair pair = entry.getKey();
            MiniPerson mp =  entry.getValue();
            MiniPerson mpCopy = mp.copy();
            state.put(pair, mpCopy);
        }
        return state;
    }
}