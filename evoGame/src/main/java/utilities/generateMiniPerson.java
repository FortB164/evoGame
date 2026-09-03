package utilities;
import miniPeople.*;
public class generateMiniPerson {

    miniPerson mp;

    public generateMiniPerson() {}

    public miniPerson generate() {


        if ((int)(Math.random() * 2) == 0) {
            mp = new miniMan(
                util.generateDoubleArray(2, 10, 100),
                util.generateDoubleArray(2, 10, 100),
                util.generateDoubleArray(2, 10, 100)
            );

        }
        
        else {
            mp = new miniWoman(
                util.generateDoubleArray(2, 10, 100),
                util.generateDoubleArray(2, 10, 100),
                util.generateDoubleArray(2, 10, 100)
            );
        }

        return mp;
    }

}