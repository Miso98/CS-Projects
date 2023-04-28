/*
 * File: NameSurferGraph.java
 * ---------------------------
 * This class represents the canvas on which the graph of
 * names is drawn. This class is responsible for updating
 * (redrawing) the graphs whenever the list of entries changes
 * or the window is resized.
 */

import acm.graphics.*;
import java.awt.event.*;
import java.util.*;
import java.awt.*;

public class NameSurferGraph extends GCanvas
implements NameSurferConstants, ComponentListener {
	private ArrayList <NameSurferEntry> nameEntries = new ArrayList <NameSurferEntry>();
	private final static int NUM_COLORS = 4;
	/**
	 * Creates a new NameSurferGraph object that displays the data.
	 */

	public NameSurferGraph() {
		addComponentListener(this);
	}


	/**
	 * Clears the list of name surfer entries stored inside this class.
	 */
	public void clear() {
		nameEntries.clear();
		update();
	}


	/* Method: addEntry(entry) */
	/**
	 * Adds a new NameSurferEntry to the list of entries on the display.
	 * Note that this method does not actually draw the graph, but
	 * simply stores the entry; the graph is drawn by calling update.
	 */
	public void addEntry(NameSurferEntry entry) {
		nameEntries.add(entry);
	}


	/**
	 * Updates the display image by deleting all the graphical objects
	 * from the canvas and then reassembling the display according to
	 * the list of entries. Your application must call update after
	 * calling either clear or addEntry; update is also called whenever
	 * the size of the canvas changes.
	 */

	public void update() {
		removeAll();
		addGraph();
	}

	/*adds the graph by creating the graph lines 
	 * adding the years labels of the graph then creating the actual line graph from the data 
	 * 
	 */
	private void addGraph() {
		addLines();
		addYears();
		makeGraph();
	}

	/* creates a graph by making lines from each of the rank numbers of the name entry entered in colors 
	 * each line in a cycle of 4, black, red, blue then magenta, then back to black
	 * the graph is scaled to be inside of the margin boundaries and for names not ranked in the top 1000
	 * the name is put at the bottom and given * for the rank number rather than 0 which is in the array
	 * the lines are made for every decade using two rank values from the rank array for the y value the x value
	 * is evenly split up by the number of decades and is evenly spaced based on the width 
	 * 
	 */


	private void makeGraph() {
		int count = 0; //this is the counter to add different line colors
		Color[] colors = new Color[] {Color.BLACK, Color.RED, Color.BLUE, Color.MAGENTA};
		for (int i = 0; i < nameEntries.size(); i ++){
			double yScale = (getHeight() - 2 * GRAPH_MARGIN_SIZE)/(double) MAX_RANK; //must be casted as a double so that rounding error doesn't occur
			NameSurferEntry entry = nameEntries.get(i); 
			Color lineColor = colors[count % NUM_COLORS];
			for (int j = 0; j < NDECADES -1; j++){
				double x = getWidth()/(double)NDECADES * j;
				if (entry.getRank(j) != 0){ 
					double y = entry.getRank(j) * yScale + GRAPH_MARGIN_SIZE;
					double endY = 0;
					if (entry.getRank(j+1) != 0){ // makes sure that if the name is unranked in the top 1000
						// the graph will go to the bottom and not the top of the margin 
						endY = entry.getRank(j+1) * yScale + GRAPH_MARGIN_SIZE;
					} else {
						endY = getHeight() - GRAPH_MARGIN_SIZE;
					}
					GLine line = new GLine ( x, y,  x += getWidth()/NDECADES, endY);
					line.setColor(lineColor);
					add(line);
					GLabel labelName = new GLabel(entry.getName() + " " + entry.getRank(j));
					labelName.setColor(lineColor);
					add(labelName, (x - getWidth()/(NDECADES+1)), y );
				} else { // if the rank is not in the top 1000 the data sheet has it listed as 0 so instead of writing 0
					// i have the rank listed as * and the graph line at the bottom of the graph
					double y = getHeight() - GRAPH_MARGIN_SIZE;
					double endY = 0;
					if (entry.getRank(j+1) != 0){
						endY = entry.getRank(j+1) * yScale + GRAPH_MARGIN_SIZE; 
					} else if (entry.getRank(j+1) == 0){
						endY = getHeight() - GRAPH_MARGIN_SIZE;
					}
					GLine line = new GLine ( x, y,  x += getWidth()/NDECADES, endY);
					line.setColor(lineColor);
					add(line);
					GLabel labelName = new GLabel(entry.getName() + " *");
					labelName.setColor(lineColor);
					add(labelName, (x - getWidth()/(NDECADES+1)), y );
				}
			}
			addLastLabel(entry, i, yScale, lineColor); ////off by one index so need to add the last label
			count ++; //each time a graph is completely made a new color is used for the next one
		}

	}

	/*this adds the last label at the end of the graph on the last line with the right color 
	 * this needs to be a separate method because there is an off by one index that needs to be solved
	 * this will account for if the last name is not ranked in the top 1000 by adding a star instead of 0
	 * by the label name
	 */
	private void addLastLabel(NameSurferEntry entry, int i, double yScale, Color color) {
		if (entry.getRank(NDECADES -1) != 0){ //off by one index so need to add the last label
			GLabel lastLabel = new GLabel(entry.getName() + entry.getRank(NDECADES-1));
			lastLabel.setColor(color);
			add(lastLabel, getWidth() - getWidth()/(double)NDECADES,entry.getRank(NDECADES-1) * yScale + GRAPH_MARGIN_SIZE);
		} else {
			GLabel lastLabel = new GLabel(entry.getName() + " *");
			lastLabel.setColor(color);
			add(lastLabel, getWidth() - getWidth()/(double)NDECADES, getHeight() - GRAPH_MARGIN_SIZE);
		}
	}


	/*adds labels with each decade from 1900 to 2000 evenly spaced by the width of the screen divided
	 * by the number of decades
	 * 
	 */

	private void addYears() {
		// TODO Auto-generated method stub
		int decade = START_DECADE - 10; // off by one error it's so the decades start at 1900 
		for (int i = 0; i < NDECADES; i++){
			decade += 10;
			String decadeSign = Integer.toString(decade);
			GLabel printedDecade = new GLabel(decadeSign);
			add(printedDecade, i* (double)getWidth()/NDECADES, getHeight());
		}
	}

	/*adds lines for the graph scale evenly distributed by each decade and separated depending on the width
	 * of the program. the line length is equivalent to the height of the screen
	 * 
	 */
	private void addLines() {
		for (int i = 0; i < NDECADES; i++){
			double x = i * getWidth()/NDECADES;
			GLine lines = new GLine (x, 0, x, getHeight());
			add(lines);
		}
		GLine top = new GLine (0, GRAPH_MARGIN_SIZE, getWidth(), GRAPH_MARGIN_SIZE);
		GLine bottom = new GLine(0, getHeight() - GRAPH_MARGIN_SIZE, getWidth(), getHeight() - GRAPH_MARGIN_SIZE);
		add(top);
		add(bottom);
	}


	/* Implementation of the ComponentListener interface */
	public void componentHidden(ComponentEvent e) { }
	public void componentMoved(ComponentEvent e) { }
	public void componentResized(ComponentEvent e) { update(); }
	public void componentShown(ComponentEvent e) { }
}
