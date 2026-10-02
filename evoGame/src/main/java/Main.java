import game.Game;
import static utilities.PrintHelper.print;
import utilities.logger.Logger;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();

        game.size(5,5);
        game.simSpeed(500);
        game.population(8);
        game.start();

        Logger logger = Logger.getLogger();
        print(logger.getActionLog());
        logger.clearActions();
        print(logger.getErrorLog());
        logger.clearErrors();

        print("Sim ran for " + game.getIterations() + " iterations");



        // ancestry model exists and tracks ancestries but isnt being shown anywhere on the ui
        // same with the logger
        // currently adding a history manager for board sim, aka a board state keeper
    }

}