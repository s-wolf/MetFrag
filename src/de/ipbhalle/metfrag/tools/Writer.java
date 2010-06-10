package de.ipbhalle.metfrag.tools;

import org.apache.commons.io.output.LockableFileWriter;

public class Writer {
	
	/**
	 * Write to file and prevent that this file is overwritten by another thread while writing to it.
	 * 
	 * @param file the file
	 * @param content the content
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public static void writeToFile(String file, String content) throws InterruptedException
	{
		try
		{
			LockableFileWriter lfw = new LockableFileWriter(file, true, "/home/swolf/locks/");
			lfw.write(content);
			lfw.close();
		}
		catch (Exception e) {			
			Thread.sleep(10);
			writeToFile(file, content);
		}
	}

}
