package populationBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import entities.miniPeople.MiniPerson;
import populationBoard.boardBiology.BoardAncestry;
import populationBoard.boardBiology.BoardLife;
import populationBoard.boardBiology.BoardNames;
import populationBoard.boardLogic.BoardLocationService;
import populationBoard.boardLogic.BoardState;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.logger.Logger;


public class BoardController {

    Board board;
    BoardLocationService locationService;
    BoardAncestry ancestry;
    public BoardNames names;
    Logger logger;
    BoardLife life;
    BoardState states;


    public BoardController(Board board) {

        this.logger = Logger.getLogger();
        this.board = board;
        this.names = new BoardNames(board);
        this.ancestry = new BoardAncestry(names);
        this.locationService = new BoardLocationService(board);
        this.life = new BoardLife(locationService, names, ancestry);
        this.states = new BoardState(board);

    }

    public int getStatesSize(){
        return states.getStatesSize();
    }

    public void initializePopulation(int count) {
        if (!locationService.hasAvailableSpace()) {
            return;
        }

        for(int i = 0 ; i < count; i++) {

            MiniPerson mp = BoardLife.generateMiniPerson();
            CoordinatePair pair = locationService.generateRandomCoordinates();

            names.nameAndSet(mp);
            if (locationService.setPerson(pair, mp) != 0) {
                logger.logError(new IllegalStateException(
                        "Failed to place " + names.getNameOf(mp) + " at " + pair));
                return;
            }

            logger.logAction(names.getNameOf(mp) + " added to board.");
        }
        states.addState();
    }

    public void moveAllRandomly(){

        Random random = new  Random();
        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();
        Set<CoordinatePair> keysCopy = new HashSet<>(hs.keySet()); // iterate over key copy not original other concurrent access problem
        HashSet<MiniPerson> hasMoved = new HashSet<>(); // record of who moved


        for(CoordinatePair pair : keysCopy){
            MiniPerson person = hs.get(pair);
            if(person == null || hasMoved.contains(person)) continue; // skip eliminated or already moved people

            boolean moveFlag;
            boolean collision = false;
            Direction direction = Direction.values()[random.nextInt(4)];  // random direction to move in
            CoordinatePair newPair = new CoordinatePair(pair.getX()+direction.getX(),pair.getY()+direction.getY());


            if(locationService.movePersonByOne(pair,direction) == 0){
                moveFlag = true;
            }

            else{
                moveFlag = false;

                if(locationService.getPerson(newPair) != null){
                    collision = true;
                    logger.logAction(names.getNameOf(locationService.getPerson(pair)) + " has collided with " + names.getNameOf(locationService.getPerson(newPair)) + ".");
                    life.handleBreeding(pair,direction);
                }
            }

            if(moveFlag){
                hasMoved.add(person);
                logger.logAction(names.getNameOf(person) + " has moved " + direction.toString() + ", into " + newPair.toString());
            }

            // A collision consumes both participants' turns, even if one was eliminated.
            else if (collision) {
                hasMoved.add(person);
                MiniPerson target = hs.get(newPair);
                if (target != null) hasMoved.add(target);
            } else {
                hasMoved.add(person);
            }

            life.handleAging(moveFlag ? newPair : pair);
            if (collision) life.handleAging(newPair);

        }
        states.addState();
    }
}