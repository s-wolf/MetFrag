package de.ipbhalle.metfrag.fragmenter;

import org.openscience.cdk.interfaces.IBond;

public class BondPair {
	
	private int count = 0;
	private IBond bond1 = null;
	private IBond bond2 = null;
	
	public BondPair(IBond bond1, IBond bond2)
	{
		count = 2;
		this.bond1 = bond1;
		this.bond2 = bond2;
	}
	
	public BondPair(IBond bond1)
	{
		count = 1;
		this.bond1 = bond1;
	}
	
	public int getCount()
	{
		return this.count;
	}
	
	public IBond getBond1()
	{
		return this.bond1;
	}
	
	public IBond getBond2()
	{
		return this.bond2;
	}
	
	public boolean checkBonds(IBond bond1, IBond bond2)
	{
		boolean found = false;
		if ((this.bond1 == bond1 || this.bond2 == bond1) && (this.bond1 == bond2 || this.bond2 == bond2))
			found = true;
			
		return found;
	}
	
	public boolean checkBond(IBond bond1)
	{
		boolean found = false;
		if (this.bond1 == bond1)
			found = true;
			
		return found;
	}

}
