package friends;

import java.util.ArrayList;
import java.util.HashMap;
import structures.Queue;

public class Friends {

	/**
	 * Finds the shortest chain of people from p1 to p2. Chain is returned as a
	 * sequence of names starting with p1, and ending with p2. Each pair (n1,n2) of
	 * consecutive names in the returned chain is an edge in the graph.
	 * 
	 * @param g  Graph for which shortest chain is to be found.
	 * @param p1 Person with whom the chain originates
	 * @param p2 Person at whom the chain terminates
	 * @return The shortest chain from p1 to p2. Null or empty array list if there
	 *         is no path from p1 to p2
	 */
	public static ArrayList<String> shortestChain(Graph g, String p1, String p2) {
		if ((!g.map.containsKey(p1)) || (!g.map.containsKey(p2))) {
			// System.out.println("here");
			return null;
		}

		ArrayList<String> path = new ArrayList<String>();
		Person start = g.members[g.map.get(p1)];
		Person target = g.members[g.map.get(p2)];

		Queue<Person> q = new Queue<Person>();
		HashMap<Person, Person> prev = new HashMap<Person, Person>();

		q.enqueue(start);
		prev.put(start, null);

		BFS(q, target, g, prev);

		Person person = target;

		while (person != null && prev.get(target) != null) {
			path.add(0, person.name);
			person = prev.get(person);
		}
		if(path.size()==0) {
			return null;
		}
		return path;
	}

	private static void BFS(Queue<Person> q, Person target, Graph g, HashMap<Person, Person> prev) {
		// System.out.println(q.size());
		while (!q.isEmpty()) {
			Person person = q.dequeue();
			ArrayList<Person> friends = new ArrayList<Person>();
			Friend f = person.first;
			while (f != null) {
				// System.out.println(person.name);
				friends.add(g.members[f.fnum]);
				f = f.next;
			}

			for (Person friend : friends) {
				if (!prev.containsKey(friend)) {
					q.enqueue(friend);
					prev.put(friend, person);
				}
			}
		}
	}

	/**
	 * Finds all cliques of students in a given school.
	 * 
	 * Returns an array list of array lists - each constituent array list contains
	 * the names of all students in a clique.
	 * 
	 * @param g      Graph for which cliques are to be found.
	 * @param school Name of school
	 * @return Array list of clique array lists. Null or empty array list if there
	 *         is no student in the given school
	 */
	public static ArrayList<ArrayList<String>> cliques(Graph g, String school) {
		boolean[] visited = new boolean[g.members.length];
		ArrayList<ArrayList<String>> students = new ArrayList<>();
		ArrayList<String> clique = new ArrayList<>();
		String firstStudent = null;
		int firstIndex = 0;
		for (firstIndex = 0; firstIndex < g.members.length; firstIndex++) {
			if (g.members[firstIndex].student) {
				if (g.members[firstIndex].school.equals(school)) {
					firstStudent = g.members[firstIndex].name;
					break;
				}
			}
		}
		if (firstStudent == null) {
			return null;
		}

		getTheStudents(g, clique, school, g.members[firstIndex], visited);
		if (clique.size() > 0) {
			students.add(clique);
		}

		for (Person student : g.members) {
			if (visited[g.map.get(student.name)] || !student.student) {
				// System.out.println("v: " + student.name);
				continue;
			}
			clique = new ArrayList<>();
			if (student.school.equals(school)) {
				// System.out.println(student.name);
				getTheStudents(g, clique, school, student, visited);
			}
			if (clique.size() > 0) {
				students.add(clique);
			}
		}
		return students;
	}

	private static void getTheStudents(Graph g, ArrayList<String> clique, String school, Person p, boolean[] visited) {
		int studentAtIndex = g.map.get(p.name);
		if (!visited[studentAtIndex]) {
			clique.add(p.name);
		}
		visited[studentAtIndex] = true;
		Friend friend = p.first;
		while (friend != null) {
			int i = friend.fnum;
			Person friendP = g.members[i];
			if (!visited[i] && friendP.student && friendP.school.equals(school)) {
				getTheStudents(g, clique, school, g.members[i], visited);
			}
			friend = friend.next;
		}
	}

	/**
	 * Finds and returns all connectors in the graph.
	 * 
	 * @param g Graph for which connectors needs to be found.
	 * @return Names of all connectors. Null or empty array list if there are no
	 *         connectors.
	 */
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
		if(connectors.size()==0) {
			return null;
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
		Friend f = g.members[start].first;
		while(f!=null) {
			if (!visited[f.fnum]) 
			{			
				DFS(g, f.fnum, visited);
			}
			f = f.next;
		}	
	}
}
