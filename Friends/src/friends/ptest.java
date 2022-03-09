package friends;
import java.util.ArrayList;
import java.util.HashMap;

import structures.Queue;

public class ptest {
	public static ArrayList<String> connectors(Graph g) {
		ArrayList<String> connectors = new ArrayList<String>(); 

		boolean[] visited = new boolean[g.members.length];
		boolean[] backed = new boolean[g.members.length];
		Person[] person = new Person[g.members.length];
		for (int i =0; i<g.members.length;i++) { 
			person[i] =g.members[i];					
		}			 
		int n = g.members.length;		
		for (int i = 0; i < n; i++) {
			if (!visited[i])
				DFSConnectors(g, i, visited, backed, connectors,person);
		}
		return connectors;
	}

	private static void DFSConnectors(Graph g, int start,boolean[] visited,boolean[] backed,ArrayList<String> connectors, Person[] person){		
			DFS(g, start, backed);			
			int number = g.members[start].first.fnum;
			String name = g.members[start].name;
			g.members[start] = null;			
			
			DFS(g, number, visited);
			visited[start] =  true;
					
			for(int i = 0; i<g.members.length; i++)
			{
				if(backed[i]!=visited[i])
				{
					connectors.add(name);
					break;
				}
			}
						
			for(int i = 0; i<g.members.length; i++)
			{
				g.members[i] = person[i];
				backed[i] = false;
				visited[i] = false;
			}		
	}
	
	private static void DFS(Graph g, int start,boolean[] visited) 
	{
		if(g.members[start] == null)
		{
			return;
		}
		visited[start] = true;
		
		for (Friend n = g.members[start].first; n != null; n = n.next) 
		{			
			if (!visited[n.fnum]) 
			{			
				DFS(g, n.fnum, visited);
			}

		}
	
	}
}
