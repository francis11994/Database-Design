import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

/*
 *  Name: Francis Kim
 *  Course: CSC460
 *  Program: Prog1B.java
 *  Due Date: 02/01/2017 3:30 (beggining of the class)
 *  Assignment: Program 1 (partB)
 *  Instructor and TA: McCann, and Yawen Chen
 *  How to execute Program:
 *  				1. Make binary file from Prog1A.java
 *				2. Use that binary file with command line argument
 *				For example in lecutra
 *				javac Prog1A.java
 *				java Prog1A [filename.csv]  <- create binary file
 *				javac Prog1B.java
 *				java Prog1B [filename.bin]  <- binary file we created through Prog1A
 *
 *  Description: 
 *			There are two different part in this assignment.
 *			First part is reads and print to the screen the Trip Ids and Trip Totals of the first five records of data,
 *			the middle record(or both middle records, if the quantity of records is even), and the last five records of
 *			data in the binary file, plus the total number of records in the binary file.
 *			To design first part, I used seek() method to point out the position, and then I read trip Id and 	trip Totals using readFully() and readDouble().
 *			If there are records less than 5, then it should be also print out first, middle, and last lines of records.
 *			To design second part, I made interpolation method which calculated probe_index, and I kept finding the prefix mathched index.
 *			To convert from Hex to decimal number, I used BigInteger which can saved bigger than long. For example BigInteger a = new BigInteger("abc", 16) converted Hex abc to
 * 			decimal number.
 *			When value of Probe Index is matched with trip ID, then interpolation search is over. And after I found probe index which is matched with trip ID, I keep backward until
 *			the minimum of prefix value which is matched with trip ID, and then I printed out all the prefix trip ID value on the screen. I used startWith() method to check if prefix
 *			is contains in the trip ID.
 *			In addition, I added error cases
 *			1. When prefix is out of range from trip ID
 *			2. When prefix is not matched with trip ID
 *			3. When records have some error
 *			etc.

 *  The programs operational requirements: I used Java language based on eclipse, and I copay and paste my code into lectura. input csv file should be located in current directory, and created
 *					  binary file also located in current directory
 *  Bugs:		There is no bugs in this assignment
 */


/*
   Prog1B: Prog1B have all nessecary method to implement spec required, There is no other classes for this assignment.
*/
public class Prog1B {

