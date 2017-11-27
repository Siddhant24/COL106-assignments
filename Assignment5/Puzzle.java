import java.io.*;
import java.util.*;

public class Puzzle {
	public static class Vertex {
		char[] element;
		int gapPos;
		public Vertex(char[] e, int g) {
			element = e;
			gapPos = g;
		}
		Vertex prev;
		String move;
		int numMoves = 999999999;
		int pqPos;
		Boolean inCloud = false;
		int d = 999999999;
		ArrayList<Pair> neighbours = new ArrayList<Pair>();
	}
	
	public static class Pair {
		public Vertex first;
		public int second;
		public String move;
		public Pair(Vertex v, int c, String s){
			first = v;
			second = c;
			move = s;
		}
	}
	
	public static class HeapPriorityQueue {
		private ArrayList<Vertex> heap = new ArrayList<Vertex>();
		private int parent(int j) { 
			return (j-1)/2;
		}
		private int leftChild(int j) {
			return 2*j+1;
		}
		private int rightChild(int j) {
			return 2*j+2;
		}
		private boolean hasLeft(int j) {
			return leftChild(j) < heap.size();
		}
		private boolean hasRight(int j) {
			return rightChild(j) < heap.size();
		}
		public int size() {
			return heap.size();
		}
		public int compare(int i, int j) {
			Vertex x = heap.get(i);
			Vertex y = heap.get(j);
			if(x.d > y.d) {
				return 1;
			}
			else if(x.d == y.d) {
				if(x.numMoves > y.numMoves) return 1;
				else if(x.numMoves == y.numMoves) return 0;
				else return -1;
			}
			else {
				return -1;
			}
		}
		private void percDown(int j) {
			while(hasLeft(j)) {
				int i = leftChild(j);
				int smaller = i;
				if(hasRight(j)) {
					int k = rightChild(j);
					if(compare(i, k) > 0) {
						smaller = k;
					}
				}
				if(compare(j, smaller) <= 0) {
					heap.get(j).pqPos = j;
					return;
				}
				Vertex temp = heap.get(j);
				heap.set(j, heap.get(smaller));
				heap.set(smaller, temp);
				heap.get(j).pqPos = j;
				j = smaller;
			}
			if(heap.size() != 0) heap.get(j).pqPos = j;
		}
		public void percUp(int j) {
			while(j > 0) {
				int p = parent(j);
				if(compare(p, j) <= 0) {
					heap.get(j).pqPos = j;
					return;
				}
				Vertex temp = heap.get(j);
				heap.set(j, heap.get(p));
				heap.set(p, temp);
				heap.get(j).pqPos = j;
				j = p;
			}
			if(j == 0 && size() != 0) heap.get(0).pqPos = 0;
		}
		public Vertex deleteMin() {
			if(size() == 0) return null;
			Vertex min = heap.get(0);
			heap.set(0, heap.get(heap.size()-1));
			heap.remove(heap.size()-1);
			percDown(0);
			return min;
		}
		public void insert(Vertex v) {
			heap.add(v);
			percUp(heap.size()-1);
		}		
		public Vertex min() {
			return heap.get(0);
		}
		public Vertex delete(int j) {
			Vertex deleted = heap.get(j);
			heap.set(j, heap.get(heap.size()-1));
			heap.remove(heap.size()-1);
			percDown(j);
			return deleted;
		}
		public void print() {
			int i;
			for(i=0;i<10;i++) {
				System.out.println(heap.get(i).d);
			}
			System.out.println();
		}
	}
	
	public static class Graph {	
		public HashMap<String, Vertex> graph;	
		public HashMap<Character, Integer> costs;
		public HashMap<String, ArrayList<Pair>> edges;
		public HeapPriorityQueue pq;
		char[] source;
		char[] destination;
		
		public Graph(char[] s, char[] d) {
			source = s;
			destination = d;
			graph = new HashMap<String, Vertex>();
			costs = new HashMap<Character, Integer>();
			edges = new HashMap<String, ArrayList<Pair>>();
			pq = new HeapPriorityQueue();
		}
		
