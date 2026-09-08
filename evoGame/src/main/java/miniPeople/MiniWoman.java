package miniPeople;
import utilities.util;

import java.util.ArrayList;

public class MiniWoman extends MiniPerson {
    
    public MiniWoman(ArrayList<Double> strength, ArrayList<Double> speed, ArrayList<Double> iq){

        this.strength = strength;
        this.speed = speed;
        this.iq = iq;

        
        this.strength = util.scaleArrayList(strength, 0.666);
        this.speed = util.scaleArrayList(speed, 0.8);
        this.iq = util.scaleArrayList(iq, 1.25);
        
    }

    public void fight(MiniPerson mp){

    }

    public MiniPerson breed(MiniPerson mp){
        return null;
    }
    
}
