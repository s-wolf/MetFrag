package de.ipbhalle.metfrag.cdk12Testing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedundancyCheck {
	
	public static void main(String[] args) {
		String[] test = {"CN1CCC23C4C1CC5=C2C(=C(C=C5)O)OC3C(C=C4)O", "CN1CCC23C4C1CC5=C2C(=C(C=C5)O)OC3C(C=C4)", "CN1CCC23C4C1CC5=C2C(=C(C=C5)O)OC3C(C=C4)O"};
		
		
		Map<String, Boolean> testMap = new HashMap<String, Boolean>();
		List<String> testList = new ArrayList<String>();
		
		long start = System.currentTimeMillis();
		for (int i = 0; i < test.length; i++) {
			
			
//			if(!testMap.containsKey(test[i]))
//			{
//				testMap.put(test[i], true);
//			}
			if(!testList.contains(test[i]))
				testList.add(test[i]);
		}
		long end = System.currentTimeMillis();
		System.out.println("Gesamt: " + (end - start));
		
	}

}
