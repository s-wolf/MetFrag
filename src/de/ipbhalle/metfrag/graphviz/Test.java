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
