package populationBoard;

import miniPeople.miniMan;
import miniPeople.miniPerson;
import miniPeople.miniWoman;
import utilities.util;

import java.util.Random;

public class populationBoardController {

	populationBoard board;
	
	
	public populationBoardController(populationBoard board) {
		this.board = board;
	}
	

	public miniPerson getPerson(coordinatePair pair) {

        if(board.isOccupied(pair)) {
            return board.getAtCoordinates(pair);
        }

        else throw new IllegalArgumentException("Coordinates are empty");
    }
	
    public void setPerson(miniPerson person, coordinatePair pair) {

    	if(!board.isOccupied(pair)) {
            board.putAtCoordinates(person, pair);
        }

        else throw new IllegalArgumentException("Coordinates are occupied");
	}

    // when you remove person it also gets the value of it, so you can store it somewhere
    public miniPerson removePerson(coordinatePair pair) {

        if(!board.isOccupied(pair)){
            return board.removeAtCoordinates(pair);
        }

        else throw new IllegalArgumentException("Coordinates are empty");
	}



    public void movePerson(coordinatePair oldPair, coordinatePair newPair) {
        if(!board.isOccupied(oldPair)){
            throw new IllegalArgumentException("Old coordinates are not occupied");
        }

        if(board.isOccupied(newPair)){
            throw new IllegalArgumentException("New coordinates already occupied");
        }

        setPerson(removePerson(oldPair), newPair);
    }

    public void movePersonByOne(coordinatePair pair, Direction direction) {
        int newX = pair.getX() + direction.getX();
        int newY = pair.getY() + direction.getY();
        coordinatePair newPair =  new coordinatePair(newX, newY);
        movePerson(pair, newPair);
    }

    public void initializePopulation(int x) {

        for(int i = 0; i < x; i++) {
            setPerson(generateMiniPerson(), generateRandomCoordinates());
        }

    }

    // do not confuse this with mp.breed()
    // this only generates person with random attributes without breeding
    // do not use this for child persons
    public miniPerson generateMiniPerson() {
        miniPerson mp;

        if ((int)(Math.random() * 2) == 0) {
            mp = new miniMan(
                    util.generateDoubleArrayList(2, 10, 100),
                    util.generateDoubleArrayList(2, 10, 100),
                    util.generateDoubleArrayList(2, 10, 100)
            );
        }

        else {
            mp = new miniWoman(
                    util.generateDoubleArrayList(2, 10, 100),
                    util.generateDoubleArrayList(2, 10, 100),
                    util.generateDoubleArrayList(2, 10, 100)
            );
        }

        return mp;
    }

    public coordinatePair generateRandomCoordinates(){
        Random random = new Random();
        int randX = random.nextInt(0, board.sizeX);
        int randY = random.nextInt(0, board.sizeY);
        return new coordinatePair(randX, randY);
    }

}