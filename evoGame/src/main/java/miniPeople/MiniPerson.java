package miniPeople;
import java.util.ArrayList;
import java.util.Random;

import org.jetbrains.annotations.NotNull;

import miniPeople.dataObjects.Gender;
import utilities.listHelper;
import static utilities.listHelper.mergeArrayList;

public abstract class MiniPerson {

    Gender gender = this instanceof MiniMan ? Gender.MALE : this instanceof MiniWoman ? Gender.FEMALE : Gender.NULL;
    
    public ArrayList<Double> strength = new ArrayList<>();
    public ArrayList<Double> speed = new ArrayList<>();
    public ArrayList<Double> iq = new ArrayList<>();

    public int lifespan = 50;

    public MiniPerson(){
    }

    public String getAveragedStats(){
        return "Strength: " + (listHelper.arrayListAverage(strength)) + "\n" +
                "Speed: " + (listHelper.arrayListAverage(speed)) + "\n" +
                "IQ: " + (listHelper.arrayListAverage(iq));
    }

    public Gender getGender(){
        return gender;
    }
    
    public MiniPerson[] fight(MiniPerson mp){

        Random random = new Random();
        double strengthA = listHelper.arrayListAverage(this.strength);
        double strengthB = listHelper.arrayListAverage(mp.strength);

        double totalStrength = strengthA + strengthB;
        double roll = random.nextDouble()*totalStrength;


        // winner is in array[0] and loser is in array[1]
        if(roll <= listHelper.arrayListAverage(this.strength)){
            return new MiniPerson[] {this, mp};
        }

        // here order is swapped, meaning the other person won instead
        return new MiniPerson[] {mp, this};
    }


    public MiniPerson breed(@NotNull MiniPerson mp) {
        if (this.getGender() == mp.getGender()){
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