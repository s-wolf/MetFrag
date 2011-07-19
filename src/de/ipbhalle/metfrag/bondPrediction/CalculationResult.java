package de.ipbhalle.metfrag.bondPrediction;

import org.openscience.cdk.interfaces.IAtomContainer;

public class CalculationResult {
	
	private IAtomContainer originalMol = null;
	private IAtomContainer molWithProton = null;
	private IAtomContainer molOutput = null;
	private String resultString = "";
	private String protonatedAtom = "";
	private String debugMessages = "";
	
	/**
	 * Instantiates a new charge result.
	 *
	 * @param originalMol the original mol
	 * @param molWithProton the mol with proton
	 * @param molOutput the mol with charge
	 * @param resultString the charge string
	 * @param protonatedAtom the protonated atom
	 * @param debugMessages the debug messages
	 */
	public CalculationResult(IAtomContainer originalMol, IAtomContainer molWithProton, IAtomContainer molOutput, String resultString, String protonatedAtom, String debugMessages)
	{
		this.setOriginalMol(originalMol);
		this.setMol(molWithProton);
		this.setResultString(resultString);
		this.setMolOutput(molOutput);
		this.setProtonatedAtom(protonatedAtom);
		this.setDebugMessages(debugMessages);
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

	public void setResultString(String resultString) {
		this.resultString = resultString;
	}

	public String resultString() {
		return resultString;
	}

	public void setOriginalMol(IAtomContainer originalMol) {
		this.originalMol = originalMol;
	}

	/**
	 * Gets the original mol without modifications on the structure
	 *
	 * @return the original mol
	 */
	public IAtomContainer getOriginalMol() {
		return originalMol;
	}

	public void setMolOutput(IAtomContainer molOutput) {
		this.molOutput = molOutput;
	}

	/**
	 * Gets the mol without hydrogens but with charge on the protonated atom
	 *
	 * @return the mol with charge
	 */
	public IAtomContainer getMolOutput() {
		return molOutput;
	}

	public void setProtonatedAtom(String protonatedAtom) {
		this.protonatedAtom = protonatedAtom;
	}

	public String getProtonatedAtom() {
		return protonatedAtom;
	}

	public void setDebugMessages(String debugMessages) {
		this.debugMessages = debugMessages;
	}

	public String getDebugMessages() {
		return debugMessages;
	}

}
