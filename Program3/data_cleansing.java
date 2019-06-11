import java.io.*;
import java.util.Scanner;

public class data_cleansing {

	public static String[] setColumn(){
		String[] column = {"FYEAR" , "STATE" , "COUNTY" , "LEAEID" , "LEACTDSNUM", "LEANAME" , "SEID" , "SCTDSNUM" , "SNAME" , "CHARS" , "MathMSS" , "MathPFFB" , "MathPA" , "MathPM" , "MathPE" , "MathPP" , "ReadingMSS", "ReadingPFFB" , "ReadingPA" , "ReadingPM" , "ReadingPE" , 
				   			"ReadingPP" , "WritingMSS" , "WritingPFFB" , "WritingPA" , 
				   			"WritingPM" , "WritingPE" , "WritingPP" , "ScienceMSS" , "SciencePFFB" , 
				   			"SciencePA" , "SciencePM" , "SciencePE" , "SciencePP"};
		return column;
	}

	public static String[] seperate(String line, int size) {

		int num=0;
		boolean split=true;
		String[] answer= new String[size];			
		
		answer[0]="";
		for(int i=0 ; i<line.length() ; i++){
			if(line.charAt(i)=='"'){
				split=!split;
				continue;
			}
			if(line.charAt(i)==',' && split){
				num++;
				answer[num] = "";
				if(num>=size){
					return null;
				}
				continue;
			}
			answer[num]+=line.charAt(i);
		}
		
		return answer;
	}

	public static String[] setColumnTypes(){
		String[] columnTypes={"integer" , "varchar(16)" , "varchar(32)" , "varchar(8)" , "varchar(16)", "char(80)" , "varchar(8)" , "varchar(16)" , "char(80)" , "varchar(4)" , "integer" , "integer" , "integer" , "integer" , "integer" , 
		   			"integer" , "integer" , "integer" , "integer" , "integer" , 
		   			"integer" , "integer" , "integer" , "integer" , "integer" , 
		   			"integer" , "integer" , "integer" , "integer" , "integer" , 
		   			"integer" , "integer" , "integer" , "integer"};
		return columnTypes;
	}

	public static boolean check(String word) { 
	    word=word.replace(" ","");
	    word=word.replace("\t","");
	    return word.matches("[-+]?\\d*\\.?\\d+");  
	} 

	public static void main (String [] args) throws IOException{

		String inputCSV = "aims_2014.csv", table = "AIM_AND_AIMSA_2014";
		String[] column = setColumn(), columnTypes= setColumnTypes();
		int leng = column.length;
		File writeFile = new File("insert_2014.txt");
		FileOutputStream fos = new FileOutputStream(writeFile);
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
		Scanner scanFile;
		try {
			scanFile = new Scanner(new File(inputCSV));
			   
			   String first = "INSERT INTO "+table+" VALUES (", second=null, third = ");", line=null;
			   boolean bool=true;
			   while(scanFile.hasNextLine()){
			           bool=true;
				   line=scanFile.nextLine(); 
				   String[] save = seperate(line, leng);
				   
				   if(save == null){
					   	break;
				   }
				   second="";
				for(int i=0;i<leng;i++){
				   if(save[i].length()==0){
				   	   bool=false;
				   }else if(columnTypes[i].compareTo("integer")!=0){
					   second+='\'';
					   second+= save[i].replaceAll("\'", "\'\'");
					   second+='\'';
				   }
				   else if(!check(save[i])){
					  second+="NULL";
			  	   }
				   else{
					second+=save[i];
				   }
				       if(i<leng-1){
					second+=" , ";
					}
				   }
				   if(bool){
				   		bw.write(first+second+third+"\n");	
				   }
			   }
			   System.out.println();
			   bw.close();
			   scanFile.close();
		} catch (FileNotFoundException e) {
		}

	   }

}

