package de.ipbhalle.metfrag.tools;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.OverlappingFileLockException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.io.FileUtils;
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
			Thread.sleep(1000);
			writeToFile(file, content);
		}
	}
	
	
	/**
	 * Write to file using java nio.
	 *
	 * @param file the file
	 * @param content the content
	 * @throws InterruptedException the interrupted exception
	 */
	public static void writeToFileNIO(String fileString, String content) throws InterruptedException
	{
		
		// Get a file channel for the file
        File file = null;
        FileChannel channel = null;
        FileLock lock = null;
		try {
			 file = new File(fileString);
	         channel = new RandomAccessFile(file, "rw").getChannel();
	        
	         // Use the file channel to create a lock on the file.
	         // This method blocks until it can retrieve the lock.
	         lock = channel.lock();
	        
	         // Try acquiring the lock without blocking. This method returns
	         // null or throws an exception if the file is already locked.
	         try {
	             //lock = channel.tryLock();
	        	 FileUtils.writeStringToFile(file, "file content");
	             
	         } catch (OverlappingFileLockException e) {
	             // File is already locked in this thread or virtual machine
	        	 System.err.println("File is locked!!! " + e.getMessage());
	         }
	        
         } catch (Exception e) {
        	 System.err.println("Error: " + e.getMessage());
         }
		finally
		{
			// Release the lock
	         try {
				lock.release();
				// Close the file
		        channel.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         
		}
	    
	}
	
	
	public static void main(String[] args) {
		Runnable r1 = new Runnable() { 
		  public void run() { 
		  	try {
				  writeToFileNIO("test.txt", "Ausgabe\n");
				  Thread.sleep( 1500 );
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		  } 
		}; 
		
		ExecutorService executor = Executors.newFixedThreadPool(10);
		 
		executor.execute( r1 ); 
		executor.execute( r1 ); 
		executor.execute( r1 ); 
		executor.execute( r1 ); 
		executor.execute( r1 ); 
		executor.execute( r1 ); 
		executor.execute( r1 ); 

		 
		executor.shutdown();
	}

}
