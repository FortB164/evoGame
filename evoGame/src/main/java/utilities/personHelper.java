package utilities;

import miniPeople.MiniMan;
import miniPeople.MiniPerson;
import miniPeople.MiniWoman;

public class personHelper {

    public static MiniPerson generateMiniPerson() {
        MiniPerson mp;

        if ((int)(Math.random() * 2) == 0) {
            mp = new MiniMan(
                    listHelper.generateDoubleArrayList(5, 10, 100),
                    listHelper.generateDoubleArrayList(5, 10, 100),
                    listHelper.generateDoubleArrayList(5, 10, 100)
            );
        } else {
            mp = new MiniWoman(
                    listHelper.generateDoubleArrayList(5, 10, 100),
                    listHelper.generateDoubleArrayList(5, 10, 100),
                    listHelper.generateDoubleArrayList(5, 10, 100)
            );
        }

        return mp;
    }
}
