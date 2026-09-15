package utilities;

import miniPeople.MiniMan;
import miniPeople.MiniPerson;
import miniPeople.MiniWoman;

import java.util.Random;

public class personHelper {

    public static MiniPerson generateMiniPerson() {
        Random random = new Random();
        MiniPerson mp;
        int randomGender = random.nextInt(2);

        if (randomGender== 0) {
            mp = new MiniMan(
                    listHelper.generateDoubleArrayList(5, 10, 100),
                    listHelper.generateDoubleArrayList(5, 1, 5),
                    listHelper.generateDoubleArrayList(5, 10, 100)
            );
        } else {
            mp = new MiniWoman(
                    listHelper.generateDoubleArrayList(5, 10, 100),
                    listHelper.generateDoubleArrayList(5, 1, 5),
                    listHelper.generateDoubleArrayList(5, 10, 100)
            );
        }

        mp.lifespan = random.nextInt(50,81);

        return mp;
    }
}
