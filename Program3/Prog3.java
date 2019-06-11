/*
 *	Name: Francis Kim
 *
 *	Course: CSC460
 *
 *	Assignment: Program3
 *
 *	Instructor and TA: McCann, Yawen Chen, and Jacob
 *
 *	Due date: 04/05/2017 beggining of class
 *
 *
 *	Description: 
 *		     The purpose of this program is to achieve using sql with Java language.
 *		     I created 5 sql files, and it makes data table of Arizona Instrument to Measure Standards from 2010 to 2014. 
 *		     
 *		     We can search any data from the table using sql command in java, and print out those results on the screen.
 *			
 *	How to execute:
 *		     1. turn on the oracle data server (sqlpl username@oracle.aloe)
 * 		     2. execute the sql files (@ AIMS_AND_AIMSA_2010.sql ~ @ AIMS_AND_AIMSA_2014.sql)
 *		     3. make Prog3 program (javac Prog3.java -> java Prog3 username password)
 *		     4. Check the results
 *
 *	Other requirements:
 *		     Add the Oracle JDBC driver to your CLASSPATH environment variable:
 *		     export CLASSPATH=/opt/oracle/product/10.2.0/client/jdbc/lib/ojdbc14.jar:${CLASSPATH}
 *
 *	Bug:
 *		     There is no bugs
*/

import java.io.*;
import java.sql.*;
import java.util.Scanner;

public class Prog3{

	/*
	*	queryA(Connection dbconn)
	*	Received Connection dbconn as parameter
	*	Print amounts of high schools in each years
	*/
	public void queryA(Connection dbconn){

		// Set up the statement and resultset
		Statement stmt = null;
        	ResultSet answer = null;

		// Set the query as String
		String query;

		try{		
		// Loop until 2010 to 2014
		for(int year=2010; year <= 2014; year++){
			// out test query
			query = "select count(SNAME) as count from AIM_AND_AIMSA_" + year +" where SNAME like '%High School%' and SNAME not like '%Junior%'";
			
			// Send the query to the DBMS, and get and display the results
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if(answer != null){

				// set ResultSetMetaData with Resultset
				ResultSetMetaData answermetadata = answer.getMetaData();
				
				// loop until query is end and print out the value of column
				while(answer.next()){
					System.out.print("Hom many high schools are listed in the " + year + "? ");	// print out the question
					System.out.println(answer.getInt("count"));	// print out number of high schools
				}

			}
		}
			// close the statement
			stmt.close();
			// catch error cases
		}catch (SQLException e) {
                	System.err.println("*** SQLException:  "
                    	+ "Could not fetch query results.");
                	System.err.println("\tMessage:   " + e.getMessage());
                	System.err.println("\tSQLState:  " + e.getSQLState());
                	System.err.println("\tErrorCode: " + e.getErrorCode());
                	System.exit(-1);
        	}
	}

	/*
	*	queryB(Connection dbconn)
	*	Received Connection dbconn as parameter
	*	For each of the five years, display the number of charter schools and how many of them had a
	*	sum of the Math percentages Falls Far Below and Approaches that was less than the percent
	*	Passing
	*/

	public void queryB(Connection dbconn){
		
		// Set up the statement and resultset
		Statement stmt1 = null;
        	ResultSet answer1 = null;

		// Set up the statement and resultset
		Statement stmt2 = null;
		ResultSet answer2 = null;

		// set up query 1 and query 2
		String query1;
		String query2;

		try{
			// loop until from 2010 to 2014
			for(int year=2010; year <= 2014; year++){
				
				// our test query
				query1 = "select count(CHARS) as charsNum1 from AIM_AND_AIMSA_" + year + " where CHARS = 'Y'";	// query command that print number of charter schools
				
				// Send the query to the DBMS, and get and display the results
				stmt1 = dbconn.createStatement();
				answer1 = stmt1.executeQuery(query1);
				query2 = "select count(SNAME) as charsNum2 from AIM_AND_AIMSA_" + year + " where CHARS = 'Y' and MathPFFB+MathPA < MathPP"; // query command that print second question
				
				// Send the query to the DBMS, and get and display the results
				stmt2 = dbconn.createStatement();
				answer2 = stmt2.executeQuery(query2);
				
				if(answer1 != null && answer2 != null){
					// loop until query 1 is end
					while(answer1.next()){
						System.out.print("The number of charter schools for " + year + ": ");	// ask question
						System.out.println(answer1.getInt("charsNum1"));	// print out number of chars
						// loop until query 2 is end
						while(answer2.next()){
							System.out.print("The number of Charter Schools where the sum of math percentages falls far below and approaches is less than the percentage of passing " + year + ": "); // ask question
							System.out.println(answer2.getInt("charsNum2"));	// print out how many of them had a
														// sum of the Math percentages Falls Far Below and Approaches that was less than the percent Passing
						}
						
					}
	
				}

			}
			// close statement
			stmt1.close();
			stmt2.close();
			// catch error cases
		}catch (SQLException e) {
                	System.err.println("*** SQLException:  "
                    	+ "Could not fetch query results.");
                	System.err.println("\tMessage:   " + e.getMessage());
                	System.err.println("\tSQLState:  " + e.getSQLState());
                	System.err.println("\tErrorCode: " + e.getErrorCode());
                	System.exit(-1);
        	}

	}

