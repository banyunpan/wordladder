/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Replace <...> with your actual data.
 * <Student1 Name>
 * <Student1 EID>
 * <Student1 5-digit Unique No.>
 * <Student2 Name>
 * <Student2 EID>
 * <Student2 5-digit Unique No.>
 * Slip days used: <0>
 * Git URL:
 * Fall 2016
 */


//megan 

//hello

//hello again

// hello again again

// hey



package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	static int wordsCompared = 0;
	static String start;
	static String end;
	
	public static void main(String[] args) throws Exception {
		
		/*
		 * 
		 * testing String methods
		 * 
		 */
		/*
		String string = new String("hello");
		System.out.println(string.substring(string.length(), string.length()));
		System.out.println(Character.toString((char)('A' + 1)));
		*/
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default from Stdin
			ps = System.out;			// default to Stdout
		}
		initialize();
		
		ArrayList<String> s = parse(kb);
		//printLadder(s);
		//System.out.println(s.get(0) + " " + s.get(1));
		
		// TODO methods to read in words, output ladder
		
		long startTime = System.nanoTime();
		
		// dfs solution
		ArrayList<String> ladder = getWordLadderDFS(s.get(0), s.get(1));
		//ArrayList<String> ladder2 = getWordLadderBFS(s.get(0), s.get(1));
		
		long endTime = System.nanoTime();
		
		printLadder(ladder);

		System.out.println("DFS took " + (endTime - startTime) + " ns");
		System.out.println(wordsCompared + " words compared");
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
		
		/*
		 *plan:
		 *call makeDictionary() to initialize a data structure for the dictionary.
		 *initialize some static field based on this dictionary, including
		 *some heuristical information that will help nextNearWord find
		 *the best next word to consider.
		 *
		 *the key thing is to create this structure and heuristical info fields.
		 *so far, dictionary is simply a set, no particular order assumed
		 * 
		 */
	}
	
	/**
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of 2 Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		String s = keyboard.next();
		/*
		if(s.equals("/quit")){
			return new ArrayList<String>();
		}
		else{
			ArrayList<String> x = new ArrayList<String>(2);
			x.add(s);
			s = keyboard.next();
			x.add(s);
			return x;
		}*/
		ArrayList<String> words = new ArrayList<String>();
		if(s.equals("/quit")){
			System.exit();
			return words;
		}
		words.add(s.toUpperCase());
		start = new String(s);
		s = keyboard.next();
		if(s.equals("/quit")){
			return new ArrayList<String>();
		}
		words.add(s.toUpperCase());
		end = new String(s);
		return words;//todo

	}
	/*******************************************************************
	 * *****************************************************************
	 * *******************************************************************
	 * 
	 * DFS
	 * 
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		
		// Returned list should be ordered start to end.  Include start and end.
		// Return empty list if no ladder.
		// TODO some code
		
		//start and end are guaranteed to be different
		start = start.toUpperCase();
		end = end.toUpperCase();
		Set<String> dict = makeDictionary();
		if(isNear(start, end)){
			ArrayList<String> ladder = new ArrayList<String>();
			ladder.add(start.toLowerCase());
			ladder.add(end.toLowerCase());
			return ladder;
		}
		
		dict.remove(start);
		dict.remove(end);
		
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(start.toLowerCase());
		
		
		
		DFS(dict, ladder, end);
		
		
		return ladder;
		
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @return
	 */
		
	    public static ArrayList<String> getWordLadderBFS(String start, String end) {
	    	int dictCount = 0; // count for words in dictionary
	    	int matches = 0;   // counts how many letters don't match
	    	int LCount = 0;    // counts which letter is being compared
	    	int qCount = 0;    // counts current place in the queue
	    	int children = 0;  // makes sure word in queue has children
	    	
	    	ArrayList<Node> queue = new ArrayList<Node>(); // queue for BFS path
	    	Set<String> dict = makeDictionary();
	    	ArrayList<String> dictArray = new ArrayList<String>(dict); // turns dictionary into ArrayList
	    	
	    	Node S = new Node(null, start); // makes the start word a Node
	    	queue.add(S); //adds start to queue
	    	
	    	while(!queue.isEmpty()){ // BFS will return null if queue becomes empty
	    	
	    		while(dictCount < dictArray.size()){ // checks every word in the dictionary
	    			while(LCount < 5){ // checks each letter to compare
	    				if(queue.get(qCount).word.charAt(LCount) != dictArray.get(dictCount).charAt(LCount)){
	    					matches = matches + 1;
	    				}
	    				LCount = LCount + 1;
	    			}
	    			if(matches == 0){
	    				dictArray.remove(dictCount);
	    			}
	    			else if(matches == 1){
	    				Node A = new Node(queue.get(qCount), dictArray.get(dictCount)); // makes word from dictionary a node, then adds to queue
	    				queue.add(A);
	    				if(dictArray.get(dictCount).equals(end)){           // if end word is found,
	    					ArrayList<String> wl = new ArrayList<String>(); // creates backwards word ladder, getting from parent
	    					Node n = queue.get(queue.size() - 1);
	    					int c = 0;
	    					while(!n.equals(S)){
	    						wl.add(n.word);
	    						n = n.parent;
	    						c = c + 1;
	    					}
	    					ArrayList<String> WL = new ArrayList<String>(); // creates new queue, reverses it
	    					WL.add(S.word);
	    					while(c > 0){
	    						WL.add(wl.get(c - 1));
	    						c = c - 1;
	    					}
	    					return WL;
	    				}
	    				dictArray.remove(dictCount);
	    				children = children + 1;
	    			}
	    			else{
	    				dictCount = dictCount + 1;
	    			}
	    			LCount = 0;
	    			matches = 0;
	    		}
	    	
	    		if(children == 0){
	    			queue.remove(qCount);
	    			if(queue.get(qCount).equals(null)){
	    				qCount = queue.size() - 1;
	    			}
	    		}
	    		else{
	    			qCount = qCount + 1;
	    		}
	    		children = 0;
	    		dictCount = 0;
	    	}
			
			return null; // replace this line later with real return
		}
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
			//infile = new Scanner (new File("short_dict.txt"));
			//infile = new Scanner(new File("my_dict.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		long startTime = System.nanoTime();
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		long endTime = System.nanoTime();
	//	System.out.println((endTime - startTime) + "ns for makeDictionary");
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		//case insensitive
		if(ladder.size() == 0){
			System.out.println("no word ladder can be found between " +
								start.toLowerCase() + " and " +
								end.toLowerCase() + ".");
		}
		else{
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between "
								+ start.toLowerCase() + " and " + end.toLowerCase() + ".");
		
			for(int x = 0; x < ladder.size(); x++){
				System.out.println(ladder.get(x).toLowerCase());
			}
		}
		
	}
	// TODO
	// Other private static methods here

	private static ArrayList<String> trivialLadder(String s){

		ArrayList<String> trivial = new ArrayList<String>();

		trivial.add(s);
		trivial.add(s);

		return trivial;

	}
	
	private static ArrayList<String> DFS(Set<String> dict, ArrayList<String> ladder,
											String endWord){
		
		//entering this method,
		//we assume that currentWord and endWord are not near
		endWord = endWord.toUpperCase();
		while(!isNear(ladder.get(ladder.size() - 1), endWord)){
			String nearWord = findNextNear(dict, ladder.get(ladder.size() - 1), endWord);
			
			if(nearWord == null){
				//reached a dead end in DFS
				ladder.remove(ladder.size() - 1);
				break;
			}
			wordsCompared++;
			dict.remove(nearWord);
			ladder.add(nearWord);
			if(isNear(nearWord, endWord)){
				ladder.add(endWord);
			}
			else{
				DFS(dict, ladder, endWord);
			}
		}
		
		return ladder;
	}
	private static String findNextNear(Set<String> dict, String word, String key){
		
		String s = word.toUpperCase();
		
		for(int pos = 0; pos < word.length(); pos++){
			//change one letter on word to see if it matches a word in dict
			char letter = s.charAt(pos);
			int i = (key.charAt(pos) - 'A') % 26; // next letter of alphabet to replace with
			int looped = i;
			do { // cycle through the alphabet
				String temp = new String(s.substring(0, pos) +
										Character.toString((char)(i + 'A')) +
										s.substring(pos+1,s.length()));
				if(dict.contains(temp)){
					return temp;
				}
				i = (i + 1) % 26;
				if(i == letter){
					i = (i + 1) % 26;
				}
			}while(i != looped/*(letter - 'A')*/);
		}
		
		return null; // couldn't find something near to word in dict
	}
	private static boolean isNear(String s1, String s2){
		//check if s1 and s2 are at most one letter different
		
		char[] temp1 = s1.toUpperCase().toCharArray();
		char[] temp2 = s2.toUpperCase().toCharArray();
		
		int differences = 0;
		for(int i = 0; i < temp1.length; i++){
				if(temp1[i] != temp2[i]){
					differences++;
				}
				if(differences > 1){
					return false;
				}
		}
		
		return true;//todo
	}
}
