package populationBoard;

import miniPeople.MiniPerson;
import miniPeople.dataObjects.Gender;
import populationBoard.dataObjects.Board;
import populationBoard.dataObjects.CoordinatePair;

import java.util.HashMap;

public class BoardNames {
    public HashMap<MiniPerson, String> names =  new HashMap<>();
    int maleNameCounter = 0;
    int femaleNameCounter = 0;
    Board board;

    public BoardNames(Board board){
        this.board = board;
    }

    public void nameAndSet(MiniPerson mp){
        if (mp.getGender() == Gender.MALE) {
            maleNameCounter++;
            names.put(mp, "M" + maleNameCounter);
            return;
        }
        femaleNameCounter++;
        names.put(mp, "F" + femaleNameCounter);
    }

    public String getNameOf(MiniPerson mp){
        return names.get(mp);
    }
}