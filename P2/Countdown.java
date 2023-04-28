/*
 * File: Countdown.java
 * Name: Mitchell So	
 * Section Leader: Ashley Taylor
 * ----------------------
/* displays numbers counting down from start to 1 with 1 second in between 
 * then says liftoff
 */

import acm.program.*;

public class Countdown extends ConsoleProgram {

	/** Count down to 0 from this number */
	private static final int START = 10;

	public void run() {
		for(int i=START; i > 0; i--){
			println(i);
			pause(1000);
		}
		println("Liftoff");
	}
}

