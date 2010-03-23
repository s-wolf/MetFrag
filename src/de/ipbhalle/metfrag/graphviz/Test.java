package de.ipbhalle.metfrag.graphviz;


import java.io.*;

public class Test
{
   public static void main(String[] args)
   {
      Test p = new Test();
      p.start();
   }

   private void start()
   {
      GraphViz gv = new GraphViz();
      gv.addln(gv.start_graph());
      gv.addln("A -> B;");
      gv.addln("A -> C;");
      gv.addln(gv.end_graph());
      System.out.println(gv.getDotSource());
      
      File out = new File("/home/basti/out.gif");
      gv.writeGraphToFile(gv.getGraph(gv.getDotSource()), out);
      
//      digraph structs {
//    	    node [shape=plaintext];
//
//    	    struct1 [label=<<TABLE>
//    	    <TR><TD><IMG SRC="eqn.gif"/></TD></TR>
//    	    <TR><TD>caption</TD></TR>
//    	    </TABLE>>];
//    	}
      GraphViz gv1 = new GraphViz();
      gv1.addln(gv.start_graph());
      gv1.addln("node [shape=plaintext];" +
      		"struct1 [label=<<TABLE><TR><TD>" +
      		"<IMG SRC=\"/home/basti/eqn.gif\"/>" +
      		"</TD></TR><TR><TD>caption</TD></TR></TABLE>>];"
      		);
      gv1.addln(gv.end_graph());
      System.out.println(gv1.getDotSource());
      File out1 = new File("/home/basti/out1.ps");
      gv.writeGraphToFile(gv.getGraph(gv1.getDotSource()), out1);
   }
}
