package friends;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Driver {
    public static void main( String[] args ) {
        try { 
        	Scanner sc = new Scanner(new File("conntest6.txt"));
            Graph gr = new Graph( sc );
            //System.out.println( gr.map.get("samir"));
            //System.out.println("Shortest chain ex1:" + " " + Friends.shortestChain(gr,"nick","aparna"));
            
           /* for( Person p : gr.members ) {
                System.out.print( "(" + p.name + ", " + p.school + ")" );
                Friend ptr = p.first;
                while( ptr != null ) {
                    System.out.print("\n" + gr.members[ptr.fnum].name + " school @ " + gr.members[ptr.fnum].school );
                    ptr = ptr.next;
                }
                System.out.println("\n");
            }*/
         //System.out.println( Friends.shortestChain( gr, "p301", "p198" ));
         //System.out.println( Friends.cliques( gr, "rutgers" ) );
         System.out.println( Friends.connectors( gr ) );
            //System.out.println( Friends.connectorsData( gr ) );
        } catch ( FileNotFoundException fnfe ) {
            System.out.println( "File not found!" );
            fnfe.printStackTrace();
        }
    }
}