		public void generateGraph() {
			int i;
			for(i=0;i<9;i++) {
				if(source[i] == 'G') break;
			}
			Stack<Vertex> st = new Stack<Vertex>();
			Vertex vt = new Vertex(source, i);
			vt.d = 0;
			vt.numMoves = 0;
			graph.put(new String(vt.element), vt);
			st.push(vt);
			while(!st.isEmpty()) {
				Vertex v = st.pop();
				pq.insert(v);
				char[] s = v.element;
				int gapPos = v.gapPos;
				int x = gapPos%3;
				int y = gapPos/3;
				Vertex n;
				int c;
				if(x-1 >= 0) {
					char[] temp = s.clone();
					temp[gapPos] = temp[gapPos-1];
					temp[gapPos-1] = 'G';
					String tempstr = new String(temp);
					if(!graph.containsKey(tempstr)) {
						n = new Vertex(temp, gapPos-1);
					}
					else 
						n = graph.get(tempstr);
					c = costs.get(s[gapPos-1]);
					String h1 = "R" + s[gapPos-1];
					String h2 = "L" + s[gapPos-1];
					v.neighbours.add(new Pair(n, c, h1));
					n.neighbours.add(new Pair(v, c, h2));
					if(!graph.containsKey(tempstr)) {						
						st.push(n);
						graph.put(tempstr, n);
					}
				}
				if(x+1 <= 2) {
					char[] temp = s.clone();
					temp[gapPos] = temp[gapPos+1];
					temp[gapPos+1] = 'G';
					String tempstr = new String(temp);
					if(!graph.containsKey(tempstr)) {
						n = new Vertex(temp, gapPos+1);
					}
					else 
						n = graph.get(tempstr);
					c = costs.get(s[gapPos+1]);
					String h1 = "L" + s[gapPos+1];
					String h2 = "R" + s[gapPos+1];
					v.neighbours.add(new Pair(n, c, h1));
					n.neighbours.add(new Pair(v, c, h2));
					if(!graph.containsKey(new String(temp))) {
						st.push(n);
						graph.put(tempstr, n);
					}
				}
				if(y-1 >= 0) {
					char[] temp = s.clone();
					temp[gapPos] = temp[gapPos-3];
					temp[gapPos-3] = 'G';
					String tempstr = new String(temp);
						if(!graph.containsKey(tempstr)) {
							n = new Vertex(temp, gapPos-3);
						}
					else 
						n = graph.get(tempstr);
					c = costs.get(s[gapPos-3]);
					String h1 = "D" + s[gapPos-3];
					String h2 = "U" + s[gapPos-3];
					v.neighbours.add(new Pair(n, c, h1));
					n.neighbours.add(new Pair(v, c, h2));
					if(!graph.containsKey(new String(temp))) {
						st.push(n);
						graph.put(tempstr, n);
					}
				}
				if(y+1 <= 2) {
					char[] temp = s.clone();
					temp[gapPos] = temp[gapPos+3];
					temp[gapPos+3] = 'G';
					String tempstr = new String(temp);
					if(!graph.containsKey(tempstr)) {
						n = new Vertex(temp, gapPos+3);
					}
					else 
						n = graph.get(tempstr);
					c = costs.get(s[gapPos+3]);
					String h1 = "U" + s[gapPos+3];
					String h2 = "D" + s[gapPos+3];
					v.neighbours.add(new Pair(n, c, h1));
					n.neighbours.add(new Pair(v, c, h2));
					if(!graph.containsKey(new String(temp))) {
						st.push(n);
						graph.put(tempstr, n);
					}
				}			
			}
		}
		
		public String shortestPath() {
			Vertex end = graph.get(new String(destination));
//			boolean found = false;
			while(pq.size() != 0) {
				Vertex v = pq.deleteMin();
				v.inCloud = true;
				if(end != null && v.element.equals(end.element)) {
//					found = true;
					end = v;
					break;
				}
				
				int j = v.neighbours.size();
				for(int i = 0; i< j ;i++) {
					Pair p = v.neighbours.get(i);
					Vertex n = p.first;
					if(!n.inCloud) {
						int dist = p.second + v.d;
						if(dist < n.d || (dist == n.d && v.numMoves+1 < n.numMoves)) {
							n.d = dist;
							n.prev = v;
							n.move = p.move;
							n.numMoves = v.numMoves + 1;
							pq.percUp(n.pqPos);
						}
					}
				}
			}
			Vertex temp = end;
			StringBuilder sb = new StringBuilder();
			while(temp != null) {
				if(temp.move != null) {
					sb.append(' ');
					sb.append(temp.move);
				}
				temp = temp.prev;
			}
			sb.reverse();
			return (end.numMoves + " " + end.d + "\n" + sb.toString());
		}
	}
	
	
	
	public static void main(String args[]) {
	 try {
		 FileInputStream fs = new FileInputStream(args[0]);
		 Scanner s = new Scanner(fs);
		 FileOutputStream fos = new FileOutputStream(args[1]);
		 PrintStream ps = new PrintStream(fos);
		 int T = s.nextInt();
		 while(T != 0) {
			 T--;
			 String start = s.next();
			 String end = s.next();
			 int i,j, invs = 0;		 
			 Graph g = new Graph(start.toCharArray(), end.toCharArray());
			 char c = '1';
			 for(i=0;i<8;i++,c++) {
				 g.costs.put(c, s.nextInt());
			 }
			 for(i=0;i<8;i++) {
				 for(j=i+1;j<9;j++) {
					 if(start.charAt(i) != 'G' && start.charAt(j) != 'G') {
						 if(start.charAt(i) > start.charAt(j)) {
							 invs++;
						 }
					 }
					 if(end.charAt(i) != 'G' && end.charAt(j) != 'G') {
						 if(end.charAt(i) > end.charAt(j)) {
							 invs--;
						 }
					 }
				 }
			 }
			 invs = invs < 0 ? -1*invs : invs;
			 if(invs%2 != 0) {
				 ps.println("-1 -1");
				 ps.println();
				 continue;
			 }
			 if(!start.equals(end)) {
				 g.generateGraph();
				 ps.println(g.shortestPath());
			 }
			 else {
				 ps.println("0 0");
				 ps.println();
			 }
		 }
		 s.close();
		 ps.close();
	 }catch(FileNotFoundException e) {
		 System.out.println("File not found");
	 }
	}
}
