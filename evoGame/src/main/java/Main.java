import game.Game;

public class Main {
    public static void main(String[] args) {

        Game game = new Game();
        game.size(3,3);
        game.population(2);
        game.start();

        // also, right now all persons move by one, I propose we make them move by their speed
        // their strength decides their fights (to the death)
        // their iq decides their chances to run away from a fight, as in both fighters survive


    }

}
