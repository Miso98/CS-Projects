/*
 * File: DrawCenteredRect.java
 * Name: Mitchell So	
 * Section Leader: Ashley Taylor
 * ----------------------
/*  creates a blue filled rectangle at the center of the screen
 * gets the width of the screen and height of the window and centers 
 * the rectangle based on the height and width of the window and rectangle
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class DrawCenteredRect extends GraphicsProgram {

	/** Size of the centered rect */
	private static final int WIDTH = 350;
	private static final int HEIGHT = 270;

	public void run() {
		GRect rect = new GRect (WIDTH,HEIGHT);
		rect.setFilled(true);
		rect.setColor(Color.BLUE);
		double X = getWidth() / 2.0 - WIDTH / 2.0; 
		double Y = getHeight() / 2.0 - HEIGHT/ 2.0; 
		add (rect, X, Y);
	}
}

