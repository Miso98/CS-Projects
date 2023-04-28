/* 
 * File: FacePamphlet.java
 * -----------------------
 * When it is finished, this program will implement a basic social network
 * management system.
 */

import acm.program.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class ChatClient extends ConsoleProgram{

	private static final String HOST = "http://localhost:8080/";

	private String userName = "";
	private JTextField msgField = new JTextField(15);
	private int currMessageIndex = 0;

	public void init() {
		setFont("Courier-24");
		add(new JLabel("Message: "), SOUTH);
		add(msgField, SOUTH);
		add(new JButton("Send"), SOUTH);
		add(new JButton("Refresh"), SOUTH);
		addActionListeners();
	}
	
	public void run() {
		userName = readLine("Enter username: ");
		loadMessages();
	}

	public void actionPerformed(ActionEvent actionEvent) {
		String buttonStr = actionEvent.getActionCommand();
		if(buttonStr.equals("Send")) {
			send();
		} 
		if(buttonStr.equals("Refresh")) {
			loadMessages();
		}
		
	}

	private void send() {
		try {
			Request request = new Request("newMsg");
			String msg = userName + ": " + msgField.getText();
			request.addParam("msg", msg);
			SimpleClient.makeRequest(HOST, request);
			loadMessages();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void loadMessages() {
		try {
			Request request = new Request("getMsgs");
			request.addParam("index", "" +currMessageIndex);
			String str = SimpleClient.makeRequest(HOST, request);
			ArrayList<String> newMessages = makeListFromString(str);
			for(String msg : newMessages) {
				println("> " + msg);
			}
			currMessageIndex += newMessages.size();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> makeListFromString(String friendsStr) {
		ArrayList<String> friends = new ArrayList<String>();
		String raw = friendsStr.substring(1, friendsStr.length() - 1);
		String[] parts = raw.split(",");
		for(String part : parts) {
			String str = part.trim();
			if(!str.isEmpty()){
				friends.add(str);
			}
		}
		return friends;
	}


}
