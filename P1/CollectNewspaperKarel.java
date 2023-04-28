/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends Karel {
	
	// You fill in this part
	/*Makes Karel go to the beeper 
	 * pick up the beeper then return to the starting position
	 * precondition: Karel is facing east and is in the 
	 * top left corner of the room 
	 * postcondition: Karel is facing east and is in the 
	 * top left corner in the room and has a beeper
	 * beeper is no longer outside of the room
	 */
	public void run(){
		gotoBeeper();
		pickBeeper(); 
		returntoStart();
	}
	/*Makes Karel turn 90 degrees clockwise, essentially turn right
	 * by making Karel turn left three times
	 * precondition: none
	 * postcondition: Karel is now facing 90 degrees clockwise from 
	 * the original direction
	 */
	private void turnRight(){
		turnLeft();
		turnLeft();
		turnLeft();
	}
	/*Makes Karel move to the end of the inner room
	 *  turn right move turn left and move to the beeper
	 * precondition: Karel starts at the top left corner of the room
	 * facing east
	 * postcondition: Karel is facing the same direction as before
	 * but is now on top of the beeper
	 */
	private void gotoBeeper(){
		move(); 
		move();
		turnRight();
		move();
		turnLeft();
		move();
	}
	/*Makes Karel turn left twice move turn right move 
	 * turn left move twice back to the corner of the room
	 * turn left twice and face the same way as the starting
	 * precondition: Karel starts where the beeper was picked up 
	 * facing east
	 * postcondition: Karel is facing east 
	 * and is in the top corner of the inner room
	 */
	private void returntoStart(){
		turnLeft();
		turnLeft();
		move();
		turnRight();
		move();
		turnLeft();
		move();
		move();
		turnLeft();
		turnLeft();
	}
	
}
