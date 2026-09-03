package miniPeople;
import java.util.ArrayList;
import static utilities.util.mergeArrayList;

public class miniMan extends miniPerson{

    public miniMan(ArrayList<Double> strength, ArrayList<Double> speed, ArrayList<Double> iq){
        this.strength = strength;
        this.speed = speed;
        this.iq = iq;
    }

    public void fight(miniPerson mp1, miniPerson mp2){

    }
    // WORKOUT GENETICS LOGIC. ASAP! OR ADD GENETICS MODULE! NEXT WORK SESSION SHOULD IMPLEMENT THIS WITHOUT FAIL!!
    public miniPerson breed(miniPerson mp1, miniPerson mp2){
        if(mp1.getGender() == mp2.getGender()) fight(mp1, mp2);

        ArrayList<Double> mergedStrength =  mergeArrayList(mp1.strength, mp2.strength);
        ArrayList<Double> mergedSpeed =  mergeArrayList(mp1.strength, mp2.strength);
        ArrayList<Double> mergedIq =  mergeArrayList(mp1.strength, mp2.strength);

        return null;
    }

}