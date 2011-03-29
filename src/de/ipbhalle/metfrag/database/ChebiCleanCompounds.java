package de.ipbhalle.metfrag.database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ChebiCleanCompounds {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		try {
			
			/*	Sets up a file reader to read the file passed on the command
				line one character at a time */
			FileReader input = new FileReader("/home/swolf/Downloads/ChEBI_complete.sdf");
            
			/* Filter FileReader through a Buffered read to read a line at a
			   time */
			BufferedReader bufRead = new BufferedReader(input);
			
            String line; 	// String that holds current file line
            int count = 0;	// Line number of count 
            
            StringBuilder sbMolecule = new StringBuilder();
            
            // Read first line
            line = bufRead.readLine();
            count++;
            
            boolean drop = false;
            
            // Create file 
            FileWriter fstream = new FileWriter("/home/swolf/Downloads/ChEBI_complete_CLEAN.sdf", true);
            BufferedWriter out = new BufferedWriter(fstream);

			// Read through file one line at time. Print line # and line
            while (line != null){
                if(line.contains("$$$$"))
                {
                	if(!drop)
                		out.write(sbMolecule.append(line + "\n").toString());
                	drop = false;
                	sbMolecule = new StringBuilder();
                	line = bufRead.readLine();
                	continue;
                }
                else if((line.contains("8  0  0  0  0") || line.contains("6  0  0  0  0")) && !line.contains("V2000"))
                {
                	drop = true;
                }
                
                sbMolecule.append(line + "\n");
                
                line = bufRead.readLine();
                count++;
            }
            
            out.close();
            
            bufRead.close();
			
        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
		}catch (IOException e){
			// If another exception is generated, print a stack trace
            e.printStackTrace();
        }
	}

}
