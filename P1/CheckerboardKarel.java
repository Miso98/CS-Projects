/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	// You fill in this part
	/* Makes Karel fill every other space in every row or column
	 * depending on the room making a checker board.
	 * for a column Karel turns left and treats the column
	 * as a rotated row and fills the row normally
	 * precondition: there are no beepers present and Karel 
	 * is at the bottom left corner of the room or the bottom of the column
	 * depending on the room
	 * postcondition: Karel is at the top of the room and every row has 
	 * beepers in every other space or if the room is a column every other
	 * space in the column is filled with beepers
	 */
	public void run(){
		if (frontIsBlocked()){
			turnLeft();
			fillRow();
		}
		while (frontIsClear()){
			fillRow();	
		}
	}
	/*Makes Karel fill a singular row
	 * Karel places a beeper then moves to every other space
	 *  and places another beeper. if there are more rows above Karel will
	 *  move to the next row facing the next wall to fill the next row
	 * precondition: Karel is in the bottom left facing east 
	 * post condition: the row in the room has a beeper in every other spot 
	 * if Karel after filling the row with beepers is blocked and there are
	 * no more rows Karel will be facing north
	 * if there are more rows and the last space is a beeper Karel will be 
	 * in the next row in the second space from the nearest wall
	 * if there are more rows an the last space has no beeper Karel will be 
	 * in the next row in first space next to wall
	 */
	private void fillRow(){
		putBeeper();
		while (frontIsClear()){
			move();
			if (frontIsClear()){
				move();
				putBeeper();
			}
		}
			if (frontIsBlocked() && facingEast()){
				turnLeft();
				if (noBeepersPresent() && frontIsClear()){
					move();
					turnLeft();
				}
				if (beepersPresent() && frontIsClear()){
					move();
					turnLeft();
					move();
				}	
				
			}
			if (frontIsBlocked() && facingWest()){
				turnRight();
				if (noBeepersPresent() && frontIsClear()){
					move();
					turnRight();
				}
				if (beepersPresent() && frontIsClear()){
					move();
					turnRight();
					move();
				}
			}
		}
	}	