	// These all int values saved each of string length and Total bytes from Prog1A. ( There are 10 Strings in records, so I saved all lengths in order)
	private static int length1, length2, length3, length4, length5, length6, length7, length8, length9, length10, TOTAL_LENGTH;
	// numberOfRecords is saved how many lines exist in the file
	private static long numberOfRecords;
	
	
	/*
		public void readLength(RandomAccessFile readBinaryFile)
		This method read each of string length at the first line in binary file, and then saved all the length into length1 to length10 in order
	*/
	public void readLength(RandomAccessFile readBinaryFile){
		
		try {
			length1 = readBinaryFile.readInt();	// Read trip ID length
			length2 = readBinaryFile.readInt();	// Read taxi ID length
			length3 = readBinaryFile.readInt();	// ReadTrip Start Timestamp
			length4 = readBinaryFile.readInt();	// Read Trip End Timestamp
			length5 = readBinaryFile.readInt();	// Read Pickup Census Tract
			length6 = readBinaryFile.readInt();	// Read Dropoff Census Tract
			length7 = readBinaryFile.readInt();	// Read Payment Type
			length8 = readBinaryFile.readInt();	// Read Company
			length9 = readBinaryFile.readInt();	// Read Pickup Centroid Location
			length10 = readBinaryFile.readInt();	// Read Dropoff Centroid  Location
			TOTAL_LENGTH = readBinaryFile.readInt();  // Read Total Length
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*
		public void fetchObjectAndPrint(long numberOfRecordsEvenOrAdd, RandomAccessFile stream)
		This method fecth the trip ID and trip Total objects, and print out in one line per record, and there is space between trip ID, and Trip total
		numberOfRecordsEvenOrAdd = determine number of records is even or odd
	*/
	public void fetchObjectAndPrint(long numberOfRecordsEvenOrAdd, RandomAccessFile stream){
		long numRecords = numberOfRecords;	// numRecords is number of records in file
		long times=0;				// times represent how many times we have to print out tripID , and trip total
		long middle = numRecords/2;		// middle represent middle of records
		byte[] tripID = new byte[length1];	// changed to byte from length1
		long tripTotalByte = length1 + length2 + length3 + length4  + length5 + length6 + 12 + 40;	// tripTotalByte represent location of trip Total
		
		try{
				// print out first five line of tripID and taxiID
				while(numRecords > 0){		// loop until number records bigger than 0
					stream.seek(44+(TOTAL_LENGTH*times));	// find tripID field
					stream.readFully(tripID);
					System.out.print(new String(tripID) + " ");	// print out tripID
					stream.seek(44+(TOTAL_LENGTH*times)+tripTotalByte);	// find trip total field
					System.out.println(stream.readDouble());		// print out trip total
					if(times==4){
						break;					// if we print out 5 of trip ID and trip total, then it should be stopped
					}
					numRecords--;
					times++;
				}

				//print out middle line of tripID and taxiID
				if(numberOfRecordsEvenOrAdd == 0){			// if  number of record is even number, then print out 2 tripID, and trip total
					middle = middle - 1;				// first record in the middle
					stream.seek(44+(TOTAL_LENGTH*middle));		// find tripID
					stream.readFully(tripID);
					System.out.print(new String(tripID) + " ");	// print out trip ID
					stream.seek(44+(TOTAL_LENGTH*middle)+tripTotalByte);	// find trip total
					System.out.println(stream.readDouble());		// print out trip total
					
					// second record in the middle
					stream.seek(44+(TOTAL_LENGTH*(middle+1)));	// find trip ID 
					stream.readFully(tripID);
					System.out.print(new String(tripID) + " ");	// print out trip ID
					stream.seek(44+(TOTAL_LENGTH*(middle+1))+tripTotalByte);	// find trip total
					System.out.println(stream.readDouble());		// print out trip total

				}else if(numberOfRecordsEvenOrAdd == 1){			// if number of records is odd number
					stream.seek(44+(TOTAL_LENGTH*middle));		// find tripID
					stream.readFully(tripID);
					System.out.print(new String(tripID) + " ");	// print out trip ID
					stream.seek(44+(TOTAL_LENGTH*middle)+tripTotalByte);	// find trip total
					System.out.println(stream.readDouble());		// print out trip total
				}

				// print out last five line of tripID and taxiID
				numRecords = numberOfRecords;				// reset numRecords = numberOfRecords
				if(numRecords < 5){					// if number of records is less than 5
					times=0;
					while(numRecords > 0){				// print out until number of records is bigger than 0
						stream.seek(44+(TOTAL_LENGTH*times));	// find tripID
						stream.readFully(tripID);		
						System.out.print(new String(tripID) + " ");	// print out tripID
						stream.seek(44+(TOTAL_LENGTH*times)+tripTotalByte); // find trip total
						System.out.println(stream.readDouble());		// print out trip total
						numRecords--;
						times++;
					}
				}else if(numRecords >= 5){				// if number of records is bigger than 5
					times= numRecords - 5;
					while(times <= numRecords-1){
						stream.seek(44+(TOTAL_LENGTH*times));	// find tripID
						stream.readFully(tripID);
						System.out.print(new String(tripID) + " ");	// print out tripID
						stream.seek(44+(TOTAL_LENGTH*times)+tripTotalByte);	// find trip total
						System.out.println(stream.readDouble());	// print out trip total
						times++;
					}
				}
				
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
		public long calculateProbeIndex(String prefix, String lowIndexHexValue, String highIndexHexValue, long lowIndex, long highIndex)
		this method calculate probe_index, I used BigInteger to convert Hex to Decimal.
		prefix = prefix user input
		lowIndexHexValue = value of low index
		highIndexHexValue = value of high index
		lowIndex = low index
		highIndex = high index
		
		probe_index = low_index + [ target-key(low_index) / (key[high_index] - key[low_index]) ] * (high_index - low_index)
	*/
	public long calculateProbeIndex(String prefix, String lowIndexHexValue, String highIndexHexValue, long lowIndex, long highIndex){
		long probeIndex, subtractHighLow;
		subtractHighLow = highIndex - lowIndex; // high index - low index
		String subtractHighLowString = String.valueOf(subtractHighLow); // high index - low index as String value
		String lowIndexString = String.valueOf(lowIndex); // key[low_index] as String
		String resultString;	// change result to String
	
		BigInteger calulate;	// saved final value in calculate
		
		BigInteger lowIndexInteger = new BigInteger(lowIndexString);	// low index
		BigInteger lowIndexHexValueInteger = new BigInteger(lowIndexHexValue, 16);	// convert String to Integer from Hex value for high index value
		BigInteger highIndexHexValueInteger = new BigInteger(highIndexHexValue, 16);	// convert String to Integer from Hex value for low index value
		BigInteger prefixInteger = new BigInteger(prefix, 16);	// convert String to Integer from Hex value for prefix
		
		BigInteger down = highIndexHexValueInteger.subtract(lowIndexHexValueInteger);	// represent (key[high_index] - key[low_index])
		BigInteger up = prefixInteger.subtract(lowIndexHexValueInteger);			// represent (target - key[low_index])
		BigInteger subtractHighLowInteger = new BigInteger(subtractHighLowString);	// represent (high_index - low_index)
		
		if(down.intValue()==0){						
			return 0;							// if down value is 0, then return 0		
		}
		calulate = up.divide(down);						// [ target-key(low_index) / (key[high_index] - key[low_index]) ]

		calulate = calulate.multiply(subtractHighLowInteger);			// [[ target-key(low_index) / (key[high_index] - key[low_index]) ] * (high_index - low_index)

		
		calulate = calulate.add(lowIndexInteger);				//  low_index + [ target-key(low_index) / (key[high_index] - key[low_index]) ] * (high_index - low_index)

		
		resultString = calulate.toString();					// Convert calculate as String
		
		probeIndex = Long.parseLong(resultString);				// Convert calculate as long
		
		if(probeIndex < 0){
			probeIndex = 0;							// if probeIndex is negative, then return 0
		}
		
		return probeIndex;							// return probe_Index
	}
	
	/*
		compareValue(String prefix, String probeIndexValue)
		This method compare prefix and probe_index value
		prefix = user input prefix
		probeIndexValue = value of probe Index
	*/
	public int compareValue(String prefix, String probeIndexValue){
		
		BigInteger prefixInteger = new BigInteger(prefix, 16);	// convert Hex to Decimal
		BigInteger probeIndexInteger = new BigInteger(probeIndexValue, 16);	// convert Hex to Decimal
		
		if(prefixInteger.compareTo(probeIndexInteger) == 1){	// if prefix is big, then return 1
			return 1;
		}else if(prefixInteger.compareTo(probeIndexInteger) == -1){	// if prefix is less, then return -1
			return -1;
		}else {
			return 0;	// if prefix = value of probe_index, then return 0
		}
	}
	
	/*
		searchMinimumIndexOfValue(long probeIndex1, String prefix, RandomAccessFile stream, String probeIndexKey)
		this method is that if prefix is matched with trip ID, then keep track of which is the minum value of index which contains prefix
		probeIndex1 = index of probe
		prefix = user input prefix
		probeIndexKey = value of probe Index
	*/
	public long searchMinimumIndexOfValue(long probeIndex1, String prefix, RandomAccessFile stream, String probeIndexKey) {
	
		long probeIndex = probeIndex1;	// probeIndex is index of probe
		long original=0;			// if probeIndex is changed from 1 to 0, then saved 1 into original
		try {
				byte[] tripIDByte = new byte[length1];
				while(probeIndexKey.startsWith(prefix) == true && probeIndex != 0){	// prefix should be contain in value of probe_index, and probeIndex should not be 0
					probeIndex--;	// probe_Index reduce by 1
					stream.seek(44+(TOTAL_LENGTH*probeIndex));	// find tripID
					stream.readFully(tripIDByte);
					probeIndexKey = new String(tripIDByte);		// probeIndexKey is trip ID
					if(probeIndex==0){
						original=1;				// if probeIndex is changed from 1 to 0, then saved 1 into original
					}
				}
				
				if(probeIndex != 0){
					probeIndex++;					// if probeIndex is not 0 at the first, then increase 1
				}
				if(original==1){
					probeIndex=1;					// if probeIndex is changed from 1 to 0, then saved 1 into probeIndex
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return probeIndex;
	}
	/*
		printOutTrips(String prefix, long minimumIndexOfValue, String probeIndexValue, RandomAccessFile stream)
		this method print out trip ID, and trip total which contains prefix
		prefix = user input prefix
		minimumIndexOfValue = minimum index which contains prefix
		probeIndexValue = value of probe_index

	*/
	public void printOutTrips(String prefix, long minimumIndexOfValue, String probeIndexValue, RandomAccessFile stream){
		
		try {
			byte[] tripID = new byte[length1];
			long tripTotalByte = length1 + length2 + length3 + length4  + length5 + length6 + 12 + 40;	 	// byte of tripTotal
			double tripTotal;
			
				while(probeIndexValue.startsWith(prefix) == true){
					System.out.print(probeIndexValue + " ");		// print out tripID
					stream.seek(44+(TOTAL_LENGTH*(minimumIndexOfValue))+tripTotalByte);	// find trip total
					tripTotal = stream.readDouble();		// saved trip ID
					System.out.println(tripTotal);			// print out trip ID
					minimumIndexOfValue++;
					if(minimumIndexOfValue < numberOfRecords){	// if minimum index of value is less than number of records
						stream.seek(44+(TOTAL_LENGTH*(minimumIndexOfValue)));	// find trip ID
						stream.readFully(tripID);
						probeIndexValue = new String(tripID);	// saved trip ID
					}else{
						break;		// if minimum index of value is bigger than number of records, then break
					}
				}
				
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
		public void illegalChar(String prefix)
		If there is wrong Hex character, then print error and terminate the program
	*/
	public void illegalChar(String prefix){
		
		for(int a=0; a < prefix.length(); a++){	// loop until prefix length
			if((prefix.charAt(a) < '0' || prefix.charAt(a) >= 'g') || prefix.charAt(a) == ' '){	// if there is bigger than g
				if(prefix.charAt(a) < '0' || prefix.charAt(a) >= 'g'){
				System.out.println("There is illegal character in prefix: " + prefix.charAt(a));	// print out error
				}
				if(prefix.charAt(a) == ' '){
					System.out.println("There is illegal character in prefix: space");
				}
				System.exit(0);		// terminate program
			}
		}
	}
	
	/*
		public static void main(String args[])
		main handle all the method and implement all things
		binary file is accepted as command line argument
	*/
	public static void main(String args[]){
		
		File binaryFile;
		RandomAccessFile readBinaryFile = null;
		Prog1B ins = new Prog1B();		// make instance
		long numberOfRecordsEvenOrAdd = 0;	// decide number of records is even or odd
		binaryFile = new File(args[0]); // read file
		try {
			readBinaryFile = new RandomAccessFile(binaryFile, "rw"); 
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* Move the file pointer (which marks the byte with which
         * the next access will begin) to the front of the
         * file (that is, to byte 0), and read each of string length that we created in Prog1A.
         */
		try {
			readBinaryFile.seek(44);
			if(readBinaryFile.readLine()==null){
				System.out.println("There are No records in binary file (Empty)"); // If binary file is empty, print error, and terminate
				System.exit(0);
			}
			readBinaryFile.seek(0);			// set to 0 position
			ins.readLength(readBinaryFile);		// read all the string length at first line in binary file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Part1: Print first five line, middle, last five line of trip ID, and trip total

		try {
			numberOfRecords = readBinaryFile.length() / TOTAL_LENGTH;	// get number of records in the binary file
			numberOfRecordsEvenOrAdd = numberOfRecords%2;			// decide number of records is even or odd
			System.out.println("First five line, middle, and last five lines of trip ID and total trip: ");
			ins.fetchObjectAndPrint(numberOfRecordsEvenOrAdd, readBinaryFile);	// print first five line, middle, last five line of trip ID, and trip total
			System.out.println(numberOfRecords);				// print out number of records
			System.out.println("");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Part2 : Print out prefix using interpolation search

		System.out.println("Enter the prefix to search the specific trip ID:"); // ask to enter the prefix
		Scanner scan = new Scanner(System.in);	// use scan to user repeadlly input
		String prefix;				// saved prefix
		long probeIndex;			// saved probe index
		long lowIndex=0, highIndex=numberOfRecords;	// first low index set to 0, and high index set to number of records
		
		try {
			String lowIndexHexValue;	// Saved value of low index
			String probeIndexValue, fixedPrefix = null;	// saved value of probe index, and prefix which is add with 0
			String highIndexHexValue;	// Saved value of high index
			boolean matchFalse = false;	// if it is not matched
			long minimumIndexOfValue;
			byte[] tripID = new byte[length1];
			readBinaryFile.seek(44);	// find first tripID in binary file
			readBinaryFile.readFully(tripID);
			lowIndexHexValue = new String(tripID); // saved value of low index

			readBinaryFile.seek(44 + (TOTAL_LENGTH*(numberOfRecords-1)));	// point to very last of trip ID
			readBinaryFile.readFully(tripID);
			highIndexHexValue = new String(tripID);		// saved last of trip ID
			
			int space;
			//Start to ask prefix tripID by loop
			while(scan.hasNext()){
				prefix = scan.nextLine();
				if(prefix.isEmpty()==false){		// if prefix is not empty
						ins.illegalChar(prefix);	// check the error if there is wrong Hex char
						fixedPrefix = prefix;		// fixedPrefix is prefix
						space = length1 - prefix.length(); // 40 - length of prefix
						
						for(int i=1; i <= space; i++){
							fixedPrefix = fixedPrefix + "0";	// attached 0 to make proper value
						}
					
						probeIndex = ins.calculateProbeIndex(fixedPrefix, lowIndexHexValue, highIndexHexValue, lowIndex, highIndex); // Calculate first probe index
						if(probeIndex >= numberOfRecords){	// if probe index is bigger than number of Records
							System.out.println("User input(trip ID) is not in range of Trip ID field");
							matchFalse = true;
						}
						if(matchFalse == false){ // if there is no error
								readBinaryFile.seek(44+(TOTAL_LENGTH*probeIndex));	// find trip ID with probe_index
								readBinaryFile.readFully(tripID);
								probeIndexValue = new String(tripID);			// saved value of probe index
									while(probeIndexValue.startsWith(prefix) == false  && matchFalse == false){	// loop until prefix is not in value of probe index
										
											if(ins.compareValue(fixedPrefix, probeIndexValue) == 1){	// if prefix is bigger then value of probe index
												lowIndex = probeIndex + 1;				// lowIndex increase by 1
												readBinaryFile.seek(44 + (TOTAL_LENGTH * (lowIndex-1)));	// find tripID
												readBinaryFile.readFully(tripID);
												lowIndexHexValue = new String(tripID);			// saved tripID
											}else if(ins.compareValue(fixedPrefix, probeIndexValue) == -1){ // if prefix is less than value of probe index
												highIndex = probeIndex - 1;				// highIndex decrease by 1
												if(highIndex >= 1){					// high index should be bigger than 0
												readBinaryFile.seek(44 + (TOTAL_LENGTH * (highIndex-1)));	// read tripID
												readBinaryFile.readFully(tripID);
												highIndexHexValue = new String(tripID);			// saved trip ID
												}
											}
											
											// These are the error catch
											if(lowIndex > highIndex){ // if prefix is not matched (prefix > value of probe index)
												System.out.println("There is no user input prefix(" + prefix + ") matched with trip ID field");
												matchFalse = true;
												//System.exit(0);
											}else if(numberOfRecords==1 && (lowIndex==1 && highIndex==1)){ // if prefix is not matched when number of records is 1
												System.out.println("There is no user input prefix(" + prefix + ") matched with trip ID field");
												matchFalse = true;
												//System.exit(0);
											}else if(highIndex <= 0){ // if prefix is not matched (prefix < value of probe index
												System.out.println("There is no user input prefix(" + prefix + ") matched with trip ID field");
												matchFalse = true;
												//System.exit(0);
											}
										if(matchFalse==false){
										probeIndex = ins.calculateProbeIndex(fixedPrefix, lowIndexHexValue, highIndexHexValue, lowIndex, highIndex); // calculate the probe Index
										readBinaryFile.seek(44+(TOTAL_LENGTH*(probeIndex)));	// find value of probe index
										readBinaryFile.readFully(tripID);
										probeIndexValue = new String(tripID);			// saved value of probe index
										}
									}
									
								if(matchFalse == false){ // if value of probe index is matched with prefix
									minimumIndexOfValue = ins.searchMinimumIndexOfValue(probeIndex, prefix, readBinaryFile, probeIndexValue); // search minimum index of prefix tripID
									ins.printOutTrips(prefix, minimumIndexOfValue, probeIndexValue, readBinaryFile);	// print out tripID, and trip total which tripID match with prefix
								}
								
								
								// reset all value
								lowIndex = 0;			// set lowIndex to 0
								highIndex = numberOfRecords;	// set high index to number of records
								
								readBinaryFile.seek(44);	// find first tripID in binary file
								readBinaryFile.readFully(tripID);
								lowIndexHexValue = new String(tripID); // saved lowIndexHexValue
				
								readBinaryFile.seek(44 + (TOTAL_LENGTH*(numberOfRecords-1))); // find last tripID in binary file
								readBinaryFile.readFully(tripID);
								highIndexHexValue = new String(tripID);	// saved high index hex value
								
								matchFalse = false;
						}else{ // if there is error
							matchFalse = false;
						}
						
				}
				System.out.println("");
				System.out.println("Enter the prefix to search the specific trip ID:");
			}
			
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Clean-up by closing the file 

        	try {
            	readBinaryFile.close();
        	} catch (IOException e) {
            	System.out.println("VERY STRANGE I/O ERROR: Couldn't close " + "the file!");
		}
		
	}
	
}
