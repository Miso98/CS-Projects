/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {

	// You fill in this part
	/*Makes Karel fill up the columns with beepers without 
	 * double stacking beepers in the columns that are 4 spaces apart
	 * precondition: Karel is facing east in the bottom left
	 * corner of the room
	 * postcondition: Karel is in the bottom right corner of the room
	 * and all the columns no longer have spaces without beepers facing east
	 */
	public void run(){
		while (frontIsClear()){
			makeColumn();
			moveFour();
		}
		makeColumn();
	}
	/* Makes Karel move four spaces forward
	 * precondition: none
	 * post condition: Karel has moved four spaces forward
	 */
	private void moveFour(){
		move();
		move();
		move();
		move();
	}
	/*Makes Karel fill up the column with beepers 
	 * by turning left and moving forward if a beeper is present
	 * if there is no beeper present karel places a beeper
	 * and continues to do so until the front is blocked
	 * Karel then turns around and repeats the process until 
	 * without double stacking
	 * precondition: Karel is facing east 
	 * post condition: Karel is facing east with the front blocked
	 * and there are beepers in the columns with no spaces 
	 * and the beepers are not double stacked
	 */
	private void makeColumn(){
		turnLeft();
		if (noBeepersPresent()){
			putBeeper();
		}
		while (frontIsClear()){
			if (beepersPresent()){
				move();
			}
			else{
				putBeeper();
				move();
			}
		}
		turnAround();
		while (frontIsClear()){
			if (beepersPresent()){
				move();
			}
			else{
				putBeeper();
				move();
			}
			
		}
		turnLeft();
	}
}
