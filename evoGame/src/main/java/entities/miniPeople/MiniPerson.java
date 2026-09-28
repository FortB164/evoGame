package entities.miniPeople;
import java.util.ArrayList;
import java.util.Random;

import interfaces.Copyable;
import org.jetbrains.annotations.NotNull;

import entities.dataObjects.Gender;
import utilities.ListHelper;

import static utilities.ListHelper.mergeArrayList;

public class MiniPerson implements Copyable<MiniPerson> {

    Gender gender = this instanceof MiniMan ? Gender.MALE : Gender.FEMALE;

    public ArrayList<Double> strength = new ArrayList<>();
    public ArrayList<Double> speed = new ArrayList<>();
    public ArrayList<Double> iq = new ArrayList<>();

    private int lifespan; // assigned randomly in a range when initializing the population

    public MiniPerson(){
    }

    // copy constructor
    public MiniPerson(MiniPerson other) {
        this.gender = other.gender;
        this.strength = new ArrayList<>(other.strength);
        this.speed = new ArrayList<>(other.speed);
        this.iq = new ArrayList<>(other.iq);
        this.lifespan =  other.lifespan;
    }

    @Override
    public MiniPerson copy(){
        return new MiniPerson(this);
    }

    public void decreaseLifespan(int aging){
        lifespan += aging;
    }
    public int getLifespan(){
        return lifespan;
    }
    public void setLifespan(int lifespan){
        this.lifespan = lifespan;
    }

    public String getAveragedStats(){
        return "Strength: " + (ListHelper.arrayListAverage(strength)) + "\n" +
                "Speed: " + (ListHelper.arrayListAverage(speed)) + "\n" +
                "IQ: " + (ListHelper.arrayListAverage(iq));
    }

    public Gender getGender(){
        return gender;
    }
    
    public MiniPerson[] fight(MiniPerson mp){
        MiniPerson [] winnerLoser = new MiniPerson[2];
        Random random = new Random();
        double strengthA = ListHelper.arrayListAverage(this.strength);
        double strengthB = ListHelper.arrayListAverage(mp.strength);

        double iqA = ListHelper.arrayListAverage(this.iq);
        double iqB = ListHelper.arrayListAverage(mp.iq);

        double totalStrength = strengthA + strengthB;
        double totalIq = iqA + iqB;

        double strengthBasedWin = random.nextDouble()*totalStrength;
        double iqBasedWin = random.nextDouble()*totalIq;
        double highestIq = Math.max(iqA, iqB);

        // higher iq persons escapes
        // both survive
        // no one is removed
        // for this reason, this is a very rare occurrence

        if(iqBasedWin <= highestIq/10){
            return new MiniPerson[0];
        }

        // winner is in array[0] and loser is in array[1]
        if(strengthBasedWin <= ListHelper.arrayListAverage(this.strength)){
            winnerLoser[0] = this;
            winnerLoser[1] = mp;
            return  winnerLoser;
        }

        // here order is swapped, meaning the other person won instead
        winnerLoser[0] = mp;
        winnerLoser[1]  = this;
        return winnerLoser;
    }


    public MiniPerson breed(@NotNull MiniPerson mp) {
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

        int k = random.nextInt(100);

        /*
        if random bound is 100, possible numbers are 0 to 99

        k above 40 means 40 to 99 which is only 59, so 59% chance only not 60

        so do above 39

        only 60% chance for breeding to succeed
         */

        if(k > 39){
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

        return null;

    }
}