	/*
	*	queryC(Connection dbconn)
	*	Received Connection dbconn as parameter
	*	For each county in 2014, which 10 schools had the greatest differences between the Passing
	*	percentages in Reading and Writing? Display one table for each county that includes position number
	*	(1 for the school with the biggest difference, and duplicate position numbers for ties) ten schools in decending order
	*
	*/

	public void queryC(Connection dbconn){

		// Set up statement and resultSet
		Statement stmt1 = null;
        	ResultSet answer1 = null;

		// Set up statement and resultSet
		Statement stmt2 = null;
		ResultSet answer2 = null;
	
		// Set up each query
		String query1;	// print each county
		String query2;	// print each table
		int count=0;
		try{

			// our test first query
			query1 = "select COUNTY from AIM_AND_AIMSA_2014 group by COUNTY";
			// Send the query to the DBMS, and get and display the results
			stmt1 = dbconn.createStatement();
			answer1 = stmt1.executeQuery(query1);
			if(answer1 != null){
				
				// loop until first query is end
				while(answer1.next()){
					System.out.println(answer1.getString("COUNTY"));	// print name of county
					System.out.println("-----------------------");
					System.out.println();
					
					// get table which contain pos, school name, reading passing percentage, writing passing percentage, absolute value of ReadingPP - WritingPP															   		
					query2 = "select rank() over(order by ABS(ReadingPP-WritingPP) DESC) as POS, SNAME, ReadingPP,WritingPP,ABS(ReadingPP-WritingPP) as diff from AIM_AND_AIMSA_2014 where WritingPP is not null and ReadingPP is not null and COUNTY='" + answer1.getString("COUNTY") + "'";

					// Send the query to the DBMS, and get and display the results
					stmt2 = dbconn.createStatement();
					answer2 = stmt2.executeQuery(query2);
					if(answer2 != null){
						// Print out the column
						System.out.println("POS School Name									  ReadingPP		   WritingPP	      |Diff|");
						
						// loop until query 2 is end
						while(answer2.next()){
							/* 
							 *	I used severy else if statment because to display the beautiful outline of table
							 */
							if(count==10){
								break; // break when count 10
							}else if(count==9 && answer2.getInt("POS")==10){
								System.out.println(answer2.getInt("POS") + "   "+ answer2.getString("SNAME") + answer2.getInt("ReadingPP") + "		             " + answer2.getInt("WritingPP") + "		        " + answer2.getInt("diff"));
							}else if(count==9 && answer2.getInt("POS") != 10){
								System.out.println(answer2.getInt("POS") + "   "+ answer2.getString("SNAME") + " " + answer2.getInt("ReadingPP") + "		       	       " + answer2.getInt("WritingPP") + "		" + answer2.getInt("diff"));
							}else{
								System.out.println(answer2.getInt("POS") + "   "+ answer2.getString("SNAME") + " " + answer2.getInt("ReadingPP") + "		               " + answer2.getInt("WritingPP") + "		" + answer2.getInt("diff"));
							}
							count++;	// count = count + 1
						}
						
					}
					System.out.println();	// print out new line
					count=0;		// set equal to 0 to initialize
				}
				// close the statements
				stmt1.close();
				stmt2.close();
			}
			// catch the error cases
		}catch (SQLException e) {
                	System.err.println("*** SQLException:  "
                    	+ "Could not fetch query results.");
                	System.err.println("\tMessage:   " + e.getMessage());
                	System.err.println("\tSQLState:  " + e.getSQLState());
                	System.err.println("\tErrorCode: " + e.getErrorCode());
                	System.exit(-1);
        	}

	}

	/*
	*	queryD(Connection dbconn)
	*	Received Connection dbconn as parameter
	*	Print out which is the highest Math and Science school that maintain top 50 from 2010 to 2014
	*	I used sum of math and science mean scale score
	*/

