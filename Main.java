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
	
	public static void main(String[] args) throws Exception {
		
		/*
		 * 
		 * testing String methods
		 * 
		 */
		String string = new String("hello");
		System.out.println(string.substring(string.length(), string.length()));
		
		
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
		printLadder(s);
		//System.out.println(s.get(0) + " " + s.get(1));
		
		// TODO methods to read in words, output ladder
		
		// dfs solution
		ArrayList<String> ladder = getWordLadderDFS(s.get(0), s.get(1));
		//ArrayList<String> ladder2 = getWordLadderBFS(s.get(0), s.get(1));
		if(ladder.size() == 0){
			System.out.println("no word ladder can be found between " + s.get(0) + " and " + s.get(1) + ".");
		}
		else{
			System.out.println("a " + (ladder.size()-2) + "-rung word ladder exists between "
								+ s.get(0) + " and " + s.get(1) + ".");
			printLadder(s);
		}
	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
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
			return words;
		}
		words.add(s);
		s = keyboard.next();
		if(s.equals("/quit")){
			return new ArrayList<String>();
		}
		return null;//todo

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
		
		Set<String> dict = makeDictionary();
		// TODO more code
		if(dict.size() == 0){
			System.out.println("empty dictionary");
		}

		if(isNear(start, end)){
			ArrayList<String> ladder = new ArrayList<String>();
			ladder.add(start);
			ladder.add(end);
			return ladder;
		}
		
		dict.remove(start);
		dict.remove(end);
		
		ArrayList<String> ladder = new ArrayList<String>();
		ladder.add(start);
		
		return DFS(dict, ladder, end);
		
	}
	
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		
		// TODO some code
		Set<String> dict = makeDictionary();
		// TODO more code
		
		return null; // replace this line later with real return
	}
    
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			//infile = new Scanner (new File("five_letter_words.txt"));
			infile = new Scanner (new File("short_dict.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
	
	public static void printLadder(ArrayList<String> ladder) {
		for(int x = 0; x < ladder.size(); x++){
			System.out.println(ladder.get(x));
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
		
		while(!isNear(ladder.get(ladder.size() - 1), endWord)){
			String nearWord = findNextNear(dict, ladder.get(ladder.size() - 1));
			
			if(nearWord == null){
				//reached a dead end in DFS
				ladder.remove(ladder.size() - 1);
				break;
			}
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
	private static String findNextNear(Set<String> dict, String word){
		
		String s = word.toUpperCase();
		
		for(int pos = 0; pos < word.length(); pos++){
			//change one letter on word to see if it matches a word in dict
			char letter = s.charAt(pos);
			int i = (letter - 'A' + 1) % 26; // next letter of alphabet to replace with
			while(i != letter){ // cycle through the alphabet
				String temp = new String(s.substring(0, pos) +
										Character.toString((char)(letter + 'A')) +
										s.substring(pos+1,s.length()));
				if(dict.contains(temp)){
					return temp;
				}
				i = (i + 1) % 26;
			}
		}
		
		return null; // couldn't find something near to word in dict
	}
	private static boolean isNear(String s1, String s2){
		//check if s1 and s2 are at most one letter different
		
		char[] temp1 = s1.toUpperCase().toCharArray();
		char[] temp2 = s2.toUpperCase().toCharArray();
		
		int differences = 0;
		for(int i = 0; i < temp1.length; i++){
			for(int j = 0; j < temp2.length; j++){
				if(temp1[i] != temp2[j]){
					differences++;
				}
				if(differences > 1){
					return false;
				}
			}
		}
		
		return true;//todo
	}
}
