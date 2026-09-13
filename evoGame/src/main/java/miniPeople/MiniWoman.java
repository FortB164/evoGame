package miniPeople;
import java.util.ArrayList;

import utilities.listHelper;

public class MiniWoman extends MiniPerson {
    
    public MiniWoman(ArrayList<Double> strength, ArrayList<Double> speed, ArrayList<Double> iq){

        this.strength = listHelper.scaleArrayList(strength, 0.666);
        this.speed = listHelper.scaleArrayList(speed, 0.8);
        this.iq = listHelper.scaleArrayList(iq, 1.25);
        
    }
}
