package utilities;

import java.util.ArrayList;
import java.util.Random;

public class ListHelper {

    public static ArrayList<Double> generateDoubleArrayList(int size, int from, int to){
        Random random = new Random();
        ArrayList<Double> tempArrList = new ArrayList<>();
        for(int i = 0; i < size; i++){
            tempArrList.add(random.nextDouble(from, to));
        }
        return tempArrList;
    }

    public static double arrayListAverage(ArrayList<Double> arr){
        double temp = 0;
        for (Double aDouble : arr) {
            temp += aDouble;
        }
        return temp/arr.size();
    }
    
    public static ArrayList<Double> scaleArrayList(ArrayList<Double> array, double factor) {
        ArrayList<Double> scaledArrayList = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
            scaledArrayList.add(i, array.get(i)*factor);

		}
		return scaledArrayList;
	}

    public static ArrayList<Double> mergeArrayList(ArrayList<Double> array1, ArrayList<Double> array2){
        ArrayList<Double> mergedArrayList = new ArrayList<>();
        mergedArrayList.addAll(array1);
        mergedArrayList.addAll(array2);
        return mergedArrayList;
    }

}
