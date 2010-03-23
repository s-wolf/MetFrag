package de.ipbhalle.metfrag.cdk12Testing;

import de.ipbhalle.metfrag.main.MetFlowConvenience;

public class MetFragConvenience {
	
	public static void main(String[] args) {
		MetFlowConvenience drf2 =
			new MetFlowConvenience(0.01, 50, 10, "119.051 467.616 45\n123.044 370.662 36\n" +
								"147.044 6078.145 606\n153.019 10000.0 999\n179.036 141.192 13\n" +
								"189.058 176.358 16\n273.076 10000.000 999\n274.083 318.003 30", "kegg" +
										"", true, "", 50, 1, 272.06847);
		try {
			System.out.println(drf2.metFrag());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
