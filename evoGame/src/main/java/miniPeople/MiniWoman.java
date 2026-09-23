package miniPeople;
import java.util.ArrayList;

import utilities.ListHelper;

public class MiniWoman extends MiniPerson {
    
    public MiniWoman(ArrayList<Double> strength, ArrayList<Double> speed, ArrayList<Double> iq){

        this.strength = ListHelper.scaleArrayList(strength, 0.666);
        this.speed = ListHelper.scaleArrayList(speed, 0.8);
        this.iq = ListHelper.scaleArrayList(iq, 1.25);
    }
}
