/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */
package de.ipbhalle.metfrag.massbankParser;

import java.util.Vector;
import java.util.Arrays;




public class Molecule implements java.io.Serializable, Comparable<Molecule> {
	private Abundancy[] contents;
	private int position;
	private double mass;
	private ElementTable elementTable;
	private double dbe;
	private double score;
	private Vertex vertex;
	
	public Molecule(Molecule another){
		elementTable = another.elementTable;
		position = another.position;
		mass = another.mass;
		dbe = another.dbe;
		vertex = null;
		contents = new Abundancy[elementTable.size()];
		for (int i = 0; i < position; ++i){
			contents[i] = new Abundancy(another.contents[i].name, another.contents[i].amount);
		}
	}
	
	public Molecule(ElementTable et){
		mass = 0.0;
		elementTable = et;
		contents = new Abundancy[et.size()];
		position = 0;
		score = 1.0;
		dbe = 1.0;
		vertex = null;
	}
	
	
	public Molecule(String formula, ElementTable et) throws ElementNotFoundException {
		this(et);
		StringBuffer element = new StringBuffer(), number = new StringBuffer();
		Element el;
		int amount;
		for (int i=0; i < formula.length(); ++i){
			char c = formula.charAt(i);
			if (Character.isUpperCase(c)){
				// if an element has been read
				if (element.length() != 0) {
					// lookup element
					el = et.get(element.toString());
					// if a number has been read: parse amount, enter in map and update mass.
					if (number.length() != 0) {
						amount = Integer.parseInt(number.toString());
						contents[position] = new Abundancy(element.toString(), amount);
						++position;
						mass += amount*el.getMass();
					}	else { // Insert one element
						contents[position] = new Abundancy(element.toString(), 1);
						++position;
						mass += el.getMass();
					}
				}
				element.delete(0, element.length());
				number.delete(0, number.length());
				element.append(c);
			} else if (Character.isLowerCase(c)){
				element.append(c);
			} else if (Character.isDigit(c)){
				number.append(c);
			}
		} // end for
		
		// empty stringbuffers for the last time
		// if an element has been read
		if (element.length() != 0) {
			// lookup element
			el = et.get(element.toString());
			// if a number has been read: parse amount, enter in map and update mass.
			if (number.length() != 0) {
				amount = Integer.parseInt(number.toString());
				contents[position] = new Abundancy(element.toString(), amount);
				++position;
				mass += amount*el.getMass();
			}	else { // Insert one element
				contents[position] = new Abundancy(element.toString(), 1);
				++position;
				mass += el.getMass();
			}
		}
		Arrays.sort(contents, 0, position);
		dbe= calcDBE();
	}
	
