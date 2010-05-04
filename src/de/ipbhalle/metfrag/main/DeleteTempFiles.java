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
package de.ipbhalle.metfrag.main;

import java.io.File;
import java.util.List;
import java.util.Vector;

public class DeleteTempFiles implements Runnable{
	
	private Vector<File> filesToDelete = null;
	
	/**
	 * Instantiates a new delete temp files.
	 * 
	 * @param filesToDelete the files to delete
	 */
	public DeleteTempFiles(Vector<File> filesToDelete)
	{
		this.filesToDelete = filesToDelete;
	}
	
	/**
	 * Instantiates a new delete temp files.
	 * 
	 * @param filesToDelete the files to delete
	 */
	public DeleteTempFiles(List<File> filesToDelete)
	{
		this.filesToDelete = new Vector<File>();
		for (File file : filesToDelete) {
			this.filesToDelete.add(file);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override public void run()
	{
		for(int i = 0; i < filesToDelete.size(); i++)
		{
			filesToDelete.get(i).delete();
		}  
	}

}
