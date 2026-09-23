package populationBoard;

import miniPeople.MiniMan;
import miniPeople.MiniPerson;
import miniPeople.MiniWoman;
import org.jetbrains.annotations.NotNull;
import populationBoard.dataObjects.CoordinatePair;
import populationBoard.dataObjects.Direction;
import utilities.ListHelper;
import utilities.Logger;

import java.util.Random;

public class BoardLife {

    BoardLocationService locationService;
    BoardNames names;
    BoardAncestry ancestry;
    Logger logger;

    public BoardLife(BoardLocationService locationService, BoardNames names, BoardAncestry ancestry) {
        this.locationService = locationService;
        this.names = names;
        this.logger = Logger.getLogger();
        this.ancestry = ancestry;
    }

    public void handleAging(CoordinatePair pair) {
        MiniPerson person = locationService.getPerson(pair);
        if (person == null) return;

        person.decreaseLifespan(-1);
        if (person.getLifespan() < 1){
            logger.addAction(names.getNameOf(locationService.getPerson(pair)) + " has died of old age. Their stats were: " + locationService.getPerson(pair).getAveragedStats());
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
            logger.addAction(names.getNameOf(parent1) + " and " +  names.getNameOf(parent2) + " are the same gender. A fight has broken out!");
            handleFights(pair, newPair);
            // one of the two same-gender combatants is now dead, so breeding cannot happen
            return;
        }

        MiniPerson child = parent1.breed(parent2);
        if(child == null) logger.addAction("Breeding between " + names.getNameOf(parent1) + " and " + names.getNameOf(parent2) + "has failed.");
        else if (locationService.hasAvailableSpace()) {
            CoordinatePair childPair = locationService.generateRandomCoordinates();
            names.nameAndSet(child);
            locationService.setPerson(childPair, child);
            ancestry.handleAncestry(child, parent1, parent2);

            logger.addAction("A child of " + names.getNameOf(parent1) + " and " + names.getNameOf(parent2) + " is born at" + childPair.toString());
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

        logger.addAction(names.getNameOf(winnerLoser[0]) + " has killed " + names.getNameOf(winnerLoser[1]) + ".");
    }

    public static MiniPerson generateMiniPerson() {
        Random random = new Random();
        MiniPerson mp;
        int randomGender = random.nextInt(2);

        if (randomGender== 0) {
            mp = new MiniMan(
                    ListHelper.generateDoubleArrayList(5, 10, 100),
                    ListHelper.generateDoubleArrayList(5, 1, 5),
                    ListHelper.generateDoubleArrayList(5, 10, 100)
            );
        } else {
            mp = new MiniWoman(
                    ListHelper.generateDoubleArrayList(5, 10, 100),
                    ListHelper.generateDoubleArrayList(5, 1, 5),
                    ListHelper.generateDoubleArrayList(5, 10, 100)
            );
        }

        mp.setLifespan(random.nextInt(50,81));
        return mp;
    }
}
