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

import java.applet.AudioClip;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.*;

public class HangmanExtensions extends ConsoleProgram {

	/***********************************************************
	 *              CONSTANTS                                  *
	 ***********************************************************/

	/* The number of guesses in one game of Hangman */
	private static final int N_GUESSES = 7;
	/* The width and the height to make the link image */
	private static final int LINK_SIZE = 180;
	/* the size of the heart */
	private static final int HEART_SIZE = 30;
	/* The y-location to display link */
	private static final int LINK_Y = 230;
	/* the width and height of the attacking ganon picture */
	private static final int GANON_WIDTH = 200;
	private static final int GANON_HEIGHT = 100;
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


	AudioClip bgMusic = MediaTools.loadAudioClip("zelda - song of storms (deon custom remix).au");

	/***********************************************************
	 *              Instance Variables                         *
	 ***********************************************************/

	/* An object that can produce pseudo random numbers */
	private RandomGenerator rg = new RandomGenerator();
	/* the container for the graphics portion of hangman */
	private GCanvas canvas = new GCanvas();
	/* the string that contains the partially guessed word */
	private String partiallyGuessedWord;
	/* number of guesses left that the user has */
	private int guessesLeft;
	/* the randomly generated word that will be used for the game */
	private String word;
	/* an array list that will contain the lines for the parachute */
	ArrayList<GImage> heartsList = new ArrayList<GImage>();
	/* the label display that shows the partially solved word */
	private GLabel wordDisplay;
	/* the label that will show the incorrectly guessed letters*/
	private GLabel incorrectGuesses;
	/* that string in which the incorrectly guessed letters will be stored */
	private String incorrectGuessesString;
	/* this boolean will determine whether to play hangman again */
	private boolean playAgain;



	/***********************************************************
	 *                    Methods                              *
	 ***********************************************************/
	/*adds the canvas which will be the container for the graphics
	 *  then adds background, link, and the parachute 
	 */

	public void init() {
		add(canvas);
		drawBackground();
		addLink();
		addTriForce();
	}

	/*adds the parachute and strings to the canvas the parachute is centered and at a designated height
	 * there are 7 strings that represent the seven guesses and they are evenly spaced
	 * along the width of the parachute
	 */

	private void addTriForce() {
		GImage parachute = new GImage("triforcesymbol.jpg");
		parachute.setSize(PARACHUTE_WIDTH, PARACHUTE_HEIGHT);
		canvas.add(parachute, canvas.getWidth() / 2.0 - parachute.getWidth() / 2.0, PARACHUTE_Y);
		double endX = canvas.getWidth() / 2.0 + parachute.getWidth() / 2.0 + parachute.getWidth()/(N_GUESSES - 1);
		double endY = parachute.getHeight() + PARACHUTE_Y;
		for (int i = 0; i < N_GUESSES; i++){
			GImage hearts = new GImage ("hearts.png");
			hearts.setSize(HEART_SIZE,HEART_SIZE);
			canvas.add(hearts, endX -= parachute.getWidth()/(N_GUESSES - 1), endY);
			heartsList.add(hearts);
		}	
	}

	/*adds link to the center of the canvas at a given Y coordinate 
	 */

	private void addLink() {
		GImage link = new GImage("link.jpeg");
		link.setSize(LINK_SIZE, LINK_SIZE);
		canvas.add(link, canvas.getWidth() / 2.0 - link.getWidth() / 2.0, LINK_Y);
	}

	/* makes a new partially solved word with dashes 
	 * adds the word display and incorrect guesses display. Plays the game and 
	 * ends the game depending on number of guesses left
	 */
	public void run() {
		playAgain = true;
		while (playAgain) {
			guessesLeft = N_GUESSES;
			incorrectGuessesString = "";
			canvas.removeAll();
			init();
			word = getRandomWord();
			partiallyGuessedWord = "";
			for (int i = 0; i < word.length(); i++){
				partiallyGuessedWord += "-"; 
			}
			addWordDisplay();
			addIncorrectGuesses();
			bgMusic.play();
			playGame();
			endGame();
			playAgain = promptToPlayAgain();
		}
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
				ganonCut();
				removeHearts();
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

	/*adds a ganondorf picture to travel across the parachute line before cutting a string from the parachute
	 */
	private void ganonCut() {
		double vx = -3;
		double vy= 0;
		GImage ganon = new GImage("ganon.jpg");
		ganon.setSize(GANON_WIDTH,GANON_HEIGHT);;
		canvas.add(ganon, canvas.getWidth() - ganon.getWidth(), canvas.getHeight() / 2.0 - PARACHUTE_Y);
		while(ganon.getX() + ganon.getWidth() > 0){
			ganon.move(vx, vy);
			pause(10);
		}
		canvas.remove(ganon);
	}



	/*removes parachute lines from outside in by removing the lines from the array 
	 * then removing that line from the canvas and then shortening the array it removes the outside
	 * lines by alternating between the end and beginning of the array depending on how many guesses are left
	 */
	private void removeHearts() {
			GImage removedHeart = heartsList.get(heartsList.size()-1);
			heartsList.remove(removedHeart);
			canvas.remove(removedHeart);
		
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
			removeLink();
			addYouLose();

		} else {
			println("You win.");
			println("The word was: " + word);
			addYouWin();
		}

	}

	/*adds a you win image of link and zelda and prompts you to play again
	 */
	private void addYouWin() {
		GImage youWin = new GImage("youwin.jpg"); 
		youWin.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.add(youWin, 0, 0);
		GLabel winner = new GLabel ("You Win! Play again?");
		winner.setFont("Courrier-36");
		winner.setColor(Color.GREEN);
		canvas.add(winner, canvas.getWidth() / 2.0 - winner.getWidth() / 2.0, canvas.getHeight() / 2.0);
	}


	/*removes link from the canvas
	 */

	private void removeLink() {
		GObject link = canvas.getElementAt(canvas.getWidth() / 2.0, canvas.getHeight() / 2.0);
		canvas.remove(link);
	}

	/*adds an image of ganon and a you lose label that prompts you to play again
	 */
	private void addYouLose() {
		GImage youLose = new GImage("youloseganon.jpg"); 
		youLose.setSize(canvas.getWidth(), canvas.getHeight());
		canvas.add(youLose, 0, 0);
		GLabel loser = new GLabel ("You Lose! Play again?");
		loser.setFont("Courrier-36");
		loser.setColor(Color.RED);
		canvas.add(loser,canvas.getWidth() / 2.0 - loser.getWidth() / 2.0, canvas.getHeight() / 2.0);
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
	/* prompts a yes or no response from the player to see if they would like to 
	 * play hangman again*/

	private boolean promptToPlayAgain() {
		println("Would you like to play again? Y/N");
		while (true) {
			String response = readLine("Y/N? :");
			if(response.length() != 1 || !Character.isLetter(response.charAt(0))) {
				println("Illegal answer please enter your answer");
			} else{
				response = response.toUpperCase();
				if (response.equals("Y") || response.equals("N")){
					if (response.equals("Y")) {
						return true;
					} else {
						return false;
					}
				} else {
					println("Illegal answer please enter your answer");
				}
			}
		}
	}
}
