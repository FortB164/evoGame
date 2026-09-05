package miniPeople;
import utilities.util;

import java.util.ArrayList;

public abstract class miniPerson{
    
    ArrayList<Double> strength = new ArrayList<>();
    ArrayList<Double> speed = new ArrayList<>();
    ArrayList<Double> iq = new ArrayList<>();

    public miniPerson(){
    }

    public String getAveragedStats(){
        return "Strength: " + (util.arrayListAverage(strength)) + "\n" +
                "Speed: " + (util.arrayListAverage(speed)) + "\n" +
                "IQ: " + (util.arrayListAverage(iq));
    }

    public Gender getGender(){
        if (this instanceof miniMan) return Gender.MALE;
        else if (this instanceof miniWoman) return Gender.FEMALE;
        return Gender.NULL;
    }
    
    public abstract void fight(miniPerson mp1, miniPerson mp2);
    public abstract miniPerson breed(miniPerson mp1, miniPerson mp2);
}