/*
 * File: Breakout.java
 * -------------------
 * Name:Mitchell So
 * Section Leader: Ashley Taylor
 * 
 * This plays the classic game of breakout. Bricks are set up and a ball goes down at a random angle
 * and bouncing the paddle which is controlled by the mouse onto the ball, the player must break all the bricks
 * to win the game. 
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels.  IMPORTANT NOTE:
	 * ON SOME PLATFORMS THESE CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS
	 * OF THE GRAPHICS CANVAS.  Use getWidth() and getHeight() to get the 
	 * dimensions of the graphics canvas. */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board.  IMPORTANT NOTE: ON SOME PLATFORMS THESE 
	 * CONSTANTS MAY **NOT** ACTUALLY BE THE DIMENSIONS OF THE GRAPHICS
	 * CANVAS.  Use getWidth() and getHeight() to get the dimensions of
	 * the graphics canvas. */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH =
			(WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** Delay before the ball moves again */
	private static final int DELAY = 10;

	/* Method: run() */
	/** Runs the Breakout program. */

	/** this is the original paddle */
	private GRect paddle;
	/** this provides the x coordinate of the brick */
	private int x;
	/** this is the ball */
	private GOval ball; 
	/** this is the x and y components of the velocity of the ball */
	private double vx, vy;
	/** this is the random generator to randomize the initial vx of the ball */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	/** the number of bricks in the game that need to be broken to win */
	private int numberOfBricks = NBRICK_ROWS * NBRICKS_PER_ROW;
	/** this is the object that collides with the ball */
	private GObject collider;
	/** this is the audio clip that plays whenever the ball bounces off the paddle or a block */
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
	/** this is the score */
	private int score;
	/** this shows the score on the scoreboard */
	private GLabel scoreBoard;

	/* sets up the bricks 
	 * adds a mouse listener and then plays the game for however many lives there are
	 * and while there are still bricks you continue playing the game
	 * once the for loop ends the game ends
	 */

	public void run() {
		setBricks();
		addMouseListeners();
		for (int lives = NTURNS; lives > 0; lives --){
			if(numberOfBricks != 0){
				playGame();	
			}
		}
		endGame();
	}

	/*displays the text saying either winner or loser depending if there are bricks left
	 * if there are bricks left after the for loop with play game ends, you get the loser 
	 * message. if there are no bricks left then you get the winner message
	 */
	private void endGame() {
		if (numberOfBricks != 0){
			addLoser();
		} else {
			addWinner();
		}
	}
	/*adds a label with text that says "congrats! you win!!!" 
	 * in the middle of the screen
	 */
	private void addWinner() {
		GLabel Loser  = new GLabel ("Congrats! You win!!!");
		add(Loser, getWidth() / 2.0 - Loser.getWidth() / 2.0, getHeight() / 2.0);
	}

	/*adds a label with text that says "no more lives left, you lose"
	 *  in the middle of the screen 
	 */
	private void addLoser() {
		GLabel Loser  = new GLabel ("No more lives left, you lose!");
		add(Loser, getWidth() / 2.0 - Loser.getWidth() / 2.0, getHeight() / 2.0);

	}

	/*makes a ball and the paddle, creates a random initial velocity and waits for the 
	 * mouse click, after the click collisions will be accounted for and the ball 
	 * will subsequently be moved also shows the score board
	 * 
	 */
	private void playGame() {
		makeBall();
		makePaddle();
		setUpVelocity();
		waitForClick();
		addScoreBoard();
		manageCollisions();
	}

	/*adds the scoreboard by adding a label at the bottom of the screen
	 * 
	 */
	private void addScoreBoard() {
		scoreBoard = new GLabel ("Score: 0");
		add (scoreBoard, getWidth() / 2.0 - scoreBoard.getWidth() / 2.0, getHeight() - scoreBoard.getHeight() / 2.0);

	}

	/*this sets up the initial velocity of the ball 
	 * this randomizes the initial x component of the velocity as to randomize the angle
	 */
	private void setUpVelocity() {
		vx = rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean(.5)){
			vx = -vx;
		}
		vy = 3.0;
	}

	/* this method runs while there are still bricks in the game
	 * the ball will move and if it hits the left or right wall 
	 * the direction of the x velocity is reversed if the ball hits the top wall the y velocity
	 * direction will be reversed if the object that collides with the ball is not null
	 * a sound will play, and if that object is the paddle and the y velocity's direction is moving down
	 * the direction will be reversed, if the collider is not the paddle, the object is removed and the brick count is decreased
	 * if the ball hits the bottom wall, then the entire loop is broken 
	 * at the end of each iteration of the while loop the paddle and ball are removed as to not crowd the screen and add extra obstacles
	 */
	private void manageCollisions() {
		while(numberOfBricks > 0){
			ball.move(vx,vy);
			if(hitLeftWall(ball,vx) || hitRightWall(ball, vx)){
				vx = -vx;
			}
			if(hitTopWall(ball,vy)){
				vy = -vy;
			}
			collider = collisions();
			if(collider != null){ 
				bounceClip.play();
				if (collider == paddle){
					if (vy > 0){
						vy = -vy;
					} 
				} else {
					remove(collider);
					vy = -vy;
					score ++;
					scoreBoard.setLabel("Score:" + score);
					numberOfBricks --;
				}
			}
			if(hitBottom(ball,vy)){
				break;
			}
			pause(DELAY);
		}
		remove(paddle);
		remove(ball);
	}


	/*this sets the corners of the of the ball or rather a square that encircles the ball as objects
	 * these objects will be defined as the element at each corner point 
	 * and if the point reaches an object that is not null, this method will return that object otherwise 
	 * this method will return null
	 */
	private GObject collisions(){
		GObject topLeft = getElementAt(ball.getX(), ball.getY());
		GObject topRight =getElementAt(ball.getX() + 2* BALL_RADIUS, ball.getY());
		GObject bottomLeft = getElementAt(ball.getX(), ball.getY() + 2* BALL_RADIUS);
		GObject bottomRight = getElementAt(ball.getX() + 2* BALL_RADIUS, ball.getY() + 2*BALL_RADIUS);
		if (topLeft != null){
			return topLeft;	
		}
		else if (topRight != null){
			return topRight;
		}
		else if (bottomLeft != null){
			return bottomLeft;	
		}
		else if (bottomRight != null){
			return bottomRight;	
		}
		else {
			return null;
		}
	}

	/* creates new GRect paddle of designated size and filled black 
	 */

	private GRect makePaddle() {
		paddle = new GRect(PADDLE_WIDTH,PADDLE_HEIGHT);
		paddle.setFilled(true);
		return paddle;
	}

	/*creates a ball of set size and filled color and starting in the middle of the screen
	 */
	private GOval makeBall() {
		ball = new GOval(2 * BALL_RADIUS, 2 * BALL_RADIUS);
		ball.setFilled(true);
		add(ball, getWidth() / 2.0 - BALL_RADIUS, getHeight() / 2.0 - BALL_RADIUS);
		return ball;
	}

	/* this boolean will return the ball true if the ball goes past the bottom wall
	 * and will return false if the ball is traveling up
	 */

	private boolean hitBottom(GOval ball2, double vy2) {
		if (vy < 0) return false;
		return ball.getY() >= getHeight() - ball.getHeight();
	}

	/*this boolean will return the ball false if the velocity shows it is traveling down
	 * and the method will return the ball true if the ball reaches the top wall 
	 */
	private boolean hitTopWall(GOval ball2, double vy2) {
		if (vy > 0) return false;
		return ball.getY() <= 0;
	}
	/*this boolean will return the ball if it reaches the right wall of the screen 
	 * and it will return false if the ball is traveling to the left 
	 */

	private boolean hitRightWall(GOval ball2, double vx2) {
		if (vx < 0) return false;
		return ball.getX() >= getWidth() - ball.getWidth();
	}
	/*this boolean will return false if the ball is traveling right 
	 * and it will return the ball if the ball hits the left wall
	 */

	private boolean hitLeftWall(GOval ball2, double vx2) {
		if (vx > 0) return false;
		return ball.getX() <= 0; 
	}

	/* gets the x value of where the mouse is and adds a new paddle starting at the new x value
	 *  so long as the paddle stays within the window the y value of the paddle remains constant 
	 *  this also gets rid of the original position of the paddle by creating a new paddle
	 */

	public void mouseMoved(MouseEvent e){
		x = e.getX();
		int y = getHeight() - PADDLE_Y_OFFSET;
		if (x < getWidth() - PADDLE_WIDTH){
			add(paddle, x, y);
		}
	}

	/* this sets up the rows of bricks by first creating a single brick then iterating it
	 * the number of times needed designated by bricks per row then this will create multiple rows 
	 * and repeat this row creating process by iterating the height by the height plus brick separation
	 * this thus creates multiple rows of colored bricks evenly spaced the color is determined by row number
	 * and will repeat the pattern if more rows are added
	 */

	private void setBricks() {
		int y = BRICK_Y_OFFSET; 
		for (int row = 0; row < NBRICK_ROWS; row++){
			y += BRICK_HEIGHT + BRICK_SEP;
			for( int i = 0; i < NBRICKS_PER_ROW; i++){ 
				GRect rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				double x = i * (BRICK_WIDTH + BRICK_SEP);
				rect.setFilled(true);
				rect.setColor(colorRows(row));
				add(rect, x, y);
			}
		}
	}

	/* this colors the blocks in order in pairs of two rows and will repeat the pattern
	 * if the number of rows is increased
	 * value of row color should always be between 0 and 9 otherwise it returns null 
	 * this should not happen because the if statements catch every condition
	 */
	private Color colorRows(int rowNumber) {
		int rowColor = rowNumber % 10;
		if(rowColor == 8 || rowColor == 9){
			return Color.CYAN;
		} 
		if(rowColor == 6 || rowColor == 7){
			return Color.GREEN;
		}
		if(rowColor == 4 || rowColor == 5){
			return Color.YELLOW;
		}
		if(rowColor == 2 || rowColor == 3){
			return Color.ORANGE;
		}
		if(rowColor == 0 || rowColor == 1){
			return Color.RED;
		}
		// this will never happen because the ifs will catch every condition
		return null; 
	}
}

