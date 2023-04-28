/*
 * File: FindRange.java
 * Name: Mitchell So
 * Section Leader: Ashley Taylor
 * --------------------
 /*prompts integer values until you enter the sentinel then shows the minimum and maximum
 * values of the range entered. If the only value entered is the sentinel
 * a message will display that says no values have been entered and once
 * again prompt for integer values
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {

	/** value that stops the reading of integers unless none have been entered */
	private static final int SENTINEL = 0;

	public void run() {
		println("This program finds the largest and smallest numbers.");
		int n = readInt("?");
		while (n == SENTINEL){
			println("No values have been entered");
			n = readInt("?");
		}
		int max = n;
		int min = n; 
		while(n !=SENTINEL){
			if(n > max){
				max = n;
			}
			if(n < min){
				min =n;
			}
			n = readInt("?");
		}
		println("smallest:" + min);
		println("largest:" + max);
	}
}

