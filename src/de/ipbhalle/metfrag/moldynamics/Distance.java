package de.ipbhalle.metfrag.moldynamics;

public class Distance {
	
	private String bond;
	private Double bondLength;
	private String bondID = "";
	
	/**
	 * Instantiates a new distance.
	 * 
	 * @param bond the bond
	 * @param bondLength the bond length
	 */
	public Distance(String bond, Double bondLength)
	{
		setBond(bond);
		setBondLength(bondLength);
	}
	
	/**
	 * Instantiates a new distance.
	 * 
	 * @param bond the bond
	 * @param bondLength the bond length
	 * @param bondID the bond id
	 */
	public Distance(String bond, Double bondLength, String bondID)
	{
		setBond(bond);
		setBondLength(bondLength);
		setBondID(bondID);
	}

	/**
	 * Sets the bond length.
	 * 
	 * @param bondLength the new bond length
	 */
	public void setBondLength(Double bondLength) {
		this.bondLength = bondLength;
	}

	/**
	 * Gets the bond length.
	 * 
	 * @return the bond length
	 */
	public Double getBondLength() {
		return (Math.round(bondLength * 1000.0)/1000.0);
	}

	/**
	 * Sets the bond.
	 * 
	 * @param bond the new bond
	 */
	public void setBond(String bond) {
		this.bond = bond;
	}

	/**
	 * Gets the bond.
	 * 
	 * @return the bond
	 */
	public String getBond() {
		return bond;
	}

	public void setBondID(String bondID) {
		this.bondID = bondID;
	}

	public String getBondID() {
		return bondID;
	}

}
