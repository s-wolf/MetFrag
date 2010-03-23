package de.ipbhalle.metfrag.massbankParser;

public class ElementNotFoundException extends Exception {

	ElementNotFoundException(String elementName){
		super("Element \""+elementName+"\" not found in element table!");
	}
}
