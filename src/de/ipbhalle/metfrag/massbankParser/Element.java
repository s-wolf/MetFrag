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


public class Element implements Comparable<Element>, java.io.Serializable {

  public static final double ELECTRON_MASS = 0.00054858;
  public static final double PROTON_MASS = 1.00727647;
	private String name;
	private double mass;
	private int valency;
	
	public Element(String name, double mass, int valency){
		if (mass < 0) System.out.println("Masses have to be positive");
		this.name = name;
		this.mass = mass;
		this.valency = valency;
	}
	
	public String getName(){
	 return name;
	}

	public double getMass(){
	 return mass;
	}

	public int getValency(){
	 return valency;
	}
	
	public void setName(String name){
		this.name = name;
	}

	public void setMass(double mass){
		this.mass = mass;
	}

	public void setValency(int valency){
		this.valency = valency;
	}
	
	public String toString(){
		return name;
	}
	
	public int compareTo(Element o){
		return name.compareTo(o.name);
	}
}
