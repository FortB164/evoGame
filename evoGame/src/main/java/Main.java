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


        // ancestry model exists and tracks ancestries but isnt being shown anywhere on the ui
        // same with the logger
        // currently adding a history manager for board sim, aka a board state keeper
    }

}