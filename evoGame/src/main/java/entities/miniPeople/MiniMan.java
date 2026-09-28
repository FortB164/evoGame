package entities.miniPeople;
import java.util.ArrayList;

public class MiniMan extends MiniPerson {

    public MiniMan(ArrayList<Double> strength, ArrayList<Double> speed, ArrayList<Double> iq){
        this.strength = strength;
        this.speed = speed;
        this.iq = iq;
    }
}