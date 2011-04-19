package de.ipbhalle.metfrag.databaseMetChem;

public class CandidateMetChem {
	
	private Integer compoundID;
	private String accession;
	
	public CandidateMetChem(Integer compoundID, String accession)
	{
		setAccession(accession);
		setCompoundID(compoundID);
	}

	public void setCompoundID(Integer compoundID) {
		this.compoundID = compoundID;
	}

	public Integer getCompoundID() {
		return compoundID;
	}

	public void setAccession(String accession) {
		this.accession = accession;
	}

	public String getAccession() {
		return accession;
	}

}
