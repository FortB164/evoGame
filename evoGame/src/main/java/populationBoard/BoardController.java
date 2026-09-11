package populationBoard;

import miniPeople.MiniPerson;
import org.jetbrains.annotations.NotNull;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.personHelper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BoardController {

    Board board;
    BoardLocationService locationService;

    public BoardController(Board board) {
        this.board = board;
        locationService = new BoardLocationService(board);
    }


    public void initializePopulation(int count) {
        if (count > board.getFullSize()) {
            throw new IllegalArgumentException("Cannot add more people than board size");
        }

        for(int i = 0 ; i < count; i++) {
            locationService.setPerson(personHelper.generateMiniPerson(), locationService.generateRandomCoordinates());
        }
    }

    public void moveAllRandomly(){

        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();
        Set<CoordinatePair> keysCopy = new HashSet<>(hs.keySet());
        Random random = new  Random();

        HashSet<MiniPerson> hasMoved = new HashSet<>();
        for(CoordinatePair pair : keysCopy){  // Iterate over the copy
            if(hasMoved.contains(hs.get(pair))) continue;

            boolean moveFlag;
            Direction direction = Direction.values()[random.nextInt(4)];

            try {
                locationService.movePersonByOne(pair,direction);
                moveFlag = true;
            } catch (IllegalArgumentException e) {
                // quietly ignore if invalid. person skips their turn
                moveFlag = false;
                String msg = e.getMessage();
                if(msg.equals("Cannot move person. Target coordinates are occupied")){
                    setBredChild(pair,direction);
                }
            }


            if(moveFlag){
                CoordinatePair newPair = new CoordinatePair(pair.getX()+direction.getX(),pair.getY()+direction.getY());
                hasMoved.add(hs.get(newPair));
            }
            else
                hasMoved.add(hs.get(pair));

        }
    }

    public void setBredChild(@NotNull CoordinatePair pair, @NotNull Direction direction){
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);
        MiniPerson child = locationService.getPerson(pair).breed(locationService.getPerson(newPair));

        if(child == null) locationService.getPerson(pair).fight(locationService.getPerson(newPair));
        else locationService.setPerson(child, locationService.generateRandomCoordinates());
    }

}