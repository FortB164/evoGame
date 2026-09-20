import game.Game;
import utilities.Logger;

import static utilities.PrintHelper.print;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();

        game.size(5,5);
        game.simSpeed(500);
        game.population(8);
        game.start();

        Logger logger = Logger.getLogger();
        print(logger.getSimLog());



        // also, right now all persons move by one, I propose we make them move by their speed
        // their strength decides their fights (to the death)
        // their iq decides their chances to run away from a fight, as in both fighters survive


    }

}
