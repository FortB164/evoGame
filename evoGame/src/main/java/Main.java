import game.Game;
import utilities.Logger;

import static utilities.printHelper.print;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();

        game.size(100,100);
        game.simSpeed(250);
        game.population(250);
        game.start();

        Logger logger = Logger.getLogger();
        print(logger.getSimLog());



        // also, right now all persons move by one, I propose we make them move by their speed
        // their strength decides their fights (to the death)
        // their iq decides their chances to run away from a fight, as in both fighters survive


    }

}
