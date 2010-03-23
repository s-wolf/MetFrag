package de.ipbhalle.metfrag.cdk12Testing;

import java.io.*;
import java.util.logging.*;

public class QuintLog{
  public static void main(String[] args) {
    try{
      FileHandler hand = new FileHandler("vk.log");
      Logger log = Logger.getLogger("log_file");
      log.addHandler(hand);
      log.warning("Doing carefully!");
      log.info("Doing something ...");
      log.severe("Doing strictily ");
      System.out.println(log.getName());
    }
    catch(IOException e){}
  }
}
