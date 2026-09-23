package populationBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import miniPeople.MiniPerson;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.Logger;


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

    public void initializePopulation(int count) {
        if (count > board.getFullSize()) {
            throw new IllegalArgumentException("Cannot add more people than board size");
        }

        for(int i = 0 ; i < count; i++) {

            MiniPerson mp = BoardLife.generateMiniPerson();
            CoordinatePair pair = locationService.generateRandomCoordinates();

            names.nameAndSet(mp);
            locationService.setPerson(pair, mp);

            logger.addAction(names.getNameOf(mp) + " added to board");
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

            try {
                locationService.movePersonByOne(pair,direction); // try to move
                moveFlag = true; // if moved successfully, count it as a move
            } catch (IllegalArgumentException e) {
                // if move failed due to filled space or out of bounds direction, skip turn
                moveFlag = false; // count it as not moved

                String msg = e.getMessage();
                // if the failure is specifically that target coords are occupied,
                // attempt breeding immediately, as it counts as collision
                if(msg.equals("Cannot move person. Target coordinates are occupied")){
                    collision = true;
                    logger.addAction(names.getNameOf(locationService.getPerson(pair)) + " has collided with " + names.getNameOf(locationService.getPerson(newPair)) + ".");
                    life.handleBreeding(pair,direction);
                }
            }

            // if the move was successful, update coordinates of the person and add to moved
            // if coordinates are not updated, then someone else could move into the old empty coords,
            // and the person who moved into those coords that are counted as moved can not move when their turn comes
            // while the person who moved away gets to unfairly move many times
            if(moveFlag){
                hasMoved.add(person);
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