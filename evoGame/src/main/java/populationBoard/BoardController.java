package populationBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import miniPeople.MiniPerson;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.personHelper;

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

        Random random = new  Random();
        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();
        Set<CoordinatePair> keysCopy = new HashSet<>(hs.keySet()); // iterate over key cope not original other concurrent access problem
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
                    handleBreeding(pair,direction);
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

            agePerson(moveFlag ? newPair : pair);
            if (collision) agePerson(newPair);

        }
    }

    private void agePerson(CoordinatePair pair) {
        MiniPerson person = locationService.getPerson(pair);
        if (person == null) return;

        person.lifespan--;
        if (person.lifespan < 1) locationService.removePerson(pair);
    }

    public void handleBreeding(@NotNull CoordinatePair pair, @NotNull Direction direction){
        // when move failed into a direction if occupied, breed with person in those new coords
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);

        MiniPerson parent1 = locationService.getPerson(pair);
        MiniPerson parent2 = locationService.getPerson(newPair);

        // only 60% chance for breeding to succeed
        Random random = new  Random();
        int k = random.nextInt(100);
        if(k > 40){
            MiniPerson child = parent1.breed(parent2);

            if(child == null) handleFights(pair, newPair); // if both genders are same, breeding not possible so they fight instead
            else if (locationService.hasAvailableSpace()) {
                locationService.setPerson(child, locationService.generateRandomCoordinates()); // if breeding successful, add child to random coords
            }
        }
    }

    public void handleFights(CoordinatePair pair1, CoordinatePair pair2){
        // get person from each coordinate
        MiniPerson mp1 = locationService.getPerson(pair1);
        MiniPerson mp2 = locationService.getPerson(pair2);
        // make them fight, it will give result of winner-loser
        MiniPerson[] winnerLoser = mp1.fight(mp2);

        // first index is winner, last is loser
        // if first index is mp1, then remove mp2
        if(winnerLoser[0].equals(mp1)){
            locationService.removePerson(pair2);
        }
        // and if first index is mp2, remove mp1
        else if (winnerLoser[0].equals(mp2)) {
            locationService.removePerson(pair1);
        }

    }

}