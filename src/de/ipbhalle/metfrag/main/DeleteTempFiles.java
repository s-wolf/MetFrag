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