	public Vertex getVertex() {
		return vertex;
	}
	
	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
	}

	public double getMass(){
		return mass;
	}
	
	public double getDBE(){
		return dbe;
	}
	
	public ElementTable getElementTable(){
		return elementTable;
	}
	
	public void addElement(String toBeAdded) throws ElementNotFoundException {
		addElement(toBeAdded, 1);
	}
	
	public void addElement(String toBeAdded, int abundance) throws ElementNotFoundException {
		mass += elementTable.get(toBeAdded).getMass()*abundance;
		int i = 0;
		while (i < position && !toBeAdded.equals(contents[i].name)) ++i;
		if (i == position) {
			contents[position] = new Abundancy(toBeAdded, abundance);
			++position;
			Arrays.sort(contents, 0, position);			
		} else {
			contents[i].amount += abundance;
		}
		dbe = calcDBE();
	}
	
	public void removeElement(String toBeRemoved) throws ElementNotFoundException {
		Element el = elementTable.get(toBeRemoved);
		int i = 0;
		while (i < position && !toBeRemoved.equals(contents[i].name)) ++i;
		if (i == position || contents[i].amount == 0) return; // or throw exception?
		else --contents[i].amount;
		mass -= el.getMass();
		Arrays.sort(contents, 0, position);
		dbe = calcDBE();
	}
	
	public int getElementAbundance(String which){
		int i = 0;
		while (i < position && !which.equals(contents[i].name)) ++i;
		if (i == position) return 0;
		return contents[i].amount;
	}
	
	public boolean checkWithDBE(){
		/*int[] valencies = new int[5];
		for (int i = 0; i < position; ++i){
			valencies[elementTable.get(contents[i].name).getValency()] += contents[i].amount;
		}
		return ((valencies[1] - valencies[3]) % 2 == 0) &&			// filters if DBE is integer
			((valencies[3] + 2 * valencies[4] + 2) >= valencies[1]);	// filters if DBE > 0 */
		return (2*dbe % 2)== 0 && dbe >= 0;
	}
	
	private double calcDBE() throws ElementNotFoundException {
		//int[] valencies = new int[5];
		double dbe = 1.0;
		for (int i = 0; i < position; ++i){
			//valencies[elementTable.get(contents[i].name).getValency()] += contents[i].amount;
			dbe += (0.5*elementTable.get(contents[i].name).getValency()-1.0)*contents[i].amount;
		}
		return dbe;
		//return 1.0 - valencies[1]/2.0 + valencies[3]/2.0 + valencies[4];
	}
	
	public boolean isTrueSubsetOf(Molecule parent){
		boolean wasSmaller = false;
		if (parent.mass < mass) return false;
		int pIndex = 0;
		for (int i = 0; i < position; ++i){
			while (pIndex < parent.position && !contents[i].name.equals(parent.contents[pIndex].name)){
				++pIndex;
			}
			if (pIndex >= parent.position || contents[i].amount > parent.contents[pIndex].amount) return false;
			if (contents[i].amount < parent.contents[pIndex].amount) wasSmaller = true;
		}
		 // If the molecules were identical till the end of this molecule, but the parent molecule continues, return true
		if (pIndex < parent.position-1) return true;
		return wasSmaller;
	}

	public boolean isSubsetOf(Molecule parent){
		if (parent.mass < mass) return false;
		int pIndex = 0;
		for (int i = 0; i < position; ++i){
			while (pIndex < parent.position && !contents[i].name.equals(parent.contents[pIndex].name)){
				++pIndex;
			}
			if (pIndex >= parent.position || contents[i].amount > parent.contents[pIndex].amount) return false;
		}
		return true;
	}
	
	public Molecule add(Molecule summand) throws ElementNotFoundException{
		Molecule result = new Molecule(this.elementTable);
		int sIndex = 0, i = 0;
		int comparison = 0;
		while (sIndex < summand.position || i < position){
			if (sIndex >= summand.position){
				comparison = -1;
			} else if (i >= position){
				comparison = 1;
			} else {
				comparison = contents[i].compareTo(summand.contents[sIndex]);
			}
			
			if (comparison == 0){
				result.addElement(contents[i].name, contents[i].amount+summand.contents[sIndex].amount);
				++sIndex;
				++i;
			} else if (comparison < 0) {
				result.addElement(contents[i].name, contents[i].amount);				
				++i;
			} else {
				result.addElement(summand.contents[sIndex].name, summand.contents[sIndex].amount);				
				++sIndex;
			}
		}
		return result;
	}
	
	public Molecule subtract(Molecule child) throws ElementNotFoundException{
		Molecule result = new Molecule(this.elementTable);
		int cIndex = 0;
		for (int i = 0; i < position; ++i){
			while (cIndex < child.position && !contents[i].name.equals(child.contents[cIndex].name)) {
				++cIndex;
			}
			if (cIndex >= child.position){
				result.addElement(contents[i].name, contents[i].amount);
				cIndex = 0;
			}
			else result.addElement(contents[i].name, contents[i].amount-child.contents[cIndex].amount);
		}
		return result;
	}
	
	public int compareTo(Molecule another){
		return this.toString().compareTo(another.toString());
	}
	
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Molecule)) return false;
		Molecule another = (Molecule) o;
		if (position != another.position) return false;
		for (int i = 0; i < position; ++i){
			if (contents[i].amount != another.contents[i].amount || !contents[i].name.equals(another.contents[i].name)) return false;
		}
		return true;
	}
	
	public String noHString(){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < position; ++i){
			if (!contents[i].name.equals("H")){
				if (contents[i].amount > 0) buffer.append(contents[i].name);
				if (contents[i].amount > 1) buffer.append(contents[i].amount);
			}
		}
		return buffer.toString();
	}

	public String toString(){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < position; ++i){
			if (contents[i].amount > 0) buffer.append(contents[i].name);
			if (contents[i].amount > 1) buffer.append(contents[i].amount);
		}
		return buffer.toString();
	}
}

class Abundancy implements Comparable<Abundancy>, java.io.Serializable {
	String name;
	int amount;
	
	Abundancy(String name, int amount){
		this.name = name;
		this.amount = amount;
	}
	
	public int compareTo(Abundancy theOther){
		return name.compareTo(theOther.name);
	}
	
}
