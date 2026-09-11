package game;

import miniPeople.MiniPerson;
import miniPeople.dataObjects.Gender;
import populationBoard.BoardController;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;

import java.util.HashMap;

import static utilities.listHelper.print;
import static utilities.listHelper.println;


public class Game {
    private static Board board;
    private static BoardController controller;
    boolean runLoop;
    int sizeX = 10;
    int sizeY = 10;

    public Game(){
        board = new Board(sizeX, sizeY);
        controller = new BoardController(board);
        runLoop = true;
    }

    public void start() {
        controller.initializePopulation(5);
        int counter = 0;

        while(runLoop){

            try {
                controller.moveAllRandomly();
                renderInConsole();
            } catch (RuntimeException e) {
                // don't let an unexpected error during an iteration kill the whole simulation
                String msg =  e.getMessage();
                if(msg.equals("java.lang.IllegalArgumentException: bound must be positive")){
                    break;
                }
            }

            counter++;
        }
    }


    public void renderInConsole() {
        char[][] arrayToShow = new char[sizeX][sizeY];

        // Fill with empty space (otherwise you get '\0' chars)
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                arrayToShow[i][j] = ' ';
            }
        }

        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();

        for (CoordinatePair cp : hs.keySet()) {
            MiniPerson p = hs.get(cp);
            if (p.getGender() == Gender.MALE) {
                arrayToShow[cp.getX()][cp.getY()] = 'M';
            } else if (p.getGender() == Gender.FEMALE) {
                arrayToShow[cp.getX()][cp.getY()] = 'F';
            }
        }

        // Top border: ┌───┬───┬───┐
        print("┌");
        for (int j = 0; j < sizeY; j++) {
            print("───");
            if (j < sizeY - 1) print("┬");
        }
        println("┐");

        // Rows
        for (int i = 0; i < sizeX; i++) {
            // Cell row: │ A │ B │ C │
            print("│");
            for (int j = 0; j < sizeY; j++) {
                print(" " + arrayToShow[i][j] + " │");
            }
            print();

            // Row separator (or bottom border on last row)
            if (i < sizeX - 1) {
                print("├");
                for (int j = 0; j < sizeY; j++) {
                    print("───");
                    if (j < sizeY - 1) print("┼");
                }
                println("┤");
            } else {
                print("└");
                for (int j = 0; j < sizeY; j++) {
                    print("───");
                    if (j < sizeY - 1) print("┴");
                }
                println("┘");
            }
        }
    }
}