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
