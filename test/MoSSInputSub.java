

import java.io.File;

import org.openscience.cdk.io.INChIReader;
import org.openscience.cdk.smiles.SmilesGenerator;

import de.ipbhalle.metfrag.spectrum.WrapperSpectrum;



public class MoSSInputSub {
	
	
	public String id; //ID aus MassBank
	
	public double val; // kennzeichnet, ob Vordergrund oder Hintergrund in MoSS
	
	public String smiles;
	
	
	public MoSSInputSub(String input)
	{
	
		String dummi[] = new String[input.split("\\s+").length];
		dummi=input.split("\\s+");
		
		this.id=dummi[0];
		
		this.val = Double.valueOf(dummi[1]).doubleValue();
		
		this.smiles = dummi[2];
		
		
		
	}
	
	//korrigiert die Smiles:
		//Wenn sie [*] (also Wildcard-Atome) enthalten: Entfernen des Smiles
		//Fehlende Klammern werden korrigiert
	public static boolean  checkSmiles(String smiles, File file) 
	{
		boolean check=true;
		
		int testsum=0;
		
		CharSequence star="*";
		
		if (smiles.contains(star))
		{
			check=false;
			
		}
		else
		{
			for(int j=0;j<smiles.length();j++)
			{
				if(smiles.substring(j,j+1).equals("(")) {testsum++;}
				if(smiles.substring(j,j+1).equals(")")) {testsum--;}	
			}
		

			if(testsum>0)
			{
				for(int i=0;i<testsum;i++)
				{
					smiles=smiles+")";
					System.out.println("File: " + file.getName());
				}
			}
			
			if(testsum<0)
			{
				testsum=-1*testsum;
				for(int i=0;i<testsum;i++)
				{
					smiles="("+smiles;
				}
				System.out.println("File: " + file.getName());
			}
			
		}
		
		return check;
		
	}
	
	
	public static void main(String[] args) {
		
		
		
		String home = "/vol/massbank/data/records/";
		//String home ="/home/ftarutti/Eval/Data/";
		
	
		for (File f : new File(home).listFiles()) {
			//System.out.println(f.getAbsolutePath());
			
			WrapperSpectrum wsp = new WrapperSpectrum(f.getAbsolutePath());

			System.out.println(wsp.getSmiles());
			if(wsp.getSmiles().contains(("*")))
			{
				System.out.println(wsp.getSmiles());
			}
			if(checkSmiles(wsp.getSmiles(), f) == false)
			{
				System.out.println("Wildcart "+f.getName());
				
			}
			
						
		}
		
	}

}

