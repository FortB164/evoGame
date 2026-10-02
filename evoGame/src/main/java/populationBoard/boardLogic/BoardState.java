package populationBoard.boardLogic;

import populationBoard.dataObjects.Board;

import java.util.ArrayDeque;

public class BoardState {

    // this class takes in the history of a board sim
    // each state is a snapshot taken each iteration
    private final Board board;
    private final ArrayDeque<Board> states = new ArrayDeque<>();

    public BoardState(Board board) {
        this.board = board;
    }

    public int getStatesSize() {
        return states.size();
    }

    public void addState() {
        states.addFirst(board.copy());
    }

    // gives states relative to current state. 0 gives current, 1 gives previous, 2 gives two states previous
    public Board getState(int n) {
        return states.stream()
                .skip(n)
                .findFirst()
                .orElse(null);
    }
}