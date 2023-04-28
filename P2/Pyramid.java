/*
 * File: Pyramid.java
 * Name: Mitchell So
 * Section Leader: Ashley Taylor
 * ------------------
/* this builds a pyramid by centering a row of blocks of equal length starting at 14
 * then incrementing the height by the brick height and incrementing 
 * the number of bricks by 1 so that each subsequent row is one less brick and 
 * one brick length higher 
 */


import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

	/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

	/** Height of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

	/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;

	public void run() {
		int baseBricks = BRICKS_IN_BASE; 
		int y = getHeight(); 
		for (baseBricks = BRICKS_IN_BASE; baseBricks > 0; baseBricks--){
			y -= BRICK_HEIGHT; 
			for(int c=0; c < baseBricks ; c++ ){
				GRect rect = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				double x = c * BRICK_WIDTH;
				add(rect,( getWidth() - baseBricks* BRICK_WIDTH ) / 2  + x,y);
			}	
		}	
	}	
}

