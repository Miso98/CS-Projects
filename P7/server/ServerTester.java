/* 
 * File: ServerTester.java
 * -----------------------
 * This program tests your server by sending a bunch of requests
 * and validating if the response is what it was expecting. Use
 * it to check if you have completed the assignment!
 */

import acm.program.*;
import acm.graphics.*;

import java.awt.Color;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;

public class ServerTester extends ConsoleProgram {

	/* The internet address of the computer running the server */
	private static final String HOST = "http://localhost:8000/";

	/* Run all of the tests */
	public void run() {

		// a welcome message
		println("Running the FacePamphlet server tester");
		println("Make sure that the server has just been restarted");
		println("-------");
		println("");
		
		// a ping request
		Request ping = new Request("ping");
		runTest(ping, false, "Hello, internet");

		// addProfile requests
		Request r1 = new Request("addProfile");
		r1.addParam("name", "Chris");
		runTest(r1, false, "success");
		
		Request r1b = new Request("addProfile");
		r1b.addParam("name", "Mehran");
		runTest(r1b, false, "success");
		
		Request r1c = new Request("addProfile");
		r1c.addParam("name", "Julie");
		runTest(r1c, false, "success");
		
		Request r1d = new Request("addProfile");
		r1d.addParam("name", "Julie");
		runTest(r1d, true, "");
		
		Request r2 = new Request("addProfile");
		r2.addParam("name", "Barbra Streisand");
		runTest(r2, false, "success");

		// continsProfile request
		Request c1 = new Request("containsProfile");
		c1.addParam("name", "Chris");
		runTest(c1, false, "true");
		
		Request c2 = new Request("containsProfile");
		c2.addParam("name", "Barbra Streisand");
		runTest(c2, false, "true");
		
		Request c3 = new Request("containsProfile");
		c3.addParam("name", "Voldemort");
		runTest(c3, false, "false");
		
		// deleteProfile requests
		Request d1 = new Request("deleteProfile");
		d1.addParam("name", "Voldemort");
		runTest(d1, true, "");
		
		Request d2 = new Request("addProfile");
		d2.addParam("name", "Beyonce Knowles");
		runTest(d2, false, "success");
		
		Request d3 = new Request("deleteProfile");
		d3.addParam("name", "Beyonce Knowles");
		runTest(d3, false, "success");
		
		Request d4 = new Request("containsProfile");
		d4.addParam("name", "Beyonce Knowles");
		runTest(d4, false, "false");
		
		// get and set status
		Request s1 = new Request("getStatus");
		s1.addParam("name", "Chris");
		runTest(s1, false, "");
		
		Request s2 = new Request("setStatus");
		s2.addParam("name", "Chris");
		s2.addParam("status", "testing");
		runTest(s2, false, "success");
		
		Request s3 = new Request("getStatus");
		s3.addParam("name", "Chris");
		runTest(s3, false, "testing");
		
		// get and set image file name
		Request i1 = new Request("getImgFileName");
		i1.addParam("name", "Chris");
		runTest(i1, false, "");
		
		Request i2 = new Request("setImgFileName");
		i2.addParam("name", "Chris");
		i2.addParam("fileName", "ChrisP.jpg");
		runTest(i2, false, "success");
		
		Request i3 = new Request("getImgFileName");
		i3.addParam("name", "Chris");
		runTest(i3, false, "ChrisP.jpg");
		
		// get and add friends
		Request f1 = new Request("getFriends");
		f1.addParam("name", "Chris");
		runTest(f1, false, "[]");
		
		Request f2 = new Request("addFriend");
		f2.addParam("name1", "Chris");
		f2.addParam("name2", "Mehran");
		runTest(f2, false, "success");
		
		Request f3 = new Request("getFriends");
		f3.addParam("name", "Chris");
		runTest(f3, false, "[Mehran]");
		
		Request f4 = new Request("getFriends");
		f4.addParam("name", "Mehran");
		runTest(f4, false, "[Chris]");
		
		Request f5 = new Request("addFriend");
		f5.addParam("name1", "Chris");
		f5.addParam("name2", "Julie");
		runTest(f5, false, "success");
		
		Request f5b = new Request("addFriend");
		f5b.addParam("name1", "Chris");
		f5b.addParam("name2", "Julie");
		runTest(f5b, true, "");
		
		Request f6 = new Request("getFriends");
		f6.addParam("name", "Chris");
		runTest(f6, false, "[Mehran, Julie]");
		
		Request f7 = new Request("deleteProfile");
		f7.addParam("name", "Julie");
		runTest(f7, false, "success");
		
		Request f8 = new Request("getFriends");
		f8.addParam("name", "Chris");
		runTest(f8, false, "[Mehran]");
		
		Request f9 = new Request("addFriend");
		f9.addParam("name1", "Chris");
		f9.addParam("name2", "Imaginary Friend");
		runTest(f9, true, "");
		
		Request f10 = new Request("addFriend");
		f10.addParam("name1", "Chris");
		f10.addParam("name2", "Chris");
		runTest(f10, true, "");
		
		// and what if we send a bad command?
		Request bad = new Request("badCommand");
		runTest(bad, true, "");
	}

	/**
    * Runs a request and checks if the result is what was expected (both whether I
    * expected an error and otherwise what String response I expected)
    */
	private void runTest(Request request, boolean expectError, String expectedOutput) {
		println(request.toString());
		try{
			// make the request
			String result = SimpleClient.makeRequest(HOST, request);
			// you didn't get an error. Were you looking for one?
			if(expectError) {
				println("Test failed. Expected an error but didn't get one");
			} else if(!result.equals(expectedOutput)) {
				println("Test failed.");
				println("Expected response: " + expectedOutput);
				println("Actuall response:  " + result);
			} else {
				println("Test passed");
			}
		} catch(IOException e) {
			// you got an error. Were you looking for one?
			if(expectError) {
				println("Test passed.");
			} else {
				println("Test failed. Received unexpected error: " + e.getMessage());
			}
		}
		println("");
	}

}
