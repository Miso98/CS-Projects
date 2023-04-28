/*
 * File: PythagoreanTheorem.java
 * Name: Mitchell So
 * Section Leader: Ashley Taylor
 * -----------------------------
/*prompts for two positive values then finds the hypotenuse
 * by calculating the square root of the sums of the values
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("Enter values to compute the the Pythagorean theorem.");
		double a = readDouble("a: ");
		double b = readDouble("b: ");
		double c = Math.sqrt(a*a + b*b);
		println("c = " + c);
	}
}

