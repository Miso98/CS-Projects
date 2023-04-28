/*
 * File: Debugger.java 
 * ---------------------------- 
 * This program is supposed to animate a ball that bounces off the 
 * four walls.  The ball should change color whenever it hits a wall. 
 * The program should also display a big "Bouncing Bouncing Ball!" 
 * label, centered horizontally.  The label color should always match 
 * the color of the ball, and both should initially be colored red. 
 *
 * However, the program has a few errors.  Use the Eclipse debugger 
 * to find and fix them!  When you're done, the program will
 * print out a secret code you should send to your Section Leader!
 */

import acm.program.*;
import acm.graphics.*;
import acm.util.*;
import java.awt.*;

public class Debugger extends GraphicsProgram {

	/* Constants */
	private static final int BALL_RADIUS = 20;
	
	private static final double MIN_DX = 2.0;
	private static final double MAX_DX = 3.0;
	private static final double MIN_DY = 2.0;
	private static final double MAX_DY = 3.0;
	
	private static final double PAUSE = 15.0;
	private static final int TEXT_HEIGHT = 100;
	private static final int NUM_COLORS = 5;
	private static final int CODE_LENGTH = 6;	// DO NOT MODIFY

	/* Instance Variables */
	private RandomGenerator rgen = RandomGenerator.getInstance();
	private GLabel text;
	private int numBounces = 0;
	private GOval ball;
	private double dx;
	private double dy;


	public void run() {
		rgen.setSeed(106 + 'A');	// DO NOT MODIFY - THIS CODE IS BUG-FREE :)
		dx = rgen.nextDouble(MIN_DX, MAX_DX);
		dy = rgen.nextDouble(MIN_DY, MAX_DY);
		setupLabel();
		setupBall();

		while (true) {
			ball.move(dx, dy);
			checkForCollisions();
			pause(PAUSE);
		}
	}


	private void setupLabel() {
		text = new GLabel("Bouncing Bouncing Ball!");
		text.setFont(new Font("Arial", Font.BOLD, 32)); 
		double x = (getWidth() / 2.0) - (text.getWidth() / 2.0);       
		double y = getHeight()/2.0 - TEXT_HEIGHT/2.0;    
		text.setLocation(x,y);
		
		   
		text.setColor(Color.RED);
		add(text);
	}

	private void setupBall() {
		ball = new GOval(BALL_RADIUS * 2, BALL_RADIUS * 2); 
		ball.setFilled(true);
		ball.setColor(Color.RED);
		double x = rgen.nextDouble(0, getWidth() - (BALL_RADIUS * 2));
		double y = rgen.nextDouble(0, getHeight() - (BALL_RADIUS * 2));
		ball.setLocation(x, y);
		add(ball);
	}

	/*
	 * If the ball collides with one of the walls, change the 
	 * corresponding direction and change the color.  Note that it's 
	 * possible to bounce off two walls (e.g. the corner) simultaneously. 
	 */
	private void checkForCollisions() { 
		boolean didBounce = false;
		if  (ball.getX() <= 0 || ball.getX() >= getWidth() - (BALL_RADIUS * 2)) {
			dx = -dx;
			Color color = getRandomNewColor(ball.getColor());
			ball.setColor(color);
			ball.move(dx, 0); // Move the ball out of the wall
//			ball.setColor(getRandomNewColor(ball.getColor()));
			text.setColor((color));
			
			didBounce = true;
			numBounces++;
		}


		if  (ball.getY() <= 0 || ball.getY() >= getHeight() - (BALL_RADIUS * 2)) {
			dy = -dy;
			ball.move(0, dy); // Move the ball out of the wall
			Color color = getRandomNewColor(ball.getColor());
			ball.setColor(color);
			text.setColor((color));

			didBounce = true;
			numBounces++;
		}
		

		
		
		/* DO NOT MODIFY THIS CODE - THIS CODE IS BUG-FREE :) */
		if (didBounce && numBounces < CODE_LENGTH) {
			decodeSecretMessage(ball);
		} else if (didBounce && numBounces == CODE_LENGTH) {
			println("\n^^^ If your program is working (should be of the format G-Y-B--1-9, but with no dashes), send this secret code to your Section Leader!");
		}
		/* SPECIAL CODE END */
	}

	/* 
	 * Choose a random color that is different than "prevColor" 
	 */
	private Color getRandomNewColor(Color prevColor) {
		while (true) { 
			Color newColor;
			int color = rgen.nextInt(0,  NUM_COLORS-1);
			if (color == 0) {
				newColor = Color.RED;
			} else if (color == 1) {
				newColor = Color.ORANGE;
			} else if (color == 2) {
				newColor = Color.YELLOW;
			} else if (color == 3) {
				newColor = Color.GREEN;
			} else {
				newColor = Color.BLUE;
			}

			// If the new color is the same as the previous color, 
			// choose again 
			if (!newColor.equals(prevColor)) return newColor;
		}
	}
	
	/* DO NOT MODIFY THIS CODE - THIS CODE IS BUG-FREE :) */
	private void decodeSecretMessage(GOval b) {
		if (b.getColor() == Color.RED) {
			print("R" + ("" + (b.getY() / getHeight() * 100.0)).substring(0, 1));
		} else if (b.getColor() == Color.ORANGE) {
			print("O" + ("" + text.getX()).substring(0, 1));
		} else if (b.getColor() == Color.YELLOW) {
			print("Y" + ("" + (b.getY() / getHeight() * 100.0)).substring(0, 1));
		} else if (b.getColor() == Color.GREEN) {
			print("G" + ("" + (b.getY() / getHeight() * 100.0)).substring(0, 1));
		} else if (b.getColor() == Color.BLUE) {
			print("B" + ("" + (b.getY() / getHeight() * 100.0)).substring(0, 1));
		}
	}
	/* SPECIAL CODE END */
}