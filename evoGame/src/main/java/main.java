import populationBoard.populationBoard;
import populationBoard.populationBoardController;
import static utilities.util.print;
import miniPeople.*;

public class main {
    public static void main(String[] args) {

        // just playing around with the code here
        populationBoard board = new populationBoard(25,25);
        populationBoardController controller = new populationBoardController(board);

        miniPerson mp1 = controller.generateMiniPerson();
        miniPerson mp2 = controller.generateMiniPerson();

        miniPerson mp3 = mp1.breed(mp1,mp2);

        print("Parent 1: " + mp1.getGender());
        print(mp1.getAveragedStats());
        print("Parent 2: "  + mp2.getGender());
        print(mp2.getAveragedStats());
        print("Child: " +  mp3.getGender());
        print(mp3.getAveragedStats());

        print(mp1.strength);
        print(mp2.strength);
        print(mp3.strength);
    }

}
