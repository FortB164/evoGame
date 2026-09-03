package populationBoard;

import miniPeople.miniPerson;

public class populationBoardController {

	populationBoard board;
	
	
	public populationBoardController(populationBoard board) {
		this.board = board;
	}
	
	
	
	public miniPerson getPerson(int x, int y) {
        board.validateCoordinate(x, y);
        return board.populationMap.get(new coordinatePair(x, y));
    }
	
    public void setPerson(miniPerson person, int x, int y) {
    	board.validateCoordinate(x, y);
		board.populationMap.put(new coordinatePair(x, y), person);
	}
    
    public miniPerson removePerson(int x, int y) {
    	board.validateCoordinate(x, y);
		return board.populationMap.remove(new coordinatePair(x, y));
	}
    

    

    public void movePerson(int oldX, int oldY, int newX, int newY) {
        if (!board.isOccupied(oldX, oldY)) {
            throw new IllegalArgumentException("No person at the old coordinates");
        }

        if (board.isOccupied(newX, newY)) {
            throw new IllegalArgumentException("New coordinates are already occupied");
        }

        setPerson(removePerson(oldX, oldY) , newX, newY);
    }

    public void movePersonByOne(int x, int y, String direction) {
        board.validateCoordinate(x, y);

        if (!board.isOccupied(x, y)) {
            throw new IllegalArgumentException("No person at the given coordinates");
        }

        int newX = x;
        int newY = y;

        switch (direction.toLowerCase()) {
            case "up":
                newY -= 1;
                break;
            case "down":
                newY += 1;
                break;
            case "left":
                newX -= 1;
                break;
            case "right":
                newX += 1;
                break;
            default:
                throw new IllegalArgumentException("Invalid direction. Use 'up', 'down', 'left', or 'right'.");
        }

        movePerson(x, y, newX, newY);

    }
	
	
}
