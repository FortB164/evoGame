package miniPeople;
import utilities.util;

import java.util.ArrayList;

public abstract class MiniPerson {
    
    public ArrayList<Double> strength = new ArrayList<>();
    public ArrayList<Double> speed = new ArrayList<>();
    public ArrayList<Double> iq = new ArrayList<>();

    public MiniPerson(){
    }

    public String getAveragedStats(){
        return "Strength: " + (util.arrayListAverage(strength)) + "\n" +
                "Speed: " + (util.arrayListAverage(speed)) + "\n" +
                "IQ: " + (util.arrayListAverage(iq));
    }

    public Gender getGender(){
        if (this instanceof MiniMan) return Gender.MALE;
        else if (this instanceof MiniWoman) return Gender.FEMALE;
        return Gender.NULL;
    }
    
    public abstract void fight(MiniPerson mp);
    public abstract MiniPerson breed(MiniPerson mp);
}