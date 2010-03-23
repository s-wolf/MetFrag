package de.ipbhalle.metfrag.fragmenter;

import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IRing;

public class BondInRing {
	
	private IBond bond = null;
	private IRing ring = null;
	
	/**
	 * Creates a new bond - ring pair.
	 * 
	 * @param bond the bond
	 * @param ring the ring
	 */
	public BondInRing(IBond bond, IRing ring)
	{
		this.bond = bond;
		this.ring = ring;
	}
	
	
	/**
	 * Gets the bond.
	 * 
	 * @return the bond
	 */
	public IBond getBond()
	{
		return this.bond;
	}
	
	/**
	 * Gets the ring.
	 * 
	 * @return the ring
	 */
	public IRing getRing()
	{
		return this.ring;
	}

}
