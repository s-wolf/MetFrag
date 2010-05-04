/*
*
* Copyright (C) 2009-2010 IPB Halle, Sebastian Wolf
*
* Contact: swolf@ipb-halle.de
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*
*/
package de.ipbhalle.metfrag.massbankParser;

import java.util.TreeMap;


public class ElementTable  implements java.io.Serializable {
	private TreeMap<String, Element> contents;
	
	public ElementTable(){
		contents = new TreeMap<String, Element>();
	}
	
	public void add(Element element){
		contents.put(element.getName(), element);
	}
	
	public Element get(String name) throws ElementNotFoundException {
		Element result = contents.get(name);
		if (result == null) throw new ElementNotFoundException(name);
		return result;
	}
	
	public Element[] getAll(){
		return contents.values().toArray(new Element[contents.size()]);
	}
	
	public int size(){
		return contents.size();
	}
	
	public static ElementTable generateCHNOPS(){
		ElementTable et = new ElementTable();
		et.add(new Element("C", 12.0, 4));
		et.add(new Element("H", 1.007825, 1));
		et.add(new Element("N", 14.003074, 3));
		et.add(new Element("O", 15.994915, 2));
		et.add(new Element("P", 30.973762, 3));
		et.add(new Element("S", 31.972071, 2));
		et.add(new Element("Cl", 35.4527, 1));
		return et;
	}
	
}
