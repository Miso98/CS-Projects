/*
 * File: Hangman.java
 * Name: Mitchell So
 * Section Leader: Ashley Taylor
 * ------------------
 * This program plays the classic game of hangman you are given 7 guesses to find a word
 * that is randomly chosen from a list, with each wrong guess another string from Karel's parachute is cut
 * until she falls over
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import acmx.export.java.io.FileReader;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Hangman extends ConsoleProgram {

	/***********************************************************
	 *              CONSTANTS                                  *
	 ***********************************************************/

	/* The number of guesses in one game of Hangman */
	private static final int N_GUESSES = 7;
	/* The width and the height to make the karel image */
	private static final int KAREL_SIZE = 150;
	/* The y-location to display karel */
	private static final int KAREL_Y = 230;
	/* The width and the height to make the parachute image */
	private static final int PARACHUTE_WIDTH = 300;
	private static final int PARACHUTE_HEIGHT = 130;
	/* The y-location to display the parachute */
	private static final int PARACHUTE_Y = 50;
	/* The y-location to display the partially guessed string */
	private static final int PARTIALLY_GUESSED_Y = 430;
	/* The y-location to display the incorrectly guessed letters */
	private static final int INCORRECT_GUESSES_Y = 460;
	/* The fonts of both labels */
	private static final String PARTIALLY_GUESSED_FONT = "Courier-36";
	private static final String INCORRECT_GUESSES_FONT = "Courier-26";

	/***********************************************************
	 *              Instance Variables                         *
	 ***********************************************************/

	/* An object that can produce pseudo random numbers */
	private RandomGenerator rg = new RandomGenerator();
	/* the container for the graphics portion of hangman */
	private GCanvas canvas = new GCanvas();
	/* the string that contains the partially guessed word */
	private String partiallyGuessedWord = "";
	/* number of guesses left that the user has */
	private int guessesLeft = N_GUESSES;
	/* the randomly generated word that will be used for the game */
	private String word = getRandomWord();
	/* an array list that will contain the lines for the parachute */
	ArrayList<GLine> parachuteStringsList = new ArrayList<GLine>();
	/* the label display that shows the partially solved word */
	private GLabel wordDisplay;
	/* the label that will show the incorrectly guessed letters*/
	private GLabel incorrectGuesses;
	/* that string in which the incorrectly guessed letters will be stored */
	private String incorrectGuessesString = "";


	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/
