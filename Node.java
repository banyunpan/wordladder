package assignment3;

public class Node{
	public String word;
	public Node parent;
	
	public Node(Node p, String w){
		parent = p;
		word = w;
	}
}