/*
 * File: NameSurferDataBase.java
 * -----------------------------
 * This class keeps track of the complete database of names.
 * The constructor reads in the database from a file, and
 * the only public method makes it possible to look up a
 * name and get back the corresponding NameSurferEntry.
 * Names are matched independent of case, so that "Eric"
 * and "ERIC" are the same names.
 */

import acm.util.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class NameSurferDataBase implements NameSurferConstants {
	
/* Constructor: NameSurferDataBase(filename) */
/**
 * Creates a new NameSurferDataBase and initializes it using the
 * data in the specified file.  The constructor throws an error
 * exception if the requested file does not exist or if an error
 * occurs as the file is being read.
 */
	private HashMap <String, NameSurferEntry> nameMap = new HashMap <String, NameSurferEntry>();
	public NameSurferDataBase(String filename) {
		try{
			BufferedReader br =	new BufferedReader(new FileReader(filename));
			while(true){
				String line = br.readLine();
				if(line == null){
					break;
				}	
				NameSurferEntry entry = new NameSurferEntry(line);
				nameMap.put(entry.getName(), entry);
			}
			br.close();
		}  catch (IOException e) {
			throw new RuntimeException(e);
		}
	
	}
	
/* Method: findEntry(name) */
/**
 * Returns the NameSurferEntry associated with this name, if one
 * exists.  If the name does not appear in the database, this
 * method returns null.
 */
	
/*this method returns the name surferentry associated with the name if it exists
 * and also makes the name entered case insensitive. this method returns null if no name is 
 * in the database
 * 
 */
	public NameSurferEntry findEntry(String name) {
		char firstLetter = name.charAt(0);
		firstLetter = Character.toUpperCase(firstLetter);
		String partialName = name.substring(1, name.length());
		partialName = partialName.toLowerCase();
		String casedName = firstLetter + partialName;
		if(nameMap.containsKey(casedName)){
			return nameMap.get(casedName);
		} else {
			return null;
		}
	}
}

