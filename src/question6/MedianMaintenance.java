package question6;

import static common.utils.Utils.readInput;

import java.util.Arrays;
 
public class MedianMaintenance {
 
    public static void main(String[] args) {
    	int size=10000;
        int[] list = new int[size];
        int[] input=readInput("Median.txt", size);
        int[] output=new int[size];
        
        long sum = 0;
 
        for (int i = 0; i < list.length; i++) {
 
            list[i] = input[i];
            Arrays.sort(list, 0, i+1);
            int median=list[i/2];
            sum+=median;
            output[i]=median;
        }
        System.out.println(sum%10000);
        try{
        for(int i:output){
        	System.out.print(i+" ");
        }
        }catch(Exception e){
        	e.printStackTrace();
        }
 
    }
}
