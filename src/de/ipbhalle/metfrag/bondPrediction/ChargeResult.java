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
	 * @param originalMol the original mol
	 * @param molWithProton the mol with proton
	 * @param molWithCharge the mol with charge
	 * @param chargeString the charge string
	 */
	public ChargeResult(IAtomContainer originalMol, IAtomContainer molWithProton, IAtomContainer molWithCharge, String chargeString, String protonatedAtom)
	{
		this.setOriginalMol(originalMol);
		this.setMol(molWithProton);
		this.setChargeString(chargeString);
		this.setMolWithCharge(molWithCharge);
	}

	public void setMol(IAtomContainer molWithProton) {
		this.molWithProton = molWithProton;
	}

	/**
	 * Gets the mol used as input for the bond length change calculation. (Protonated and with charge changed)
	 *
	 * @return the mol with proton
	 */
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

	/**
	 * Gets the mol only with the charge changed on one atom.
	 *
	 * @return the mol with charge
	 */
	public IAtomContainer getMolWithCharge() {
		return molWithCharge;
	}

}