/*
 * adds the canvas which will be the container for the graphics
 *  then adds background, karel, and the parachute 
 */
	
	public void init() {
		add(canvas);
		drawBackground();
		addKarel();
		addParachute();
	}



	/*adds the parachute and strings to the canvas the parachute is centered and at a designated height
	 * there are 7 strings that represent the seven guesses and they are evenly spaced
	 * along the width of the parachute
	 */

	private void addParachute() {
		GImage parachute = new GImage("parachute.png");
		parachute.setSize(PARACHUTE_WIDTH, PARACHUTE_HEIGHT);
		canvas.add(parachute, canvas.getWidth() / 2.0 - parachute.getWidth() / 2.0, PARACHUTE_Y);
		double x = canvas.getWidth() / 2.0;
		double y= KAREL_Y;
		double endX = canvas.getWidth() / 2.0 + parachute.getWidth() / 2.0 + parachute.getWidth() / (N_GUESSES - 1);
		double endY = parachute.getHeight() + PARACHUTE_Y;
		for (int i = 0; i < N_GUESSES; i++){
			GLine parachuteStrings = new GLine (x, y, endX -= parachute.getWidth()/(N_GUESSES - 1), endY);
			canvas.add(parachuteStrings);
			parachuteStringsList.add(parachuteStrings);
		}	
	}

	/*adds Karel to the center of the canvas at a given Y coordinate 
	 */

	private void addKarel() {
		GImage karel = new GImage("karel.png");
		karel.setSize(KAREL_SIZE, KAREL_SIZE);
		canvas.add(karel, canvas.getWidth() / 2.0 - karel.getWidth() / 2.0, KAREL_Y);
	}

	/* makes a new partially solved word with dashes 
	 * adds the word display and incorrect guesses display. Plays the game and 
	 * ends the game depending on number of guesses left
	 */
	public void run() {
		for (int i = 0; i < word.length(); i++){
			partiallyGuessedWord += "-"; 
		}

		addWordDisplay();
		addIncorrectGuesses();
		playGame();
		endGame();
	}
	/*adds a GLabel with the partially guessed word at a designated height and centered on the canvas 
	 * with a designated font and font size
	 * 
	 */
	private void addWordDisplay() {
		wordDisplay = new GLabel (partiallyGuessedWord);
		wordDisplay.setFont(PARTIALLY_GUESSED_FONT);
		canvas.add(wordDisplay, canvas.getWidth() / 2.0 - wordDisplay.getWidth() / 2.0 , PARTIALLY_GUESSED_Y);
	}

	/*adds a label that shows the incorrect guesses under the partially guessed word
	 */

	private void addIncorrectGuesses(){
		incorrectGuesses = new GLabel(incorrectGuessesString);
		incorrectGuesses.setFont(INCORRECT_GUESSES_FONT);
		canvas.add(incorrectGuesses, canvas.getWidth() / 2.0 - incorrectGuesses.getWidth() / 2.0, INCORRECT_GUESSES_Y );

	}

	/*draws the background for the graphics part adds canvas to the init
	 */

	private void drawBackground() {
		GImage bg = new GImage("background.jpg");
		bg.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.add(bg, 0, 0);
	}

	/*while there are guesses left in the hangman game and the word still has dashes
	 * meaning it has not been completely figured out through guesses
	 * this method gets the guess from the player then checks to see if the word has the letter
	 */
	private void playGame() {
		while(guessesLeft > 0 && wordHasDashes()){
			String guess = getGuess();
			checkGuess(guess);
		}

	}

	/* This method checks the string guess that is prompted. if the guess is not a single letter
	 * the method will say the move is illegal otherwise the letter will be converted to uppercase
	 * then iterated through the loop to check if it occurs in the word if it appears
	 * the partially guessed word will be changed so that the guess is now incorporated into the new 
	 * partially guessed word otherwise the guess will be called wrong and you lose a guess. if you get a right guess
	 * the word display is then updated showing your partially guessed word
	 * incorrect letter guesses are also added to the bottom of the screen
	 */

	private void checkGuess(String guess) {
		if(guess.length() != 1 || !Character.isLetter(guess.charAt(0))) {
			println("Illegal guess please enter guess");
		} else{
			guess = guess.toUpperCase();
			boolean correctGuess = false;
			for (int i = 0; i < word.length(); i++){
				if(guess.charAt(0) == (word.charAt(i))){
					partiallyGuessedWord = partiallyGuessedWord.substring(0, i) + guess + partiallyGuessedWord.substring(i+1, word.length());
					correctGuess = true;
				}
			} 
			if (!correctGuess) {
				println("There are no " + guess + "'s in the word");
				removeParachuteLine();
				canvas.remove(incorrectGuesses);
				incorrectGuessesString += guess;
				addIncorrectGuesses();
				guessesLeft--;
			} else {
				println("That guess is correct.");
			}
		}
		canvas.remove(wordDisplay);
		addWordDisplay();
	}


	/*removes parachute lines from outside in by removing the lines from the array 
	 * then removing that line from the canvas and then shortening the array it removes the outside
	 * lines by alternating between the end and beginning of the array depending on how many guesses are left
	 */
	private void removeParachuteLine() {
		if (guessesLeft % 2 == 1) {	
			GLine parachuteString = parachuteStringsList.get(parachuteStringsList.size()-1);
			parachuteStringsList.remove(parachuteString);
			canvas.remove(parachuteString);
		}
		if (guessesLeft % 2 == 0) {
			GLine parachuteString = parachuteStringsList.get(0);
			parachuteStringsList.remove(parachuteString);
			canvas.remove(parachuteString);
		}
	}


	/* this prompts the user to enter a guess and then sets that string to be called guess
	 * the method then returns guess
	 */

	private String getGuess() {
		println("Your word looks like this: " + partiallyGuessedWord);
		println("You have " + guessesLeft + " guesses left");
		String guess = readLine("Your guess: ");
		return guess;

	}
	/* terminates the game and prints whether the player wins or loses based on how many guesses 
	 * there are left. because the loop in play game only continues if there are still guesses or if the 
	 * word has been completely guessed, the end game method says whether the game was won or lost based 
	 * on number of guesses. if there are none left you lost if there are guesses left then the word was completely guessed and you won
	 */

	private void endGame() {
		if (guessesLeft == 0){
			println("You lose.");
			println("The word was: " + word);
			removeKarel();
			addFlippedKarel();

		} else {
			println("You win.");
			println("The word was: " + word);
		}

	}
	/*removes karel from the canvas
	 */

	private void removeKarel() {
		GObject karel = canvas.getElementAt(canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
		canvas.remove(karel);
	}

	/*adds a flipped karel in the same spot that the original karel was in 
	 */
	private void addFlippedKarel() {
		GImage flippedKarel = new GImage("karelFlipped.png"); 
		flippedKarel.setSize(KAREL_SIZE, KAREL_SIZE);
		canvas.add(flippedKarel, canvas.getWidth() / 2.0 - flippedKarel.getWidth() / 2.0, KAREL_Y);

	}


	/*checks if the word still has dashes and returns true if the word still has dashes
	 * meaning that the word has not been completely guessed
	 */
	private boolean wordHasDashes() {
		for (int i = 0; i < partiallyGuessedWord.length(); i++){
			if(partiallyGuessedWord.charAt(i) == '-'){
				return true;
			}
		}
		return false;
	}


	
	/*gets a random word by reading the file Hangman lexicon then 
	 * entering the read lines which are strings into an array list
	 * until the line is null then a random generator is used to get a random word
	 * in the array list
	 */
	
	private String getRandomWord() {
		ArrayList <String> randomWords = new ArrayList<String>();
		try{
			BufferedReader br =	new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while(true){
				String line = br.readLine();
				if(line == null){
					break;
				}	
				randomWords.add(line);
			}
			br.close();
		}  catch (IOException e) {
			throw new RuntimeException(e);
		}
		int index = rg.nextInt(randomWords.size()-1);
		return randomWords.get(index);

	}

}