	public void queryD(Connection dbconn){
		
		// set up statement and resultset
		Statement stmt = null;
        	ResultSet answer = null;
		Scanner scan = new Scanner(System.in);
		int select;
		int top=0;
		// set up query
		String query;
		
		System.out.println("User can choose information of top25, top50, top75, top100 school that maintain highest sum of Math and Science score from 2010 to 2014"); // query4 question
		System.out.println("1. top 25 school"); // option 1
		System.out.println("2. top 50 school"); // option 2
		System.out.println("3. top 75 school"); // option 3
		System.out.println("4. top 100 school"); // option 4
		System.out.println();
		System.out.println("Please select the options (1 to 4)");
		select = scan.nextInt();
		if(select == 1){
			top = 25;		// top 25
		}else if(select == 2){
			top = 50;		// top 50
		}else if(select == 3){
			top = 75;		// top 75
		}else if(select == 4){
			top = 100;		// top 100
		}else{
			System.out.println("There is no selected option");
			System.exit(1);
		}
		System.out.println("School Name");
		System.out.println("---------------------------------------------------------------------");
		try{
			/*

				Test our query
				I get ranking of highest sum of math and science score from 2010 to 2014 years in top 50
				and I intersect five results to get common school

			*/
			query  = "(select SNAME from (select rank() over(order by (MathMSS+ScienceMSS) desc) as rank, SNAME from AIM_AND_AIMSA_2010 where MathMSS is not null and ScienceMSS is not null) where rank <" + top + ") intersect " +
				 "(select SNAME from (select rank() over(order by (MathMSS+ScienceMSS) desc) as rank, SNAME from AIM_AND_AIMSA_2011 where MathMSS is not null and ScienceMSS is not null) where rank <" + top + ") intersect " +
				 "(select SNAME from (select rank() over(order by (MathMSS+ScienceMSS) desc) as rank, SNAME from AIM_AND_AIMSA_2012 where MathMSS is not null and ScienceMSS is not null) where rank <" + top + ") intersect " +
				 "(select SNAME from (select rank() over(order by (MathMSS+ScienceMSS) desc) as rank, SNAME from AIM_AND_AIMSA_2013 where MathMSS is not null and ScienceMSS is not null) where rank <" + top + ") intersect " +
				 "(select SNAME from (select rank() over(order by (MathMSS+ScienceMSS) desc) as rank, SNAME from AIM_AND_AIMSA_2014 where MathMSS is not null and ScienceMSS is not null) where rank <" + top + ")";
			
			// Send the query to the DBMS, and get and display the results
			stmt = dbconn.createStatement();
			answer = stmt.executeQuery(query);

			if(answer != null){
				// loop until query end
				while(answer.next()){	
					System.out.println(answer.getString("SNAME"));	// print out name of school
				}
			}
			stmt.close();
		}catch (SQLException e) {
                	System.err.println("*** SQLException:  "
                    	+ "Could not fetch query results.");
                	System.err.println("\tMessage:   " + e.getMessage());
                	System.err.println("\tSQLState:  " + e.getSQLState());
                	System.err.println("\tErrorCode: " + e.getErrorCode());
                	System.exit(-1);
        	}

	}

	/*
	*
	*	public static void main(String[] args)
	*
	*	Connect the dbms server with java, and print out all the result of queries from 1 to 5
	*
	*/
	public static void main(String[] args){
		final String oracleURL =   // Magic lectura -> aloe access spell
                        "jdbc:oracle:thin:@aloe.cs.arizona.edu:1521:oracle";

	        String username = null,    // Oracle DBMS username
        	       password = null;    // Oracle DBMS password


       	 	if (args.length == 2) {    // get username/password from cmd line args
       	     		username = args[0];
       	     		password = args[1];
      	  	} else {
            		System.out.println("\nUsage:  java JDBC <username> <password>\n"
                             + "    where <username> is your Oracle DBMS"
                             + " username,\n    and <password> is your Oracle"
                             + " password (not your system password).\n");
            		System.exit(-1);
        	}


            // load the (Oracle) JDBC driver by initializing its base
            // class, 'oracle.jdbc.OracleDriver'.

        	try {

                	Class.forName("oracle.jdbc.OracleDriver");

        	} catch (ClassNotFoundException e) {
                	System.err.println("*** ClassNotFoundException:  "
                    	+ "Error loading Oracle JDBC driver.  \n"
                    	+ "\tPerhaps the driver is not on the Classpath?");
                	System.exit(-1);
        	}


            // make and return a database connection to the user's
            // Oracle database

        	Connection dbconn = null;

        	try {
                	dbconn = DriverManager.getConnection
                               		(oracleURL,username,password);

        	} catch (SQLException e) {
                	System.err.println("*** SQLException:  "
                    	+ "Could not open JDBC connection.");
                	System.err.println("\tMessage:   " + e.getMessage());
                	System.err.println("\tSQLState:  " + e.getSQLState());
                	System.err.println("\tErrorCode: " + e.getErrorCode());
                	System.exit(-1);
        	}
		// instanciate
		Prog3 inst = new Prog3();		
		// print out query 1
		System.out.println("Result of query#1:");
		System.out.println();
		inst.queryA(dbconn);
		System.out.println();
		// print out query 2
		System.out.println("Result of query#2:");
		System.out.println();
	 	inst.queryB(dbconn);
		System.out.println();
		// print out query 3
		System.out.println("Result of query#3:");
		System.out.println();
	 	inst.queryC(dbconn);
		System.out.println();
		// print out query 4
		System.out.println("Result of query#4:");
		System.out.println();
	 	inst.queryD(dbconn);
			
		// Close the connection
        	try{
			dbconn.close();
		}catch (SQLException e) {
                	System.err.println("*** SQLException:  "
                    	+ "Could not fetch query results.");
                	System.err.println("\tMessage:   " + e.getMessage());
                	System.err.println("\tSQLState:  " + e.getSQLState());
                	System.err.println("\tErrorCode: " + e.getErrorCode());
                	System.exit(-1);
        	}
	}
}
