/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
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
