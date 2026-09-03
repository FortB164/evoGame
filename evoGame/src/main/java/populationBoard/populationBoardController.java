package populationBoard;

import miniPeople.miniPerson;

public class populationBoardController {

	populationBoard board;
	
	
	public populationBoardController(populationBoard board) {
		this.board = board;
	}
	

	public miniPerson getPerson(int x, int y) {
        if(board.isOccupied(x, y)) return board.populationMap.get(new coordinatePair(x, y));
        else throw new IllegalArgumentException("Coordinates are empty");
    }
	
    public void setPerson(miniPerson person, int x, int y) {
    	if(!board.isOccupied(x, y)) board.populationMap.put(new coordinatePair(x, y), person);
        else throw new IllegalArgumentException("Coordinates are occupied");
	}

    // when you remove person it also gets the value of it, so you can store it somewhere
    public miniPerson removePerson(int x, int y) {
        if(!board.isOccupied(x, y)) return board.populationMap.remove(new coordinatePair(x, y));
        else throw new IllegalArgumentException("Coordinates are empty");
	}



    public void movePerson(int oldX, int oldY, int newX, int newY) {
        if(!board.isOccupied(oldX, oldY))
            throw new IllegalArgumentException("Old coordinates are not occupied");

        if(board.isOccupied(newX, newY))
            throw new IllegalArgumentException("New coordinates already occupied");

        setPerson(removePerson(oldX, oldY), newX, newY);
    }

    public void movePersonByOne(int x, int y, String direction) {

        switch (direction.toLowerCase()) {
            case "up":
                movePerson(x, y, x, y+1);
                break;
            case "down":
                movePerson(x, y, x, y-1);
                break;
            case "left":
                movePerson(x-1, y, x, y);
                break;
            case "right":
                movePerson(x+1, y, x, y);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction. Use 'up', 'down', 'left', or 'right'.");
        }
    }
}
