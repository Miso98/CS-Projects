/*
 * File: CS106ATiles.java
 * Name: Mitchell So
 * Section Leader: Ashley Taylor
 * ----------------------
/* Creates 4 boxes each with a label cS106A in the middle all four boxes
 * have identical dimensions and are evenly spaced and centered in the 
 * middle of the screen evenly spread apart 
 * first  the top left tile is created then the modular additive creates 
 * another  tile and title in the next position to the right of the original tile
 * which is one tile space and one title width to the right
 *  but on the same row as in the original all in the second iteration of the for loop
 *  next the bottom left tile and CS106A title are created and the same modular additive
 *  is used to create a second CS106A label to the right of the original in the second iteration of the for loop
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class CS106ATiles extends GraphicsProgram {

	/** Amount of space (in pixels) between tiles */
	private static final int TILE_SPACE = 20;
	
	/** the height (in pixels) of the CS106A tile */
	private static final int TITLE_HEIGHT = 50; 
	
	/** the width (in pixels) of the CS106A tile */
	private static final int TITLE_WIDTH = 100;

	public void run() {
		for (int i = 0; i < 2; i++){
			CsLabelTop(i);
		}
		for (int i=0; i < 2; i++){
			CsLabelBot(i);
		}
	}

	/*This creates a CS106A title and tile in the 
	 * above and left of the center by half of a tile space
	 *  the modular additive is added depending on whether the i parameter is even or odd
	 *  if odd the tile and title will be placed in the left hand position 
	 *  while if even the tile and title will be placed in the calculated right hand position 
	 *  which is one tile space and title width to the right
	 */

	private void CsLabelTop (int i){
		GRect Tile = new GRect (TITLE_WIDTH, TITLE_HEIGHT);
		double x = getWidth() / 2.0 - TILE_SPACE/2.0 - TITLE_WIDTH + (i%2)*(TILE_SPACE + TITLE_WIDTH);
		double y = getHeight() / 2.0 - TILE_SPACE/2.0 - TITLE_HEIGHT;
		add (Tile, x, y);
		GLabel Label = new GLabel("CS106A");
		double a = (getWidth() - TITLE_WIDTH - TILE_SPACE - Label.getWidth())/2.0 + (i%2)*(TILE_SPACE + TITLE_WIDTH);
		double b = (getHeight() - TITLE_HEIGHT - TILE_SPACE + Label.getHeight())/2.0;
		add (Label, a, b);
	}

	/*This creates a CS106A title and tile in the 
	 * lower and left of the center by half of a tile space
	 *  the modular additive is added depending on whether the i parameter is even or odd
	 *  if odd the tile and title will be placed in the left hand position 
	 *  while if even the tile and title will be placed in the calculated right hand position
	 *  which is one tile space and title width to the right 
	 */

	private void CsLabelBot (int i){
		GRect Tile = new GRect (TITLE_WIDTH, TITLE_HEIGHT);
		double x = getWidth() / 2.0 - TILE_SPACE/2.0 - TITLE_WIDTH + (i%2)*(TILE_SPACE + TITLE_WIDTH);
		double y = getHeight() / 2.0 + TILE_SPACE/2.0;
		add (Tile, x, y);
		GLabel Label = new GLabel("CS106A");
		double a = (getWidth() - TITLE_WIDTH - TILE_SPACE - Label.getWidth())/2.0 + (i%2)*(TILE_SPACE + TITLE_WIDTH);
		double b = getHeight()/2.0 + TILE_SPACE/2.0 + TITLE_HEIGHT/2.0 + Label.getHeight()/2.0;
		add (Label, a, b);
	}
}

