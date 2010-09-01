package de.ipbhalle.metfrag.bondPrediction;

import org.openscience.cdk.interfaces.IAtomContainer;

public class ChargeResult {
	
	private IAtomContainer originalMol = null;
	private IAtomContainer molWithProton = null;
	private IAtomContainer molWithCharge = null;
	private String chargeString = "";
	
	/**
	 * Instantiates a new charge result.
	 * 
	 * @param mol the mol
	 * @param chargeString the charge string
	 */
	public ChargeResult(IAtomContainer originalMol, IAtomContainer molWithProton, IAtomContainer molWithCharge, String chargeString)
	{
		this.setOriginalMol(originalMol);
		this.setMol(molWithProton);
		this.setChargeString(chargeString);
		this.setMolWithCharge(molWithCharge);
	}

	public void setMol(IAtomContainer molWithProton) {
		this.molWithProton = molWithProton;
	}

	public IAtomContainer getMolWithProton() {
		return molWithProton;
	}

	public void setChargeString(String chargeString) {
		this.chargeString = chargeString;
	}

	public String getChargeString() {
		return chargeString;
	}

	public void setOriginalMol(IAtomContainer originalMol) {
		this.originalMol = originalMol;
	}

	public IAtomContainer getOriginalMol() {
		return originalMol;
	}

	public void setMolWithCharge(IAtomContainer molWithCharge) {
		this.molWithCharge = molWithCharge;
	}

	public IAtomContainer getMolWithCharge() {
		return molWithCharge;
	}

}
