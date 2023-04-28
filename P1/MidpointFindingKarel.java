/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {

	// You fill in this part
	/* Makes Karel find the middle of the room and place 
	 * a beeper on the middle spot by lining up beepers then cutting
	 * off the ends until reaching the middle 
	 * if it is in a 1x1 karel just puts a beeper
	 * precondition: there are no beepers and Karel is facing east 
	 * starting from the bottom left corner of the room
	 * postcondition: Karel is in the middle of the room 
	 * and standing on a beeper placed in the middle
	 */
	public void run(){
		if (frontIsClear()){
			layBeepers();
			cutEnds();
			clearLine();
		}
		else {
			putBeeper();
		}
	}
	/*makes Karel pick up the beeper she's standing on 
	 * turn around move and then move until she reached the last beeper
	 * on the end and is blocked by the wall pick up that beeper
	 * turn around and move
	 * precondition: Karel is facing east on the far right
	 * corner of the room and there are beepers on the row 
	 * she is standing on with no spaces
	 * post condition: Karel is facing east and is on 
	 * the second from the last space on top of the beeper
	 * the end two beepers are now gone
	 * 
	 */
	private void cutEnds(){
		pickBeeper();
		turnAround();
		move();
		while (beepersPresent() && frontIsClear()) {
			move();
		}
		pickBeeper();
		turnAround();
		move();
	}
	/* Karel moves while beepers are present then turns around
	 * moves and picks up the end beeper and moves again until 
	 * there are no beepers and Karel is in the middle Karel then places a beeper
	 * Precondition: Karel is standing on a beeper on the beeper line facing 
	 * where the beeper line goes on
	 * postcondition: Karel has cleared the ends of the beeper line 
	 * until reaching the center and places a beeper in the center of the room
	 * and stands on the beeper
	 */
	private void clearLine(){
		while (frontIsClear() && beepersPresent()){
			while (beepersPresent()) {
				move();
			}
			turnAround();
			move();
			pickBeeper();
			move();
		}
		turnAround();
		move();
		putBeeper();
	}
	/* Karel lays a line of beepers the length of the room
	 * with no spaces
	 * precondition: Karel starts at the bottom left corner of the room 
	 * facing east and there are no beepers present
	 * postcondition: Karel is at the opposite corner of the room facing east
	 * and there are beepers filling the row karel travels
	 */
	private void layBeepers(){
		putBeeper();
		while (frontIsClear()){
			move();
			putBeeper();
		}
	}
}