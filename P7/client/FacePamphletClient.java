/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system. I didn't get to finish this because i was pressed for time. everything is due at once
 * and I just wanted to finish on a good note and try to balance out the checks i've gotten for style
 * on past assignments and its a bit frustrating because i really think CS is fun and i'd want to do stuff like this 
 * but because it's just so much of a time crunch and i'm trying to do well in all my other classes 
 * i feel like i haven't had the opportunity to really give it my all and i'm worried about my grade because 
 * i don't feel confident in the final so this is all i can really do right now. It's 2:30am and tomorrow i should probably just prioritize 
 * studying for the actual exam at that point.  So yeah tl;dr. I tried it looked like fun. but no time. I worry about the grade
 * enough to try to do extra credit but not enough to miss out on potential studying. I know it doesn't work 
 * but ¯\_(ツ)_/¯ what can ya do. Thanks for an awesome quarter though 
 */

import acm.program.*;
import acm.graphics.*;

import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class FacePamphletClient extends GraphicsProgram 
implements FacePamphletConstants {

	private static final String HOST = "http://localhost:8000/";
	private JTextField statusBar;
	private JTextField changePicture;
	private JTextField friendAdd;
	private JTextField searchBar;
	
	/** 
	 * Init is called before the window is created 
	 */


	public void init() {
		JLabel name = new JLabel("Name");
		add(name, NORTH);
		searchBar = new JTextField(TEXT_FIELD_SIZE);
		add(searchBar, NORTH);
		addTopButtons();
		addSideBar();
		addActionListeners();
	}

	private void addSideBar() {
		statusBar = new JTextField(TEXT_FIELD_SIZE);
		changePicture = new JTextField(TEXT_FIELD_SIZE);
		friendAdd = new JTextField(TEXT_FIELD_SIZE);
		JButton changeStatus = new JButton("Change Status");
		JButton changePictureButton = new JButton("Change Picture");
		JButton addFriendButton = new JButton("Add Friend");
		add(statusBar, WEST);
		add(changeStatus, WEST);
		add(changePicture, WEST);
		add(changePictureButton, WEST);
		add(friendAdd, WEST);
		add(addFriendButton, WEST);

	}

	private void addTopButtons() {
		JButton add = new JButton("Add");
		add(add, NORTH);
		JButton delete = new JButton("Delete");
		add(delete, NORTH);
		JButton lookup = new JButton("Lookup");
		add(lookup, NORTH);

	}

	/** 
	 * Run is called after the window is created 
	 */
	public void run() {
		// your code here
		pingTheServer();

	}

	/**
	 * This method is called when a button is clicked.
	 */
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		println(cmd);
		if(cmd.equals("Change Status")){
			changeStatus();
		} else if(cmd.equals("Change Picture")){
			changePicture();
		} else if (cmd.equals("Add Friend")){
			addFriend();
		} else if (cmd.equals("Add")){
			addProfile();
		} else if (cmd.equals("Delete")){
			deleteProfile();
		} else if (cmd.equals("Lookup")){
			lookupProfile();
		}
	}
	
	
	private void lookupProfile() {
		try {
			Request request = new Request ("lookup");
			String name = searchBar.getText();
			request.addParam("containsProfile", name);
			SimpleClient.makeRequest(HOST, request);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void deleteProfile() {
		try {
			Request request = new Request ("delete");
			String name = searchBar.getText();
			request.addParam("deleteProfile", name);
			SimpleClient.makeRequest(HOST, request);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void addProfile() {
		try {
			Request request = new Request ("add");
			String name = searchBar.getText();
			request.addParam("addProfile", name);
			SimpleClient.makeRequest(HOST, request);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void addFriend() {
		try{
			Request request = new Request("addNewFriend");
			String friend = friendAdd.getText();
			request.addParam("addFriend", friend);
			SimpleClient.makeRequest(HOST, request);
		} catch (IOException e){
			e.printStackTrace();
		}
	}

	private void changeStatus() {
		try {
			Request request = new Request ("newStatus");
			String status = statusBar.getText();
			request.addParam("setStatus", status);
			SimpleClient.makeRequest(HOST, request);
		} catch (IOException e){
			e.printStackTrace();
		}
	}
	
	private void changePicture() {
		try {
			Request request = new Request ("changePicture");
			String imgFile = changePicture.getText();
			request.addParam("setImgFileName", imgFile);
			SimpleClient.makeRequest(HOST, request);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * This method is an example of sending a request to the server.
	 * You can delete it when you are ready.
	 */
	private void pingTheServer() {
		try {
			// Lets prepare ourselves a new request with command "ping".
			Request example = new Request("ping");
			// We are ready to send our request
			String result = SimpleClient.makeRequest(HOST, example);
			drawCenteredLabel(result);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Again, this is a helper method that we wrote for the "pingTheServer"
	 * example (above). You should not include it in your final submission
	 */
	private void drawCenteredLabel(String text) {
		GLabel label = new GLabel(text);
		label.setFont("courier-24");
		double x = (getWidth() - label.getWidth())/2;
		double y = (getHeight() - label.getAscent())/2;
		add(label, x, y);
	}

}
