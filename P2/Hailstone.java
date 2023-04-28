/*
 * File: Hailstone.java
 * Name: Mitchell So	
 * Section Leader: Ashley Taylor
 * --------------------
/*prompts for a random integer then if even divides by 2 if odd 
 * computes 3n+1 until the number reaches 1 
 * then shows the number of steps
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int n = readInt("Enter a number:"); 
		int counter = 0;
		while(n != 1){
			if (n % 2 == 0){
				print(n + " is even so I take half: ");
				n /= 2;
				println(n);
			}
			else{
				print(n + "is odd, so I make 3n + 1: ");
				n = 3 * n + 1;
				println(n);
			}
			counter++; 
		}
		println("The process took " + counter + " to reach 1");
	}
}

