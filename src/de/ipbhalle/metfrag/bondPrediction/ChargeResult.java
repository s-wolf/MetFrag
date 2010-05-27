package de.ipbhalle.metfrag.bondPrediction;

import org.openscience.cdk.interfaces.IAtomContainer;

public class ChargeResult {
	
	private IAtomContainer mol = null;
	private String chargeString = "";
	
	/**
	 * Instantiates a new charge result.
	 * 
	 * @param mol the mol
	 * @param chargeString the charge string
	 */
	public ChargeResult(IAtomContainer mol, String chargeString)
	{
		this.setMol(mol);
		this.setChargeString(chargeString);
	}

	public void setMol(IAtomContainer mol) {
		this.mol = mol;
	}

	public IAtomContainer getMol() {
		return mol;
	}

	public void setChargeString(String chargeString) {
		this.chargeString = chargeString;
	}

	public String getChargeString() {
		return chargeString;
	}

}
