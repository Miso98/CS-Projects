/*name: Mitchell So
 * section leader: Ashley Taylor
 * File: Yahtzee.java
 * ------------------
 * This program runs the game of Yahtzee where you roll 5 dice and reroll 
 * up to two times and try to fill up the different categories and get the highest possible score
 * can be played with up to 4 players now you can get a bonus for getting multiple yahtzees also an airhorn plays
 * when you get a yahtzee based on the number of yahtzees you got also there is a background music remix
 * of the nintendo 64 classic dreamland  for Kirby
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.applet.AudioClip;
import java.awt.event.*;



public class YahtzeeExtensions extends GraphicsProgram implements YahtzeeConstants {
	AudioClip airHorn = MediaTools.loadAudioClip("air horn (club sample).au");
	AudioClip bgMusic = MediaTools.loadAudioClip("dreamland.au");
	public static void main(String[] args) {
		new YahtzeeExtensions().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		nPlayers = dialog.readInt("Enter number of players");
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}
	/* initializes the entire score card matrix with number of categories and players
	 * then creates a new matrix of the same size but initialized with all -1's because the score
	 * cannot be negative then running through all the players and all the possible categories
	 * the game is played by prompting to roll dice rolling and rerolling then selecting categories and 
	 * then calculating the score then the upper and lower score are updated and the total score is updated and a winner is chosen
	 * 
	 */
	private void playGame() {
		bgMusic.play();
		scoreCard = new int [N_CATEGORIES][nPlayers];
		categoriesChosen = new int [N_CATEGORIES][nPlayers];
		for (int k = 0; k < N_CATEGORIES; k++){
			for (int l = 0; l < nPlayers; l++){
				categoriesChosen[k][l] = -1;
			}
		}
		for (int j = 1; j <= N_SCORING_CATEGORIES; j++){
			for (int i = 1; i <= nPlayers; i++){
				display.printMessage(playerNames[i - 1] + "'s turn. Click the \"Roll Dice\" button to roll the dice.");
				display.waitForPlayerToClickRoll(i);
				firstRoll();
				secondAndThirdRolls();
//				cheatRoll(1,1,1,1,1); // check yahtzee air horns using this method
				display.printMessage("Select a category for this roll");
				int category = display.waitForPlayerToSelectCategory();			
				checkCategory(category, i);
			}
		}
		updateUpperandLowerScore();
	}

	/* this method provides the second and third roll method by prompting to re roll waiting for 
	 * the player to select the dice to be rerolled then rolls the selected dice again runs through the foor loop twice
	 */

	private void secondAndThirdRolls() {
		for (int i = 0; i < 2; i++){
			display.printMessage("Select the dice you wish to re-roll and click \"Roll Again\"");
			display.waitForPlayerToSelectDice();
			nextRoll();	
		}
	}

	/*updates the upper and lower scores for each player the upper score is calculated by all the upper categories
	 * which are the ones through the sixes if the upper score is greater than 63 then a bonus of 35 points is awarded
	 * the lower score is calculated by summing the sections from Three of a kind to chance. the upper and lower score 
	 * are then summed to get the total score which is then updated
	 * 
	 */

	private void updateUpperandLowerScore() {

		//for loop through the players
		for (int i = 1; i <= nPlayers; i++){
			int upperScore = 0;
			int lowerScore = 0;
			int upperBonus = 0;
			for  (int j = 0; j < SIXES; j++){
				upperScore += scoreCard[j][i - 1 ];
			}
			for (int j = THREE_OF_A_KIND - 1; j <LOWER_SCORE; j++ ){
				lowerScore += scoreCard[j][i - 1];
			}
			display.updateScorecard(UPPER_SCORE, i , upperScore);
			display.updateScorecard(LOWER_SCORE, i , lowerScore);
			if (upperScore >= 63){
				upperBonus = 35;
				display.updateScorecard(UPPER_BONUS, i, upperBonus);
			} else {
				upperBonus = 0;
				display.updateScorecard(UPPER_BONUS, i, upperBonus);
			}
			int scoreTotal = 0;
			scoreTotal = lowerScore + upperScore + upperBonus;
			scoreCard[TOTAL - 1][i - 1] = scoreTotal; //added: updates score total in the array itself
			display.updateScorecard(TOTAL, i, scoreTotal);
		}
		chooseWinner();

		/* this method chooses the winner with the highest score and then returns the player number the player number 
		 * by replacing the next number in the totals only if is higher and returning the player's position
		 * 	
		 */
	}

	private void chooseWinner() {
		int scoreMax = -1;
		int bestPlayer = 0;
		for ( int i = 0; i < nPlayers; i++){
			if (scoreCard[TOTAL - 1 ][i] > scoreMax){ //index 16 is just 0! This means that somewhere, the 
				scoreMax = scoreCard[TOTAL - 1][i];
				bestPlayer = i;
			}
		}
		display.printMessage("Congratulations, " + playerNames[bestPlayer] + ", you're the winner with a total score of " + scoreMax);

	}
	/* this method checks each category and gets the score associated with the dice at hand for each category
	 * this then updates the score card. because the card was initialized with -1's this accounts for double 
	 * selecting because if the category is selected before hand the number will always be greater than or equal to 0
	 * so having a value of negative 1 would indicated not having been selected before 
	 * 
	 */

	private void checkCategory(int category, int player) { 
		if (category != YAHTZEE) {
			while(categoriesChosen[category - 1][player - 1] != -1) {
				display.printMessage("You already picked that category. Please choose another category.");
				category = display.waitForPlayerToSelectCategory();
			}
		}
		int score = 0;
		for (int i = ONES; i <= SIXES; i++){
			if (category == i) {
				score = getScore(i);
				display.updateScorecard(i, player, score);
			} 
		}
		if (category == THREE_OF_A_KIND){
			if (checkCategoryKinds(3)){
				score = getScore();
			}
			display.updateScorecard(THREE_OF_A_KIND, player, score);
		} else if (category == FOUR_OF_A_KIND){
			if (checkCategoryKinds(4)){
				score = getScore();
			} 
			display.updateScorecard(FOUR_OF_A_KIND, player, score);
		} else if(category == FULL_HOUSE){
			if (checkFullHouse()){
				score = 25; 
			}
			display.updateScorecard(FULL_HOUSE, player, score);
		} else if (category == SMALL_STRAIGHT){
			if (checkSmallStraight()){
				score = 30;
			}
			display.updateScorecard(SMALL_STRAIGHT, player, score);
		} else if (category == LARGE_STRAIGHT){
			if (checkLargeStraight()){
				score = 40;
			}
			display.updateScorecard(LARGE_STRAIGHT, player, score);
		} else if (category == YAHTZEE) {
			if(checkYahtzee()){
				if (anotherYahtzee(player)) {
					score = 100; //increments by 100 points
				} else {
					score = 50;
				}
			} 
			int numberPlays = (scoreCard [category - 1][player - 1] + score + 50) / 100;
			for (int i = 0; i < numberPlays; i++){
				airHorn.play();
				pause(500);
			}
			display.updateScorecard(YAHTZEE, player, scoreCard [category - 1][player - 1] + score);
		} else if (category == CHANCE){
			score = getScore();
			display.updateScorecard(CHANCE, player, score);
		} 
		scoreCard [category - 1][player - 1] += score; //minus 1 because they are not zero-indexed
		categoriesChosen [category -1][player -1] = score;
		updateScoreCardDisplay(player);
	}
	
	private boolean anotherYahtzee(int player) {
		//would need to just check the Yahtzee spot in the scoreCard
		return scoreCard[YAHTZEE - 1][player -1] != 0;
	}

	/* this method updates the scoreCard display by totaling up the score in all of the categories
	 * 
	 */
	private void updateScoreCardDisplay(int player) {
		int scoreTotal = 0;
		for  (int i = 0; i < N_CATEGORIES; i++){
			scoreTotal += scoreCard[i][player - 1];
		}
		display.updateScorecard(TOTAL, player, scoreTotal);
	}

	/*this private boolean method checks whether the dice rolled is a yahtzee by making sure that each 
	 * succeeding die is the same as the previous one
	 * and returning false if not
	 */
	private boolean checkYahtzee() {
		for( int i = 0; i < dice.length - 1; i++){
			if (dice [i] != dice [i + 1]){
				return false;
			}
		}
		return true;
	}
	/*this method checks large straights by first creating an array with the two possible large straights
	 * then checking each of the dice rolled with each of the straights and if there is a match turning that matched number
	 * into the precreated combo into a 0. then after all the numbers in both arrays have been compared
	 * the one that was being transformed is then compared to an array of 0's and if they are equivalent then there 
	 * is a large straight present
	 * 
	 */

	private boolean checkLargeStraight() {
		int [] largeStraightCombo1 = new int [] {1, 2, 3, 4, 5};
		int [] largeStraightCombo2 = new int [] {2, 3, 4, 5, 6};
		int [] largeStraightCheck = new int [] {0, 0, 0, 0, 0};
		for (int i = 0; i < dice.length; i++){
			for (int j = 0; j < dice.length; j++){
				if( largeStraightCombo1[i] == (dice[j])){
					largeStraightCombo1[i] = 0;
				}
			}
		}
		for (int i = 0; i < dice.length; i++){
			for (int j = 0; j < dice.length; j++){
				if(largeStraightCombo2[i] == dice[j]){
					largeStraightCombo2[i] = 0;
				}
			}
		}
		if (Arrays.equals(largeStraightCombo1,largeStraightCheck) || Arrays.equals(largeStraightCombo2,largeStraightCheck)){
			return true;
		} else {
			return false;
		}
	}
	/*this method checks small straights by first creating an array with the three possible small straights
	 * then checking each of the dice rolled with each of the straights and if there is a match turning that matched number
	 * into the precreated combo into a 0. then after all the numbers in both arrays have been compared
	 * the one that was being transformed is then compared to an array of 0's and if they are equivalent then there 
	 * is a large straight present
	 */
	private boolean checkSmallStraight(){
		int [] smallStraightCombo1 = new int [] {1, 2, 3, 4};
		int [] smallStraightCombo2 = new int [] {2, 3, 4, 5};
		int [] smallStraightCombo3 = new int [] {3, 4, 5, 6};
		int [] smallStraightCheck = new int [] {0, 0, 0, 0};
		for (int i = 0; i < dice.length; i++){
			for (int j = 0; j < smallStraightCheck.length; j++){
				if(dice[i] == smallStraightCombo1[j]){
					smallStraightCombo1[j] = 0;
				}
			}
		}
		for (int i = 0; i < dice.length; i++){
			for (int j = 0; j < smallStraightCheck.length; j++){
				if(dice[i] == smallStraightCombo2[j]){
					smallStraightCombo2[j] = 0;
				}
			}
		}
		for (int i = 0; i < dice.length; i++){
			for (int j = 0; j < smallStraightCheck.length; j++){
				if(dice[i] == smallStraightCombo3[j]){
					smallStraightCombo3[j] = 0;
				}
			}
		}
		if(Arrays.equals(smallStraightCombo1,smallStraightCheck) || Arrays.equals(smallStraightCombo2, smallStraightCheck) || Arrays.equals(smallStraightCombo3, smallStraightCheck)){
			return true;
		} else {
			return false;
		}
	}
	/*this checks the three of a kind and four of a kind by checking the dice rolled and creating a counter so that each
	 * new value is assigned into a new counter array and if for any number the counter is greater than the number you want 
	 * which is specified to be 3 or 4 then this method will return true and the score will be calculated as such which is essentially
	 * just the sum of all the dice
	 */
	private boolean checkCategoryKinds(int totalNeeded) {
		int [] numberCounter = new int [6]; 
		for (int i = 0; i < dice.length; i++){
			numberCounter[dice[i] - 1]++; //subtracting one because of zero-indexing
		}
		for (int i = 0; i < numberCounter.length; i++){
			if (numberCounter[i] >= totalNeeded){
				return true;
			}
		}
		return false;
	}
	/* this checks to see if the dice are a full house by creating a dice number counter
	 * which essentially adds one each time the dice are iterated through and see the same value again
	 * and if at any point in the counter array there exists a three we then check for a two and if that exists then 
	 * this a full house
	 * 
	 */
	private boolean checkFullHouse(){
		int [] numberCounter = new int [6]; 
		for (int i = 0; i < dice.length; i++){
			numberCounter[dice[i] - 1]++;
		}
		for (int i = 0; i < numberCounter.length; i++){
			if ((numberCounter[i] == 3)){
				for (int j = 0; j < numberCounter.length; j ++){
					if((numberCounter[j] == 2)){
						return true;
					}
				}
			}
		}
		return false;
	}
	/* this gets the value of the categories for three of a kind and four of a kind and chance
	 * by summing up all the dice values to get the score
	 * 
	 */
	private int getScore() {
		int score = 0;
		for (int i = 0; i < dice.length; i++) {
			score += dice[i]; //increments score 
		}
		return score;
	}
	/* the kinds of dice in the upper half by checking to see if the dice rolled
	 * contain the value that you selected and then summing up the number of those dice that are selected
	 * and adding them to the score 
	 * 
	 * 
	 */
	private int getScore(int chosenNumber) {
		int score = 0;
		for (int i = 0; i < dice.length; i++) {
			if (dice[i] == chosenNumber) {
				score += dice[i]; //increments score 
			}
		}
		return score;
	}
	/* rolls and assigns new random numbers to the dice that are selected and then displays the new dice but keeps the old
	 * dice that were not selected the same value
	 * 
	 */
	private void nextRoll() {
		for (int i = 0; i < N_DICE; i++){
			if (display.isDieSelected(i) == true){
				dice[i] = rgen.nextInt(1,6);
			} else {
				dice[i] = dice[i];
			}
		}
		display.displayDice(dice);
	}
	/*creates an array of dice and rolls random numbers between one and 6 and assigns all the values to the array
	 * displays the dice and the numbers rolled
	 * 
	 */
	private void firstRoll() {
		dice = new int [N_DICE];
		for (int i = 0; i < N_DICE; i++){
			int roll = rgen.nextInt(1,6);
			dice[i] = roll;
		}
		display.displayDice(dice);
	}


	/* private void that let me choose what dice i rolled used only for testing category checks
	 * 
	 */
	private void cheatRoll(int a, int b, int c, int d, int e) {
		dice = new int[] {a, b, c, d, e};
		display.displayDice(dice);
	}

	/* Private instance variables */
	private int nPlayers;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[] dice;
	private int [][] scoreCard;
	int [][] categoriesChosen;

}
