package miniPeople;
import java.util.ArrayList;
import java.util.Random;

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
    public miniPerson breed(miniPerson mp1, miniPerson mp2) {
        if (mp1.getGender() == mp2.getGender()){
            fight(mp1, mp2);
            return null;
        }

        ArrayList<Double> mergedStrength =  mergeArrayList(mp1.strength, mp2.strength);
        ArrayList<Double> mergedSpeed =  mergeArrayList(mp1.speed, mp2.speed);
        ArrayList<Double> mergedIq =  mergeArrayList(mp1.iq, mp2.iq);

        Random random = new Random();
        ArrayList<Double> kidStr = new ArrayList<>();
        ArrayList<Double> kidSpd = new ArrayList<>();
        ArrayList<Double> kidIq =  new ArrayList<>();

        for(int i = 0; i < mp1.strength.size(); i++){
            int randomIndexFromMerged = random.nextInt(0, mergedStrength.size());
            double randomFromMerged = mergedStrength.get(randomIndexFromMerged);
            kidStr.add(randomFromMerged);
        }

        for(int i = 0; i < mp1.speed.size(); i++){
            int randomIndexFromMerged = random.nextInt(0, mergedSpeed.size());
            double randomFromMerged = mergedSpeed.get(randomIndexFromMerged);
            kidSpd.add(randomFromMerged);
        }

        for(int i = 0; i < mp1.iq.size(); i++){
            int randomIndexFromMerged = random.nextInt(0, mergedIq.size());
            double randomFromMerged = mergedIq.get(randomIndexFromMerged);
            kidIq.add(randomFromMerged);
        }

        if ((random.nextInt(0, 2) == 0)) {
            return new miniMan(
                    kidStr,
                    kidSpd,
                    kidIq
            );
        }

        else return new miniWoman(
                kidStr,
                kidSpd,
                kidIq
        );
    }

}