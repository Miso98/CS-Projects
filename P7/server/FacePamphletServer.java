/* Name: Mitchell So
 * SL: Ashley Taylor
 * File: FacePamphletServer.java
 * ------------------------------
 * This program runs a server which hosts the data for a 
 * FacePamphlet internet application. The server stores all 
 * of the data and contains the logic for creating, deleting 
 * profiles and getting and setting profile properties. When 
 * the server receives a requests (which often come from the 
 * client), it updates its internal data, and sends back a string. WHen a request 
 * is sent to the server multiple different outcomes are possible and accounted for 
 * with given error messages given circumstances presented in the request sent
 */

import java.util.*;
import acm.program.*;

public class FacePamphletServer extends ConsoleProgram 
implements SimpleServerListener {

	/* The internet port to listen to requests on */
	private static final int PORT = 8000;

	/* The server object. All you need to do is start it */
	private SimpleServer server = new SimpleServer(this, PORT);

	private HashMap<String, FacePamphletProfile> profileMap = new HashMap<String, FacePamphletProfile>(); 

	/**
	 * Starts the server running so that when a program sends
	 * a request to this computer, the method requestMade is
	 * called.
	 */
	public void run() {
		println("Starting server on port " + PORT);
		server.start();
	}

	/**
	 * When a request is sent to this computer, this method is
	 * called. It must return a String.
	 */
	public String requestMade(Request request) {
		String cmd = request.getCommand();
		println(request.toString());
		if(cmd.equals("ping")){
			return "Hello, internet";
		} else if (cmd.equals("addFriend")){
			return addFriend(request);
		} else if (cmd.equals("addProfile")){
			return addProfile(request);
		} else if (cmd.equals("containsProfile")){
			return containsProfile(request);
		} else if (cmd.equals("deleteProfile")){
			return deleteProfile(request);
		} else if (cmd.equals("getStatus")){
			return getStatus(request);
		} else if (cmd.equals("setStatus")){
			return setStatus(request);
		} else if (cmd.equals("getImgFileName")){
			return getImgFileName(request);
		} else if (cmd.equals("setImgFileName")){
			return setImgFileName(request);
		} else if (cmd.equals("getFriends")){
			return getFriends(request);
		}
		return "Error: Unknown command " + cmd + ".";
	}

	/*this method takes in the request and then gets the name parameter from the request
	 * if the profile map has a facepamphletprofile value with the key of that name then we return the friends list
	 * which is an arraylist of strings with the friends of that name's profile. that arrayList is turned into a string
	 * using the toString method and the arrayList is goten using the getFriends method. if no profile is associated with the name
	 * then an error string is returned 
	 */

	private String getFriends(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			FacePamphletProfile profile = profileMap.get(name);
			ArrayList<String> friendsList = profile.getFriends();
			return friendsList.toString();
		} else {
			return "Error: Database does not contain profile with name " + name; 
		}
	}

	/*this method takes in the request and then gets the name parameter from the request
	 * if there is a profile associated with that name which is checked by looking to see
	 * if there is a facepamphletprofile value associated with the name key then the 
	 * string filename is set as the filename parameter from the request 
	 * and the profile's image file name is then set as file name and the success string is returned
	 * otherwise if there is no profile associated with the name an error message is returned  
	 */

	private String setImgFileName(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			FacePamphletProfile profile = profileMap.get(name);
			String fileName = request.getParam("fileName");
			profile.setImageFileName(fileName);
			return "success";
		} else {
			return "Error: Database does not contain profile with name " + name; 
		}
	}

	/*this method takes in the request the gets the name parameter. Then checks to see if the 
	 * name has a value in the hashmap of profiles. if the map has a profile with that name then 
	 * the String status is assigned as the status parameter from the request and then the profile
	 * associated with the name has its status set to status. The success string is then returned otherwise
	 * an error message will be returned if no person exists
	 */

	private String setStatus(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			FacePamphletProfile profile = profileMap.get(name);
			String status = request.getParam("status");
			profile.setStatus(status);
			return "success";
		} else {
			return "Error: Database does not contain profile with name " + name; 
		}
	}

	/* This method takes in the request. then gets the name parameter from the request
	 * checks to see if the name is in the hashmap of profiles and if it is
	 * this method will return a string of the status of the profile
	 *  if the profile doesn't exist the method will return an error message. 
	 *  Will return the empty string if no status string available in 
	 * a current existing profile
	 */


	private String getStatus(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			return profileMap.get(name).getStatus();
		} else {
			return "Error: Database does not contain profile with name " + name; 
		}
	}


	/*this method takes the request and then gets the name parameter. then the name is checked
	 * to see if a profile exists within the hashmap with the name as the key. 
	 *  if a profile exists then we will call that profile "profile"
	 * for each name in the friends list of profile, the profile is removed from each of those friends' friends list
	 * then the profile is removed from the hashmap and returning the string success otherwise an error string 
	 * is returned if the name does not return a profile 
	 * 
	 */

	private String deleteProfile(Request request) {
		String name = request.getParam("name");
		if (profileMap.containsKey(name)){
			FacePamphletProfile profile = profileMap.get(name);
			for (String friend : profile.getFriends()){
				FacePamphletProfile otherFriend = profileMap.get(friend);
				otherFriend.removeFriend(name);
			}
			profileMap.remove(name);
			return "success";
		} else {
			return "Error: Database does not contain profile with name " + name; 
		}
	}

	/*this method takes in the request and gets the name parameter. the name is then checked to see if a profile 
	 * exists within the profile hashmap and if there is a profile then "true" is returned if not then "false" 
	 * is returned
	 */
	private String containsProfile(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			return "true";
		} else {
			return "false";
		}
	}

	/*this method takes in the request and gets the name parameter. The name is then checked to see 
	 * if there is a profile that exists under that name and if there is the method returns an error message
	 * if there is not, the method creates a new profile and puts the new profile into the 
	 * profileMap with key name and value with the new profile and returns the string success
	 */
	private String addProfile(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			return "Error: Database already contains a profile with name " + name; 
		} else {
			FacePamphletProfile profile = new FacePamphletProfile(name);
			profileMap.put(name, profile);
			return "success";
		}
	}

	/*this method takes in the request and from the request gets the two parameters name 1 and name 2
	 * then if the map contains name1  and name 2 and name1 and name 2 aren't the same
	 * profile 1 and profile 2 are added. the string success is then added
	 *  if the two profiles fail to add then they are already friends
	 *  and an error is returned if either one of the names doesn't exist or are the same 
	 *  then another error message will show up. Method will return success if the friends are added
	 *  and an error message if one of the friends doesn't exists, the friends are the same person or are already friends
	 */

	private String addFriend(Request request) {
		String name1 = request.getParam("name1");
		String name2 = request.getParam("name2");
		if(profileMap.containsKey(name1) && profileMap.containsKey(name2) && !(name1.equals(name2))){
			FacePamphletProfile profile1 = profileMap.get(name1);
			FacePamphletProfile profile2 = profileMap.get(name2);
			if (profile1.addFriend(name2) && profile2.addFriend(name1)){
				return "success";
			} else {
				return "Error: " + name1 + "and" + name2 +" are already friends";
			}
		} else {
			return "Error: one of the named people doesn't exist or are the same person";
		}
	}

	/* This method takes in the request. then gets the name parameter from the request
	 * checks to see if the name is in the hashmap of profiles and if it is
	 * this method will return a string of the imagefilename of the profile if the profile doesn't exist
	 * the method will return an error message. Will return the empty string if no profileimage string available in 
	 * a current existing profile
	 * 
	 */
	private String getImgFileName(Request request) {
		String name = request.getParam("name");
		if(profileMap.containsKey(name)){
			FacePamphletProfile profile = profileMap.get(name);
			String profileImageName = profile.getImageFileName();
			return profileImageName;
		} else {
			return "Error: Database does not contain profile with name " + name; 
		}
	}
}
