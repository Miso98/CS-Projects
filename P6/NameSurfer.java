/*
 * File: NameSurfer.java
 * ---------------------
 * When it is finished, this program will implements the viewer for
 * the baby-name database described in the assignment handout.
 */

import acm.program.*;
import java.awt.event.*;
import javax.swing.*;
import acm.util.*;
import java.util.*;

public class NameSurfer extends Program implements NameSurferConstants {

	/* Method: init() */
	/**
	 * This method has the responsibility for reading in the data base
	 * and initializing the interactors at the top of the window.
	 */

	private JTextField nameField;
	private JButton graphButton;
	private JButton clearButton;
	private NameSurferGraph graph;
	private NameSurferDataBase dataBase;
	private NameSurferEntry entry;

	public void init() {
		dataBase =  new NameSurferDataBase(NAMES_DATA_FILE);
		addNameSearch();
	}

	private void addNameSearch() {
		graph = new NameSurferGraph();
		add(graph);
		nameField = new JTextField(25);
		JLabel name = new JLabel("Name");
		nameField.setActionCommand("Graph"); 
		add(name, NORTH);
		add(nameField, NORTH);
		addButtons();
		nameField.addActionListener(this);
		addActionListeners();

	}
/*adds the buttons for the namesurfer graph
 */
	private void addButtons() {
		graphButton = new JButton("Graph");
		clearButton = new JButton("Clear");
		add(graphButton, NORTH);
		add(clearButton, NORTH);

	}

	/* Method: actionPerformed(e) */
	/**
	 * This class is responsible for detecting when the buttons are
	 * clicked, so you will have to define a method to respond to
	 * button actions.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Clear")){
			graph.clear();
		}


		if (e.getActionCommand().equals("Graph")){
			String text = nameField.getText();
			entry = dataBase.findEntry(text);
			if (entry !=null){
				graph.addEntry(entry);
				graph.update();
			}
		}
	}
}
