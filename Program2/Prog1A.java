import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Scanner;
/*
 * 	Name: Francis Kim
 *  Course: CSC460
 *  Assignment: Program 1 (partA)
 *  Instructor and TA: McCann, and Yawen Chen
 *  Description: From the given csv file which is Chicago Taxi data, I changed all those data to bin file and created bin file.
 * 				 Because binary files are generally faster and easier for a program to read and write than are text files.
 * 				 For this program, we have lines of data items that we need to store, and each lines items need to be stored as a group.
 * 				 Total length in each records should be the same.
 * 				 I only created one class which is Prog1A, and created three methods in class.
 * 				 1. setMaxLegnth
 * 				 2. setUpRightColumn
 * 				 3. dumpObject
 * 				 Those three methods will helped to implement my code, and change all data into binary code.
 *  The programs operational requirements: I used Java language, and input csv file should be located in current directory.
 *  Bugs:		There is no bugs in this assignment
 */



/*
 *  Class: Prog1A
 *  Purpose: This class handle everything of code, there are three methods which are setMaxLength, setUpRightColumn, and dumpObject
 *  		 There are 11 private static int variables. length1 to length 10 represent max length in each of string column, and TOTAL_LENGTH is the total bytes of column.
 */
public class Prog1A{

	// TOTAL_LENGTH is maximum total bytes of record	
	private static int TOTAL_LENGTH;
	// Each of length1 to length 10 represent all each of the String length
	private static int length1, length2, length3, length4, length5, length6, length7, length8, length9, length10;
	// numberOfRecords is how many records in the file
	private static long numberOfRecords;
	/*
	 *  setMaxLength():
	 *  this method get a max length of each string, and also get total bytes of records.
	 */
	public void setMaxLength(String file){
		String line;
		// count is number of column filed, and a to j is save length of original column field
		int count, a, b, c, d, e, f, g, h, i, j;
		// a2 to j2 is save max length of column field. If original column length (a to j) is longer than max length, then save original column length into max length(a2 to j2)
		int a2=0,b2=0,c2=0,d2=0,e2=0,f2=0,g2=0,h2=0,i2=0,j2=0;
		
		File readFile;
		
		readFile = new File(file); // csv file is read
			try {
				Scanner scan = new Scanner(readFile); // Scan the file
				scan.nextLine();		      // Skip the first line of file
				while(scan.hasNextLine()){
					count = 1;		      // count number field column
					line = scan.nextLine();       // save the line into String line
					
					for(String str: line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)){		// Split by comma except in double quotes
						str = str.replace("\"", "");		// remove the double quote
						str = str.replace("$", "");			// remove the dollar sign
						
						// This decide which is the max length in each column and save it into variable.
						if(count==1 || count==2 || count==3 || count==4 || count==7 || count==8  || count==16 || count==17 || count==20 || count==23){
							if(count==1){			// TripID
								a = str.length();       // a represent length of original TripId column
								if(a2 < a){
									a2 = a;		// a2 saved max length of TripIdcolumn 
								}
							}else if(count==2){		// TaxiID
								b = str.length();	// b represent length of original Taxi column
								if(b2 < b){
									b2 = b;         // b2 saved max length of Taxi
								}
							}else if(count==3){		// tripStartTimeStamp
								c = str.length();	// c represent length of original tripStartTimeStamp column
								if(c2 < c){
									c2 = c;		// c2 represent max length of tripStartTimeStamp
								}
							}else if(count==4){		// tripEndTimeStamp
								d = str.length();	// d represent length of original tripEndTimeStamp column
								if(d2 < d){		
									d2 = d;		// d2 represent max length of tripEndTimeStamp
								}
							}else if(count==7){		//pickUpCensusTrack
								e = str.length();	// e represent length of original pickUpCensusTrack
								if(e2 < e){
									e2 = e;		// e2 represent max length of pickUpCensusTrack
								}
							}else if(count==8){
								f = str.length();	// dropOffCensusTrack
								if(f2 < f){		// f represent length of original dropOffCensusTrack
									f2 = f;		// f2 represent max length of dropOffCensusTrack
								}
							}else if(count==16){		// paymentType
								g = str.length();	// g represent length of original paymentType
								
								if(g2 < g){
									g2 = g;		// g2 represent max length of paymentType
								}
							}else if(count==17){		// company
								h = str.length();	// h represent length of original company
								if(h2 < h){
									h2 = h;		// h2 represent max length of company
								}
							}else if(count==20){
								i = str.length();	// Point1( double dobule )
								
								if(i2 < i){		// i represent length of original Point1
									i2 = i;		// i2 represent max length of Point1
								}
							}else if(count==23){		// Point2( double double)
								j = str.length();	// j represent length of original Point2
								if(j2 < j){
									j2 = j;		// j2 represent max length of Point2
								}
							}
						}
						count++; // count until 23
					}
				}
				// After got max length, I saved them into length 1 to length 10
				length1 = a2; length2=b2; length3=c2; length4=d2; length5=e2; length6=f2; length7=g2; length8=h2; length9=i2; length10=j2;
				// TOTAL_LENGTH saved max total bytes of record ( int is 4 bytes, and double is 8 bytes )
				TOTAL_LENGTH = length1+length2+length3+length4+length5+length6+length7+length8+length9+length10+12+80;
			} catch (FileNotFoundException err) {
				// TODO Auto-generated catch block
				err.printStackTrace();
			}
	}

	/*
	 *  setUpRightColumn(ArrayList<String> column):
	 *  This method set up the strings and int properly that spec required.
	 *  Pad String to reach needed length, and save -1 if there is no integer between comma, etc.
	 */
	public ArrayList<String> setUpRightColumn(ArrayList<String> column){
		// count is count field until 23.
		int count=1;
		// strLengh = count a actual string lengh in each column and diff is save difference of length from max length and actual length
		int strLength, diff;
		// save all correct column into result
		ArrayList<String> result = new ArrayList<String>();
		result.clear();
		
		//  This for loop changed all column data to required data from the spec.
		//  Pad String to reach needed length, and save -1 if there is no integer between comma, etc.
		for(String s : column){
			// only can approach String fields
			if(count==1 || count==2 || count==3 || count==4 || count==7 || count==8  || count==16 || count==17 || count==20 || count==23){
				strLength = s.length();
				if(count==1){
					if(length1 > strLength){
						diff = length1 - strLength;	// diff is difference length of max and original in TripID
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad sapces
						}
					}
					result.add(s);
				}else if(count==2){
					if(length2 > strLength){
						diff = length2 - strLength;	// diff is difference length of max and original in taxiID
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==3){
					if(length3 > strLength){
						diff = length3 - strLength;
						for(int i=1; i <= diff; i++){	// diff is difference length of max and original in tripStartTimeStamp
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==4){
					if(length4 > strLength){
						diff = length4 - strLength;	// diff is difference length of max and original in tripEndTimeStamp
						for(int i=1; i <= diff; i++){   
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==7){
					if(length5 > strLength){
						diff = length5 - strLength;	// diff is difference length of max and original in pickUpCensusTrack
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==8){
					if(length6 > strLength){
						diff = length6 - strLength;    // diff is difference length of max and original in dropOffCensusTract
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==16){
					
					if(length7 > strLength){		// diff is difference length of max and original in paymentType
						diff = length7 - strLength;
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==17){
					if(length8 > strLength){
						diff = length8 - strLength;	// diff is difference length of max and original in company
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==20){
					if(length9 > strLength){
						diff = length9 - strLength;    // diff is difference length of max and original in Point1
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}else if(count==23){
					if(length10 > strLength){
						diff = length10 - strLength;	// diff is difference length of max and original in Point2
						for(int i=1; i <= diff; i++){
							s = s + " ";		// Pad spaces
						}
					}
					result.add(s);
				}
			}else{
				if(s.equals("")){
					result.add("-1");			// If there is only comma in integer or double fields, then saved -1
				}else{
					result.add(s);				// if not then save original integer or double
				}
			}
			
			count++;	// count until 23
		}
		// Return the correct record in result ArrayList
		return result;
	}

	/*
	 * dumpLengthObject(RandomAccessFile stream)
	 * This method write binary file from all of the strings length
	 * I will used them in Prog1B to get each of string length 
	 */
	public void dumpLengthObject(RandomAccessFile stream){
		try {
			stream.writeInt(length1);
			stream.writeInt(length2);
			stream.writeInt(length3);
			stream.writeInt(length4);
			stream.writeInt(length5);
			stream.writeInt(length6);
			stream.writeInt(length7);
			stream.writeInt(length8);
			stream.writeInt(length9);
			stream.writeInt(length10);
			stream.writeInt(TOTAL_LENGTH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
	 *  dumpObject(ArrayList<String column, RandomAccessFile stream):
	 *  This method write all the changed data into bin file
	 *  And variables name represent each of column name
	 */
	public void dumpObject(ArrayList<String> column, RandomAccessFile stream){
		
		// Variable represent String column data
		// And get column from ArrayList that I saved all column field.
		StringBuffer tripID = new StringBuffer(column.get(0));  // Make tripID column by StringBuffer
		StringBuffer taxiID = new StringBuffer(column.get(1));  // Make taxiID column by StringBuffer
		StringBuffer tripStartTimeStamp = new StringBuffer(column.get(2));  // Make tripStartTimeStamp column by StringBuffer
		StringBuffer tripEndTimeStamp = new StringBuffer(column.get(3));    // Make tripEndTimeStamp column by StringBuffer
		StringBuffer pickUpCensusTrack = new StringBuffer(column.get(6));   // Make pickUpCensusTract column by StringBuffer
		StringBuffer dropOffCensusTract = new StringBuffer(column.get(7));  // Make dropOffCensusTract column by StringBuffer
		StringBuffer paymentType = new StringBuffer(column.get(15));        // Make paymentType column by StringBuffer
		StringBuffer company = new StringBuffer(column.get(16));	    // Make company column by StringBuffer
		StringBuffer Point1 = new StringBuffer(column.get(19));		    // Make Point1 column by StringBuffer
		StringBuffer Point2 = new StringBuffer(column.get(22));		    // Make Point2 column by StringBuffer
        try {
        	// Write all data into bin file
        	tripID.setLength(length1);
            stream.writeBytes(tripID.toString()); // write tripID into binary file
            
            taxiID.setLength(length2);
            stream.writeBytes(taxiID.toString()); // write taxiID into binary file
            
            tripStartTimeStamp.setLength(length3); // write tripStartTimeStamp into binary file
            stream.writeBytes(tripStartTimeStamp.toString());
            
            tripEndTimeStamp.setLength(length4);  // write tripEndTimeStamp into binary file
            stream.writeBytes(tripEndTimeStamp.toString());
            
            stream.writeInt(Integer.parseInt(column.get(4)));  // write tripSeconds into binary file
            stream.writeDouble(Double.parseDouble(column.get(5))); // write tripMiles into binary file
            
            pickUpCensusTrack.setLength(length5);		// write PickUpCensusTract into binary file
            stream.writeBytes(pickUpCensusTrack.toString());
            
            dropOffCensusTract.setLength(length6);
            stream.writeBytes(dropOffCensusTract.toString());	// write DropoffCensusTract into binary file
            
            stream.writeInt(Integer.parseInt(column.get(8)));   // write Pickup Community Area into binary file
            
            stream.writeInt(Integer.parseInt(column.get(9)));	// write DropOff Community Area into binary file
            
            stream.writeDouble(Double.parseDouble(column.get(10)));	// write Fare into bianry file
            stream.writeDouble(Double.parseDouble(column.get(11)));	// write Tips into binary file
            stream.writeDouble(Double.parseDouble(column.get(12)));	// write Tolls into binary file
            stream.writeDouble(Double.parseDouble(column.get(13)));	// write Extras into binary file
            stream.writeDouble(Double.parseDouble(column.get(14)));	// write Trip Toal binary file
            
            paymentType.setLength(length7);				// write PamentType into binary file
            stream.writeBytes(paymentType.toString());
            
            company.setLength(length8);					// write Company into binary file
            stream.writeBytes(company.toString());
            
            stream.writeDouble(Double.parseDouble(column.get(17)));	// write PickUp Centroid Latitude into binary file
            stream.writeDouble(Double.parseDouble(column.get(18)));	// write DropOff Centroid Latitude into binary file
            
            Point1.setLength(length9);					// write Point1 into binary file
            stream.writeBytes(Point1.toString());
            
            stream.writeDouble(Double.parseDouble(column.get(20)));	// write DropOff Centroid Latitude into binary file
            stream.writeDouble(Double.parseDouble(column.get(21)));	// write DropOff Centroid Longtitude into binary file
            
            Point2.setLength(length10);					// write Point2 into binary file
            stream.writeBytes(Point2.toString());

       } catch (IOException e) {
           System.out.println("I/O ERROR: Couldn't write to the file;\n\t"
                            + "perhaps the file system is full?");
           System.exit(-1);
        }
	}

	/*
	 * main(String args[]):
	 * main handle all the proper things
	 */
	public static void main(String args[]){
		
		// linked (LinkedList) saved all each lines from csv file
		LinkedList<String> linked = new LinkedList<String>();
		// column (ArrayList) saved each of column from csv file
		ArrayList<String> column = new ArrayList<String>();
		File readFile; // read file from csv file
		File writeFile; // write file to bin file
		Prog1A ins = new Prog1A();
		
		/* Create a File object to represent the file and a
         * RandomAccessFile (RAF) object to supply appropriate
         * file access methods.  Note that there is a constructor
         * available for creating RAFs directly (w/o needing a
         * File object first), but having access to File object
         * methods is often handy.
         */
		
		RandomAccessFile dataStream = null;	// Specializes the file I/O
		readFile = new File(args[0]);		// Used to create the readfile (read from command line argument)
		String arg = args[0].replace(".csv",".bin");
		writeFile = new File(arg);  // Used to create the writeFile ( Make binary file that contains binary data)
		if(readFile.length()==0){
			System.out.println(" The file is empty ");
			System.exit(0);
		}
		try {
			dataStream = new RandomAccessFile(writeFile, "rw");	// get RandomAccessfile as dataStream
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Set the max length in each of column string
		ins.setMaxLength(args[0]);
		
		// Write each of string length in binary file
		ins.dumpLengthObject(dataStream);
		/*
		 * Scan (read) each lines from the file and sorted by ascending order.
		 * Pad Strings, saved -1,and etc through made methods, and save those corrected data into ArrayList.
		 * So finally, write all the data into bin file.
		 */
		
		try {
			Scanner scan = new Scanner(readFile);	// read a input file
			scan.nextLine();			// skip the first line of file
			while(scan.hasNextLine()){
				linked.add(scan.nextLine());	// save all the lines into linked list
			}
			Collections.sort(linked);		// sort ascending order from linked list
			
			for(String records : linked){		// Save each lines into record
				column.clear();			// Array list make empty
				//System.out.println(records);
				for(String record : records.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1)){	// print each column seperate by comma except comma in double quotes
					record = record.replace("\"", "");	// remove double quote
					record = record.replace("$", "");	// remove dollar sign
					column.add(record);			// add each of column into ArrayList (only one record)	
				}
				column = ins.setUpRightColumn(column);		// setUpRightColumn set up the pad String, and saved -1, etc and return new arraylist
				
				ins.dumpObject(column, dataStream);		// write each fixed column into binary file
				
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Close the RandomAccessFile
		try {
            dataStream.close();		// close the RandomAccessFile
        } catch (IOException e) {
            System.out.println("VERY STRANGE I/O ERROR: Couldn't close "
                             + "the file!");
        }
		
	}
}
