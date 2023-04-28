/*
 * File: Target.java
 * Name: Mitchell So
 * Section Leader: Ashley Taylor
 * -----------------
/*Creates a centered oval filled red
 * creates a concentric oval filled white
 * adds a concentric oval filled red all three shapes are stacked 
 * on top of each other
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {	
	
	/** the conversion ratio from inches to pixels */
	private static final double INCH_RATIO = 72;
	
	/** the radius of the outermost circle of the target */
	private static final double OUTTER_RADIUS = 1 * INCH_RATIO;
	
	/** the radius of the middle circle of the target */
	private static final double MIDDLE_RADIUS = .65 * INCH_RATIO;
	
	/** the radius of the innermost circle of the target */
	private static final double INNER_RADIUS = .3 * INCH_RATIO;

	public void run() {
		Ovals(OUTTER_RADIUS, Color.RED);
		Ovals(MIDDLE_RADIUS, Color.WHITE);
		Ovals(INNER_RADIUS,  Color.RED);
	}

	/*creates an oval of designated radius and color in
	 * a position based on radius so that circles will be concentric
	 */

	private void Ovals (double radius, Color color){
		GOval Ovals = new GOval (radius, radius);
		Ovals.setFilled(true);
		Ovals.setColor(color);
		double x = getWidth()/ 2.0 - radius/ 2.0; 
		double y = getHeight() / 2.0 - radius/ 2.0;
		add (Ovals, x, y);

	}
}
