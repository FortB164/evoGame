package game;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import miniPeople.Gender;
import miniPeople.MiniPerson;
import org.jetbrains.annotations.NotNull;
import populationBoard.CoordinatePair;
import populationBoard.Direction;
import populationBoard.PopulationBoard;
import populationBoard.PopulationBoardController;

import static utilities.util.print;
import static utilities.util.println;


public class Game {
    private static PopulationBoard board;
    private static  PopulationBoardController controller;
    boolean runLoop = true;
    Random random;
    int sizeX = 10;
    int sizeY = 10;

    public Game(){
        board = new PopulationBoard(sizeX, sizeY);
        controller = new PopulationBoardController(board);
        random =  new Random();
    }

    public void start() {
        controller.initializePopulation(25);

        int counter = 0;
        int iterationDelay = 10;
        int iterations = 50;
        int maxIterations = iterationDelay * iterations;

        while(runLoop){

            if(counter == maxIterations){
                break;
            }

            try {
                if(counter % iterationDelay==0){
                    moveAllRandomly();
                    renderInConsole();
                }


            } catch (RuntimeException e) {
                // don't let an unexpected error during an iteration kill the whole simulation
                System.err.println("Error during iteration " + counter + ": " + e);
            }

            counter++;
        }
    }

    public void moveAllRandomly(){

        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();
        Set<CoordinatePair> keysCopy = new HashSet<>(hs.keySet());

        for(CoordinatePair pair : keysCopy){  // Iterate over the copy
            int k = random.nextInt(0,4);
            Direction direction = switch(k) {
                case 0 -> Direction.UP;
                case 1 -> Direction.RIGHT;
                case 2 -> Direction.DOWN;
                default -> Direction.LEFT;
            };


            if(!controller.movePersonByOne(pair, direction)){
                handleBreeding(pair, direction);
            }


        }
    }

    public void handleBreeding(@NotNull CoordinatePair pair, @NotNull Direction direction){
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);
        MiniPerson child = controller.getPerson(pair).breed(controller.getPerson(newPair));

        if(child == null){
            controller.getPerson(pair).fight(controller.getPerson(newPair));
        }

        else{
            int possibleCoordinates = board.getEmptySpaces().size();
            int randomIndex = random.nextInt(possibleCoordinates);
            controller.setPerson(child, board.getEmptySpaces().get(randomIndex));
        }

    }

    public void renderInConsole(){
        char[][] arrayToShow = new char[sizeX][sizeY];

        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();

        for(CoordinatePair cp : hs.keySet()) {
            if(hs.get(cp).getGender() == Gender.MALE){
                arrayToShow[cp.getX()][cp.getY()] = 'M';
            }
            else if(hs.get(cp).getGender() == Gender.FEMALE){
                arrayToShow[cp.getX()][cp.getY()] = 'F';
            }
        }

        for (char[] row : arrayToShow) {
            for (char c : row) {
                println(c + " ");
            }
            print();
        }
        print("----------");

    }
}