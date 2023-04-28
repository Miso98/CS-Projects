/* name: Mitchell So
 * SL: Ashley Taylor
 * File: FacePamphletProfile.java
 * ------------------------------
 * This class keeps track of all the information for one profile
 * in the FacePamphlet social network.  Each profile contains a
 * name, an image (which may not always be set), a status (what 
 * the person is currently doing, which may not always be set),
 * and a list of friends. You can get the friends List which is an array list get the name,
 * image file name,  and status which are strings set status and imagefile name which are strings
 * and you can add and remove friends add friend is a boolean that will remove the friend
 * for your friends list then return true and return false if not already friends
 */

import java.util.*;

public class FacePamphletProfile {


	/** 
	 * Constructor
	 * This method takes care of any initialization needed for
	 * the profile.
	 */

	private String name;
	private String status;
	private String imgFileName;
	private ArrayList<String>friends;

	public FacePamphletProfile(String name) {
		friends = new ArrayList<String>();
		this.name = "";
		status = "";
		imgFileName = "";
	}

	/** This method returns the name associated with the profile. */ 
	public String getName() {
		return name;
	}

	/** this method returns the name of the image file associated with the profile. */
	public String getImageFileName() {
		return imgFileName;
	}

	/** This method sets the image associated with the profile. */ 
	public void setImageFileName(String fileName) {
		imgFileName = fileName;
	}

	/** 
	 * This method returns the status associated with the profile.
	 * If there is no status associated with the profile, the method
	 * returns the empty string ("").
	 */ 
	public String getStatus() {
		return status;
	}

	/** This method sets the status associated with the profile. */ 
	public void setStatus(String status) {
		this.status = status;
	}

	/** 
	 * This method adds the named friend to this profile's list of 
	 * friends.  It returns true if the friend's name was not already
	 * in the list of friends for this profile (and the name is added 
	 * to the list).  The method returns false if the given friend name
	 * was already in the list of friends for this profile (in which 
	 * case, the given friend name is not added to the list of friends 
	 * a second time.)
	 */
	public boolean addFriend(String friend) {
		if (friends.contains(friend)){
			return false;
		} else {
			friends.add(friend);
			return true;
		}

	}

	/** 
	 * This method removes the named friend from this profile's list
	 * of friends.  It returns true if the friend's name was in the 
	 * list of friends for this profile (and the name was removed from
	 * the list).  The method returns false if the given friend name 
	 * was not in the list of friends for this profile (in which case,
	 * the given friend name could not be removed.)
	 */
	public boolean removeFriend(String friend) {
		if (friends.contains(friend)){
			friends.remove(friend);
			return true;
		} else {
			return false;
		}
	}

	/** 
	 * This method returns an iterator over the list of friends 
	 * associated with the profile.
	 */ 
	public ArrayList<String> getFriends() {
		return friends;
	}

}
