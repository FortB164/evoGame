package utilities;

import java.util.Random;

public class util {
    static Random random = new Random();

    public static int[] generateIntArray(int size, int from, int to){
        return random.ints(size, from, to).toArray();
    }


    public static double[] generateDoubleArray(int size, int from, int to){
        return random.doubles(size, from, to).toArray();
    }
    

    public static double arrayAverage(double [] arr){
        double temp = 0;
        for(int i = 0; i < arr.length ; i++){
            temp+= arr[i];
        }
        return temp/arr.length;
    }

    public static <T> void print(T value) {
        System.out.println(value);
    }
    
    public static double[] scaleArray(double[] array, double factor) {
		double[] adjustedArray = new double[array.length];
		for (int i = 0; i < array.length; i++) {
			adjustedArray[i] = array[i] * factor;
		}
		return adjustedArray;
	}
    

}
