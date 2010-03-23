package de.ipbhalle.metfrag.bondPrediction;

import org.openscience.cdk.interfaces.IAtom;

public class AtomProperty implements Comparable{
	
	private IAtom atom;
	private Double charge;
	
	/**
	 * Instantiates a new atom property.
	 * 
	 * @param atom the atom
	 * @param charge the charge
	 */
	public AtomProperty(IAtom atom, Double charge)
	{
		this.atom = atom;
		this.charge = charge;
	}
	
	
	public int compareTo(Object o1) 
	{
        if (this.charge == ((AtomProperty) o1).charge)
            return 0;
        else if ((this.charge) > ((AtomProperty) o1).charge)
            return 1;
        else
            return -1;
    }
	
	public IAtom getAtom()
	{
		return atom;
	}
	
	public Double getCharge()
	{
		return charge;
	}

}
