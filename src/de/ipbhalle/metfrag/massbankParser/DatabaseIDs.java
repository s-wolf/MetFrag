package de.ipbhalle.metfrag.massbankParser;


public enum DatabaseIDs {
	
	CHEBI , KEGG , PUBCHEM_CID , PUBCHEM_SID  , KNAPSACK , METLIN;

	@Override public String toString(){
		
		String s = super.toString();
		
		if(s.equals("PUBCHEM_CID") || s.equals("PUBCHEM_SID"))
		{
			s= s.replace("_", " ");
		}

		return s;
	}
}
