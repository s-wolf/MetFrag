/*
 *
 * Copyright (C) 2009-2010 IPB Halle, Sebastian Wolf
 *
 * Contact: swolf@ipb-halle.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package de.ipbhalle.metfrag.tools;

import java.io.*;

/**
 * The Class StreamGobbler as shown on javaworld
 * http://www.javaworld.com/javaworld/jw-12-2000/jw-1229-traps.html?page=4
 */
public class StreamGobbler extends Thread {
	private InputStream is;
	private String type;
	private String outputToFile;
	private boolean verbose;

	public StreamGobbler(InputStream is, String type, boolean verbose) {
		this.is = is;
		this.type = type;
		this.outputToFile = "";
		this.verbose = verbose;
	}
	
	/**
	 * Gets the output.
	 *
	 * @return the output
	 */
	public String getOutput()
	{
		return this.outputToFile;
	}

	public void run() {
		try {
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null)
			{
				if(this.type.equals("OUTPUT"))
					outputToFile += line + "\n"; 
				
				if(verbose)
					System.out.println(type + ">" + line);
			}			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
