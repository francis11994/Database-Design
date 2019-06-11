/*
 * 	Name: Francis Kim
 *  Course: CSC460
 *  Assignment: Program 2 
 *  Instructor and TA: McCann, Yawen Chen, and Jacob
 *  
 *  Description: Java program that creates a Dynamic Hash index for the
				binary file of taxi trips that we PartA.java program created, and uses it to satisfy a simple type of query. In
				particular, you are to index on the Trip Total field, using the digits in reversed (right to left) order, ignoring
				the decimal points and the dollar signs.
				Finally, we print out trip Id, trip Total, and number of Records.
				Apart from the various prompts to the user, the only displayed output from your program should
				be the query results. After the list of records, display a count of the number of records that match each query
				If you want to execute my program
				1. "javac Prog1A.java"		
				2. "java Prog1A file.csv"		create binary file
				3. "javac Prog2.java"			
				4. "java Prog2 file.bin"		execute Program2 with binary file
				
 *  The programs operational requirements: I used Java language, and input bin file should be located in current directory.
 *  									   
 *  Bugs:		There is no bugs in this assignment
 */


import java.io.*;
import java.util.Scanner;

/* 
 * public class Prgo2 -- this is the main class execute all the things that operate functions and print out result
 */
public class Prog2 {

	// length1 to Total_LENGTH is each filed of length from string
	private static int length1, length2, length3, length4, length5, length6, length7, length8, length9, length10, TOTAL_LENGTH;
	// Node t is represent target Node
	public static Node t;
	// if num will be used when we print out result
	public static int num=0, count = 0;
	// set up Node head is null at first
	public static Node head = null;
	// position is increased by 6500
	public static long position=6500;
	// position is increased by 6500
	public static long p = 6500;
	// numberOfRecords is represent number of records in binary file
	public static long numberOfRecords;

	/*
	 * public void setInitialHashBucket() -- this function is set up the first initial hash bucket tree node 
	 */
	
	public void setInitialHashBucket(){
		int nodeNumber = 0;				// node Number represent each digit number 0 to 9
		while( nodeNumber <= 10){		// loop until nodeNumber = 11
			if(nodeNumber == 0){
				head = new Node(nodeNumber, position, 0, position);	// set up the head 
				head.child = null;									// child of head is null
			}else{
				Node newNode = new Node(nodeNumber, position, 0, position);		// create new node to connect big node
				if(nodeNumber == 1){
					head.next = newNode;							// next of head is new node
					newNode.next = null;							// next of new node is null
					newNode.child = null;							// child of new node is null
				}else{
					Node tract = head;								// tract is point to head
					while(tract.next != null){						// go until tract next is null
						tract = tract.next;	
					}
					tract.next = newNode;							// tract next is new node we created
					newNode.next = null;							// new node next is null
					newNode.child = null;							// new node child is null
				}
			}
			
			nodeNumber++;											// increase digit
			position = position + 6500*16;							// Each hash bucket is 6500 lines and 16 represent byte of double + long
		}
	}
	
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
	 * public void insertIndexIntoHashBucket(String tripTotalString, double tripTotal, long numRecords, RandomAccessFile write)
	 * This function insert trip total and line number into node, and write into HashBucket binary file
	 * this function is important in this program
	 */
	public void insertIndexIntoHashBucket(String tripTotalString, double tripTotal, long numRecords, RandomAccessFile write){
		int length = tripTotalString.length(); // length of tripTotal from string
		Node target = head;						// target point to head
		boolean finish=false;					// finish decide wheather this function can be finished it or not
		int level = 2;							// level represent level of hash bucket tree
		// loop until trip total string is end
		for(int i = length-1; i >= 0; i--){
			
			while(target != null){	// find a digit match from trip total string
				int digit = Character.getNumericValue(tripTotalString.charAt(i));	// this is each digit of trip total
					if(target.getDigit() == digit){									// if trip total digit is matched with b tree digit of node
							if(target.getCount() < 6500){								// if hash bucket file is less than 6500
								if(tripTotal!=-1){
									write(target, tripTotal, numRecords, write);	// write trip total and number of records into binary file
								}else{
									finish=true;									// if trip total field is empty then break
									break;
								}
								finish=true;										// finish for loop
							}
							if(target.child == null && target.getCount() == 6500){		// if target child is null and hash bucket file is over 6500 then
								makeLeafNode(target);	// make leaf node			// make new leaf node
								distribute(target, level, numRecords, write);		// copy all the original node to new leaf node (distribute)			
								finish = true;										// finish for loop
								break;												// break
							}else if(target.child != null && target.getCount() == 6500){	// if child is not null, and hash bucket file is over 6500 then
								target = target.child;								// target is child of original target
								if(i==0){											// if digit is 0
									while(target != null){						
										if(target.getDigit()==10){					// then write trip total in digit 10 in node
											write(target, tripTotal, numRecords, write);	// write trip total and number of records in binary file
										}
										target = target.next;						// target is next of target
									}
									finish = true;									// finish for loop
								}
								
								level++;											// level of b tree is increased by 1
								break;												// break
							}
					}
				target = target.next;	// target is next of target
			}
			if(finish==true){
				break;			// if we write into binary file, then break
			}
		}
	}
	
