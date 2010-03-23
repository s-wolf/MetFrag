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
