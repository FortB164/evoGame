package populationBoard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import org.jetbrains.annotations.NotNull;

import miniPeople.MiniPerson;
import miniPeople.dataObjects.Gender;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.Logger;
import utilities.personHelper;


public class BoardController {

    Board board;
    BoardLocationService locationService;
    public HashMap<MiniPerson, String> names =  new HashMap<>();
    Logger logger = Logger.getLogger();
    // monotonically increasing so names are never reused once a person dies
    int maleNameCounter = 0;
    int femaleNameCounter = 0;

    // board stores by coordinates -> person
    // names stores by person -> name
    // so to get the name, we first get the coordinates, then the person, then the name

    public BoardController(Board board) {
        this.board = board;
        locationService = new BoardLocationService(board);
    }


    public void initializePopulation(int count) {
        if (count > board.getFullSize()) {
            throw new IllegalArgumentException("Cannot add more people than board size");
        }

        for(int i = 0 ; i < count; i++) {

            MiniPerson mp = personHelper.generateMiniPerson();
            CoordinatePair pair = locationService.generateRandomCoordinates();

            String name = assignName(mp);

            names.put(mp, name);
            locationService.setPerson(pair, mp);

            logger.addAction(names.get(mp) + " added to board");
        }
    }

    public String assignName(MiniPerson mp){
        if (mp.getGender() == Gender.MALE) {
            maleNameCounter++;
            return "M" + maleNameCounter;
        }
        femaleNameCounter++;
        return "F" + femaleNameCounter;
    }

    // method to count genders. Gives answer in array of {Male, Female}
    public int[] genderCount(){

        int [] genderCount = new int [2];

        HashMap<CoordinatePair, MiniPerson> hs = board.getPopulationMap();

        for(MiniPerson mp : hs.values()){
            if(mp.getGender() == Gender.MALE){
                genderCount[0]++;
                continue;
            }
            genderCount[1]++;
        }

        return genderCount;
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
                    logger.addAction(names.get(locationService.getPerson(pair)) + " has collided with " + names.get(locationService.getPerson(newPair)) + ".");
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
        if (person.lifespan < 1){
            logger.addAction(names.get(locationService.getPerson(pair)) + " has died of old age. Their stats were: " + locationService.getPerson(pair).getAveragedStats());
            locationService.removePerson(pair);

        }
    }

    public void handleBreeding(@NotNull CoordinatePair pair, @NotNull Direction direction){
        // when move failed into a direction if occupied, breed with person in those new coords
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        CoordinatePair newPair = new CoordinatePair(newX, newY);

        MiniPerson parent1 = locationService.getPerson(pair);
        MiniPerson parent2 = locationService.getPerson(newPair);

        if(parent1 == null || parent2 == null) return;


        if(parent1.getGender() == parent2.getGender()){
            logger.addAction(names.get(parent1) + " and " +  names.get(parent2) + " are the same gender. A fight has broken out!");
            handleFights(pair, newPair);
            // one of the two same-gender combatants is now dead, so breeding cannot happen
            return;
        }

        MiniPerson child = parent1.breed(parent2);
        if(child == null) logger.addAction("Breeding between " + names.get(parent1) + " and " + names.get(parent2) + "has failed.");
        else if (locationService.hasAvailableSpace()) {
                CoordinatePair childPair = locationService.generateRandomCoordinates();
                locationService.setPerson(childPair, child);
                names.put(child, assignName(child));
                logger.addAction("A child of " + names.get(parent1) + " and " + names.get(parent2) + " is born at" + childPair.toString());
                // if breeding successful, add child to random coords
        }

    }

    public void handleFights(CoordinatePair pair1, CoordinatePair pair2){
        // get person from each coordinate
        MiniPerson mp1 = locationService.getPerson(pair1);
        MiniPerson mp2 = locationService.getPerson(pair2);
        // make them fight, it will give result of winner-loser
        MiniPerson[] winnerLoser = mp1.fight(mp2);

        // first index is winner, last is loser
        // if first index is mp1, remove mp2

        if(winnerLoser[0].equals(mp1)){
            locationService.removePerson(pair2);
        }

        // otherwise the first index would be mp2, in which case remove mp1
        else {
            locationService.removePerson(pair1);
        }

        logger.addAction(names.get(winnerLoser[0]) + " has killed " + names.get(winnerLoser[1]) + ".");

    }

}