	/*
	 * public void distribute(Node target,int level, long numRecords, RandomAccessFile write)
	 * 
	 * this function is distribute all the original data into new leaf node
	 */
	
	public void distribute(Node target,int level, long numRecords, RandomAccessFile write){
		double totalTripCopy;	// it is trip total
		long numRecord;			// it is represent number of records
		int digitPlace;			// next digit of number from current digit (possible to get error)
		long pos = target.getPosition();	// pos is position of current status
		long originalPos = target.getOriginalPosition();	// it is original position without increasing
		Node targetChild = null;	// it is child of target
		
		try{
			while(originalPos < pos){			// loop until hash bucket is went through all data
				targetChild = target.child;		// it is child of target
				write.seek(originalPos);		// it is point to position of each line
				totalTripCopy = write.readDouble();	// read trip total
				numRecord = write.readLong();		// read number of records (line number)

				String changeToString = Double.toString(totalTripCopy).replace(".", "");	// change trip total into string without decimal point
				digitPlace = changeToString.length() - level;		// it is length of string - level of b tree
				
				while(targetChild != null){		// loop until child of target is null
					if(digitPlace == -1){		// if digit place is -1 then save into digit 10 in node
						if(targetChild.getDigit()==10){
							write(targetChild, totalTripCopy, numRecord, write);	// write them into binary file
							break;	// break
						}
					}else if(digitPlace != -1){		// if digit place is not -1, then save into matched digit in node
						if(targetChild.getDigit() == Character.getNumericValue(changeToString.charAt(digitPlace))){	// if digit place is not -1, then save into matched digit in node
							write(targetChild, totalTripCopy, numRecord, write);	// write into binary file
							break;	// break
						}
					}
					targetChild = targetChild.next;	// target child is next of target child
				}
				
				originalPos = originalPos + 16;		// hash bucket line is increased by 16 ( byte of double + byte of long)
			}
		}catch (IOException e) {				// catch the error
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	/*
	 * makeLeafNode(Node target)
	 * this function create new leaf node to distribute all the data
	 */
	public void makeLeafNode(Node target){
		Node leafHead = null;		// first leaf head is null
		int nodeNumber = 0;			// node number is digit number

		while( nodeNumber <= 10){		// loop to digit 10
			if(nodeNumber == 0){		// create first head node in leaf node
				leafHead = new Node(nodeNumber, position, 0, position);		// create head node
				leafHead.next = null;		// set to head node next is null
				leafHead.child = null;		// set to head chils is null
				target.child = leafHead; 	// connect target child with leaf head
			}else{
				Node newNode = new Node(nodeNumber, position, 0, position); // create new node
				if(nodeNumber == 1){		// create second node
					leafHead.next = newNode;	// connect head node with new node
					newNode.next = null;		// next of new node is null
					newNode.child = null;		// child of new node is null
				}else{
					Node tract = leafHead;		// tract point to head node in new leaf
					while(tract.next != null){	// loop until tract is null
						tract = tract.next;
					}
					tract.next = newNode;		// connect new node at the end
					newNode.next = null;			// new node next is null
					newNode.child = null;			// new node child is null
				}
			}
			
			nodeNumber++;						// increase number of digit
			position = position + 6500*16;		// Each hash bucket is 6500 lines and 16 represent byte of double + long
		}
	}
	/*
	 * write(Node target, double tripTotal, long numRecords, RandomAccessFile write)
	 * write trip total and number of records data into binary file
	 */
	public void write(Node target, double tripTotal, long numRecords, RandomAccessFile write){
		try {
			write.seek(target.getPosition());	// Find a correct position
			write.writeDouble(tripTotal); // size = 8, write trip total
			write.writeLong(numRecords);  // size = 4, write number of records
			target.addByte(); 			  // add 16 byte
			target.addCount();			  // add 1 from number of line
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/*
	 * public Node findTarget(String prefix, int lengthOfPrefix, RandomAccessFile write)
	 * this is find a target in the hash bucket tree
	 * Follow the each digit number from b tree and make a target
	 */
	public Node findTarget(String prefix, int lengthOfPrefix, RandomAccessFile write){

		Node target = head; // target point to head
		long originalPos;	// this is original position of node 
		long pos;			// this is position of node (increase)
		double checkNum;	//it is save trip total
		String checkNumtoString;	// change to string from trip total
		for(int a=lengthOfPrefix-1; a >= 0; a--){	// loop until length of digit is 0
			int digit = Character.getNumericValue(prefix.charAt(a));	// digit is each digit character number
			while(target != null){			// loop until target is null
				if(target.getDigit() == digit){		// if target is matched with digit number from node
					if(target.child == null){		// if child of target is null then
						try{
							originalPos = target.getOriginalPosition();	// save original position
							pos = target.getPosition();					// save increased position
							while(originalPos < pos){					// loop until hash bucket file is finish
								write.seek(originalPos);				// get position
								checkNum = write.readDouble();			// read trip total
								checkNumtoString = Double.toString(checkNum).replace(".", "");	// change to string from trip total
								if(checkNumtoString.endsWith(prefix) == true){		// if end of string is matched with prefix
									return target;			// then return that target
								}	
								originalPos = originalPos + 16;	// increased by 16 (long + double)
							}
							target = null;					// target is null (if there is no match)
							return target;					// return target
						}catch(IOException e){
							e.printStackTrace();
						}
					}else if(target.child != null){			// if target has child
						if(a==0){							// if length is 0 from string trip total
							return target;					// then return target
						}
						target = target.child;				// if not move to child of target
						break;								// break
					}
				}
				target = target.next;						// move target to next target
			}
		}

		return target;
	}
	/*
	 * public boolean illegalChar(String prefix)
	 * this function check the proper input value, if there is any weird character then return false, if not then return true
	 */
	public boolean illegalChar(String prefix){
		for(int a=0; a < prefix.length(); a++){	// loop until prefix length
			if( prefix.charAt(a) >= '0' && prefix.charAt(a) <= '9'){	// if there is bigger than g
			}else{
				if(prefix.charAt(a) == ' '){
					System.out.println("There is illegal character in prefix: space");	// print out error
				}else{
					System.out.println("There is illegal character in prefix: " + prefix.charAt(a));	// print out error
				}
				return false; // and return false
			}
		}
		return true;	// if there is no error return true
	}

	/*
	 * public void printTripIDTripTotal(Node findTarget, String prefix, RandomAccessFile write, RandomAccessFile read)
	 * print the all the prefix matched from hash bucket b tree and hash bucket file
	 */
	public void printTripIDTripTotal(Node findTarget, String prefix, int leadingzero, RandomAccessFile write, RandomAccessFile read){
		
		long originalPos = findTarget.getOriginalPosition();	// get original position of target
		long pos = findTarget.getPosition();					// get position of target (increased)
		double tripTotal;										// it is trip total
		long numRecords;										// it is number of records
		String tripid;											// it is trip id by string
		byte[] tripID = new byte[length1];						// it will trip id
		try{
				if(findTarget.child == null){					// if target has no child
					if(num==0){									// if it is first time executing of program
						// print out trip id and trip total
						while(originalPos < pos){				// loop until end of hash bucket file
						write.seek(originalPos);				// find a position
						tripTotal = write.readDouble();			// read a trip total
						numRecords = write.readLong();			// read number of records
						String tripTotalString = Double.toString(tripTotal).replace(".", "");	// save trip total as string without decimal point

						read.seek(44 + (TOTAL_LENGTH*(numRecords)));		// find a matched trip total from chichgo taxi binary file
						read.readFully(tripID);								// and read trip id
						tripid = new String(tripID);						// save trip id
						if(tripTotalString.endsWith(prefix) == true){		// if it is matched with end of string
							if(leadingzero==0 || leadingzero==2){
								count++;
								System.out.println(tripid + " " + tripTotal);		// print out those trip id and trip total
							}else if(leadingzero==1){
								
								if(tripTotalString.compareTo(prefix) == 0){
									count++;
									System.out.println(tripid + " " + tripTotal);		// print out those trip id and trip total
								}
							}
						}
						originalPos = originalPos + 16;						// increased by 16 byte (double + long)
						}
						num++;	// increase num
					}else if(num > 0){							// if it is not first time
						// print out trip id and trip total
						while(originalPos < pos){				// loop until end of hash bucket file
							write.seek(originalPos);			// find a position
							tripTotal = write.readDouble();		// read a trip total
							numRecords = write.readLong();		// read number of records
							String tripTotalString = Double.toString(tripTotal).replace(".", "");	// save trip total as string without decimal point

							read.seek(44 + (TOTAL_LENGTH*(numRecords)));	// find a matched trip total from chichgo taxi binary file
							read.readFully(tripID);				// and read trip id
							tripid = new String(tripID);		// save trip id
								if(leadingzero==0 || leadingzero==2){
									count++;
									System.out.println(tripid + " " + tripTotal);		// print out those trip id and trip total
								}else if(leadingzero==1){
									
									if(tripTotalString.compareTo(prefix) == 0){
										count++;
										System.out.println(tripid + " " + tripTotal);		// print out those trip id and trip total
									}
								}
							originalPos = originalPos + 16;		// increased by 16 byte (double + long)
						}	
						Node node = findTarget.next;			// move to next node
						if(node != null){						// do not write null node
							printTripIDTripTotal(node, prefix, leadingzero, write, read); // recursive to print out all data
						}
						num++;	// increase num
					}
				}else if(findTarget.child != null){				// if there is child from find target
					num++;										// increase num
					Node node = findTarget.child;				// node is child of target
					printTripIDTripTotal(node, prefix, leadingzero, write, read);	// recursive to search and print out data
					Node nextNode = findTarget.next;			// go to next node
					if(findTarget != t){						// if final node is not matched with original target node
						if(nextNode != null){
						printTripIDTripTotal(nextNode, prefix, leadingzero, write, read);	// recursive to search and print out data
						}
					}
				}
				num++;	// increase num
			
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	/*
	 * leadingZero(String prefix)
	 * this functions is remove zero and help to print out proper output (ex, 000999 999, 000777 77)
	 *
	*/
	public int leadingZero(String prefix){
		int zero=0;
		for(int i=0; i <= prefix.length()-1; i++){	// loop until length of prefix is 0
			if(prefix.charAt(i) == '0'){		// if there is match with 0
				zero++;				// increase zero integer
			}
		}
		
		if(zero == prefix.length()){			// if number of zero and length of prefix is same
			return 2;				// return 2
		}else{
			String removeZero = prefix.replaceFirst("^0+(?!$)", "");	// remove leading zero
			if(removeZero.length() < prefix.length()){
				return 0;						// if original length is bigger than remove leading zero string
			}else if(removeZero.length() == prefix.length()){
				return 2;						// if length is same
			}
		}
		return 3;
	}
	/*
	 * public static void main(String args[])
	 * this function is execute all the functions and print out correct data
	 */
	public static void main(String args[]){
		
		Prog2 ins = new Prog2();	// instance
		long tripTotalPosition; // position of Trip Total
		long numRecords;		// number of records
		File readInput = new File(args[0]);		// read the file input.bin
		File writeOutput = new File("HashBucketFile.bin");		// make the file HashBucketFile.bin
		String tripTotalString;		// string of trip total
		RandomAccessFile read = null, write = null;	// to read and write into file
		long lineNumber=0;	// number of line
		double tripTotal;	// trip total
		try {
			read = new RandomAccessFile(readInput, "rw"); 	// open the file input.bin
			write = new RandomAccessFile(writeOutput, "rw"); // open the file HashBucketFile.bin
		
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		
		// write into Hash bucket binary file for each data
		try {
			read.seek(44);		// point to position after reading all the length
			if(read.readLine()==null){	// if there is empty binary file
				System.out.println("There are No records in binary file (Empty)"); // If binary file is empty, print error, and terminate
				System.exit(0);
			}
			read.seek(0);		// point to first position of binary file
			ins.readLength(read);	// read all length from length1 to TOTAL_LENGTH
			ins.setInitialHashBucket();		// set initial node of tree
			numberOfRecords = read.length() / TOTAL_LENGTH;	// calculate the length of chichago binary file
			numRecords = numberOfRecords;	// number of records
			tripTotalPosition = length1 + length2 + length3 + length4  + length5 + length6 + 12 + 40;	// it is position of trip total in binary file
			// make the structure
			while(numRecords > 0){		// loop number of records to 0
				read.seek(44+(TOTAL_LENGTH*lineNumber)+tripTotalPosition);	// point each line of trip total position
				tripTotal = read.readDouble();	// read trip total
				tripTotalString = Double.toString(tripTotal).replace(".", "");	// change trip total as string
				ins.insertIndexIntoHashBucket(tripTotalString, tripTotal, lineNumber, write);	// insert into hash bucket file and tree
				lineNumber++;		// incrase number of line
				numRecords--;		// decrase number of records
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		Scanner scan = new Scanner(System.in);		// user input
		String prefix;		// it is prefix of user input
		Node findTarget = null;	// target node
		int lengthOfPrefix;	// length of prefix
		System.out.println("Please input prefix number: ");	// ask user input
		while(scan.hasNext()){	// user can type until end
			prefix = scan.nextLine();	// type prefix
			if(prefix.isEmpty()==false){
			int leadingzero = ins.leadingZero(prefix);	
			int error = 0;		// catch error using number
			count = 0;
			while(ins.illegalChar(prefix) == true && leadingzero<=2){	// if prefix has illegal character
				if(leadingzero == 2 || leadingzero == 0){
					lengthOfPrefix = prefix.length();	// get length of prefix
					findTarget = ins.findTarget(prefix, lengthOfPrefix, write);	// find a target 
					t = findTarget;	// t is the target
				}else if(leadingzero == 1){
					prefix = prefix.replaceFirst("^0+(?!$)", "");
					lengthOfPrefix = prefix.length();	// get length of prefix
					findTarget = ins.findTarget(prefix, lengthOfPrefix, write);	// find a target 
					t = findTarget;	// t is the target
				}
				if(findTarget != null){	// loop until target is null
					
					ins.printTripIDTripTotal(findTarget, prefix, leadingzero, write, read);	// print trip id and trip total
					if(leadingzero == 1 || leadingzero == 2){
					System.out.println(count);					// print number of records
					}
					num = 0;				// initialize num = 0
				}else{
					if(leadingzero == 0 || leadingzero == 1){	// if leading zero is 0 or 1
						error++;				// increase error integer
					}else if(leadingzero == 2){			// if leading zero is 2
						error = 5;				// error = 5
					}
					if(error==2 || error == 5){		// print out the error
						System.out.println("There is no prefix match");	// if there is no prefix match, then print out error
					}
					
				}
				if(leadingzero == 1){	// if leadingzero integer is equal to 1
					break;		// break the loop
				}
				leadingzero++;		// increase leading zero
			}
				System.out.println("Please input prefix number: ");	// ask user input
			}
			
		}
	}
}
