package miniPeople;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Random;

import static utilities.util.mergeArrayList;

public class MiniMan extends MiniPerson {

    public MiniMan(ArrayList<Double> strength, ArrayList<Double> speed, ArrayList<Double> iq){
        this.strength = strength;
        this.speed = speed;
        this.iq = iq;
    }

    public void fight(MiniPerson mp){

    }
    // WORKOUT GENETICS LOGIC. ASAP! OR ADD GENETICS PACKAGE! NEXT WORK SESSION SHOULD IMPLEMENT THIS WITHOUT FAIL!!
    public MiniPerson breed(@NotNull MiniPerson mp) {
        if (this.getGender() == mp.getGender()){
            fight(mp);
            return null;
        }

        ArrayList<Double> mergedStrength =  mergeArrayList(this.strength, mp.strength);
        ArrayList<Double> mergedSpeed =  mergeArrayList(this.speed, mp.speed);
        ArrayList<Double> mergedIq =  mergeArrayList(this.iq, mp.iq);

        Random random = new Random();
        ArrayList<Double> kidStr = new ArrayList<>();
        ArrayList<Double> kidSpd = new ArrayList<>();
        ArrayList<Double> kidIq =  new ArrayList<>();

        for(int i = 0; i < this.strength.size(); i++){
            int randomIndexFromMerged = random.nextInt(0, mergedStrength.size());
            double randomFromMerged = mergedStrength.get(randomIndexFromMerged);
            kidStr.add(randomFromMerged);
        }

        for(int i = 0; i < this.speed.size(); i++){
            int randomIndexFromMerged = random.nextInt(0, mergedSpeed.size());
            double randomFromMerged = mergedSpeed.get(randomIndexFromMerged);
            kidSpd.add(randomFromMerged);
        }

        for(int i = 0; i < this.iq.size(); i++){
            int randomIndexFromMerged = random.nextInt(0, mergedIq.size());
            double randomFromMerged = mergedIq.get(randomIndexFromMerged);
            kidIq.add(randomFromMerged);
        }

        if ((random.nextInt(0, 2) == 0)) {
            return new MiniMan(
                    kidStr,
                    kidSpd,
                    kidIq
            );
        }

        else return new MiniWoman(
                kidStr,
                kidSpd,
                kidIq
        );
    }

}