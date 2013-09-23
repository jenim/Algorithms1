package common.utils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class Utils {

	public static int[] readInput(String fileName,int lineNumber) {
		File file=new File(fileName);
		FileInputStream is=null;
		DataInputStream dis=null;
		BufferedReader br=null;
		int output[]=new int[lineNumber];
		try {
			is=new FileInputStream(file);
			dis=new DataInputStream(is);
			br=new BufferedReader(new InputStreamReader(dis));
	
			int i=0;
			while(true){ 
				String s=br.readLine();
				if(s==null){
					break;
				}
				output[i++]=Integer.valueOf(s);
			}
			return output;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br!=null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(dis!=null){
				try {
					dis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is!=null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return output;
	}

	public Utils() {
		super();
	}
	
	public static List<String> readMap(String fileName){
		List<String> input=null;
		try {
			input=FileUtils.readLines(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return input;
	}
	
	public static int[][] readArray(String fileName, int nodesNumber){
		List<String> input=null;
		try {
			input=FileUtils.readLines(new File(fileName));
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(input==null){
			return null;
		}
		int [][] output=new int[nodesNumber][nodesNumber];
		for(int i=0;i<output.length;i++){
			for(int j=0;j<output.length;j++){
				if(i==j){
					output[i][j]=0;
				}else{
					output[i][j]=-1;
				}
			}
		}
		
		for(String line:input){
			String[] split=line.split("	");
			int tail=Integer.parseInt(split[0])-1;
			for(int i=1;i<split.length;i++){
				String next=split[i];
				int pos=next.indexOf(",");
				int head=Integer.parseInt(next.substring(0,pos))-1;
				int weight=Integer.parseInt(next.substring(pos+1));
				output[tail][head]=weight;
			}
		}
		for(int i=0;i<output.length;i++){
			for(int j=0;j<output.length;j++){
				System.out.print(output[i][j]+" | ");
			}
			System.out.println();
		}
		return output;
	}
	
	public static Long[] readMapIntegers(String fileName){
		List<String> input=readMap(fileName);
		if(input!=null && !input.isEmpty()){
			Long[] output=new Long[input.size()];
			for(int i=0;i<input.size();i++){
				String s=input.get(i);
				output[i]=Long.valueOf(s);
			}
			return output;
		}
		return null;
	}

}