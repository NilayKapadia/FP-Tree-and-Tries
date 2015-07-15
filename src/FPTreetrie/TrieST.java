package FPTreetrie;

import java.util.*;
import java.io.*;



/*************************************************************************
 *  Compilation:  javac TrieST.java
 *  Execution:    java TrieST < words.txt
 *  Dependencies: StdIn.java
 *
 *  A string symbol table for extended ASCII strings, implemented
 *  using a 256-way trie.
 *
 *  % java TrieST < shellsST.txt 
 *  by 4
 *  sea 6
 *  sells 1
 *  she 0
 *  shells 3
 *  shore 7
 *  the 5
 *
 *************************************************************************/

/**
 *  The <tt>TrieST</tt> class represents an symbol table of key-value
 *  pairs, with string keys and generic values.
 *  It supports the usual <em>put</em>, <em>get</em>, <em>contains</em>,
 *  <em>delete</em>, <em>size</em>, and <em>is-empty</em> methods.
 *  It also provides character-based methods for finding the string
 *  in the symbol table that is the <em>longest prefix</em> of a given prefix,
 *  finding all strings in the symbol table that <em>start with</em> a given prefix,
 *  and finding all strings in the symbol table that <em>match</em> a given pattern.
 *  A symbol table implements the <em>associative array</em> abstraction:
 *  when associating a value with a key that is already in the symbol table,
 *  the convention is to replace the old value with the new value.
 *  Unlike {@link java.util.Map}, this class uses the convention that
 *  values cannot be <tt>null</tt>&mdash;setting the
 *  value associated with a key to <tt>null</tt> is equivalent to deleting the key
 *  from the symbol table.
 *  <p>
 *  This implementation uses a 256-way trie.
 *  The <em>put</em>, <em>contains</em>, <em>delete</em>, and
 *  <em>longest prefix</em> operations take time proportional to the length
 *  of the key (in the worst case). Construction takes constant time.
 *  The <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes constant time.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/52trie">Section 5.2</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 */
public class TrieST<Value> {
    private static final int R = 256;        // extended ASCII
    public int[] LetterCount = new int[R];
    public Node root;      // root of trie
    private int N;          // number of keys in trie
    public int minSupport;
    // R-way trie node
    public static class Node {
        private Object val;
        public Character Character;
        public Node[] next = new Node[R];
        public int count = 0;
        public int degree = 0;
    }
    Comparator<Character> integerComparator = new Comparator<Character>() {
        @Override public int compare(Character a, Character b) {
        	int a1 = a;
        	int b1 = b;	
        	if(LetterCount[a1]<LetterCount[b1]){
        		return 1;
        	}
        	else if(LetterCount[a1] ==LetterCount[b1]){
        		if(a1<b1)
        			return -1;
        		else if(a1 == b1)
        			return 0;
        		else
        			return 1;
        	}
        	else
        		return -1;
        }
    };
    public Map<Character, List<Node>> NodeConnect = new TreeMap<Character, List<Node>>(integerComparator);

   /**
     * Initializes an empty string symbol table.
     */

    /**
     * Returns the value associated with the given key.
     * @param key the key
     * @return the value associated with the given key if the key is in the symbol table
     *     and <tt>null</tt> if the key is not in the symbol table
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public Value get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        @SuppressWarnings("unchecked")
		Value val = (Value) x.val;
		return val;
    }

    /**
     * Does this symbol table contain the given key?
     * @param key the key
     * @return <tt>true</tt> if this symbol table contains <tt>key</tt> and
     *     <tt>false</tt> otherwise
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public boolean contains(String key) {
        return get(key) != null;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

    /**
     * Inserts the key-value pair into the symbol table, overwriting the old value
     * with the new value if the key is already in the symbol table.
     * If the value is <tt>null</tt>, this effectively deletes the key from the symbol table.
     * @param key the key
     * @param startIndex the value
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void put(String key, int startIndex) {
        root = put(root, key, startIndex, 0);
    }

    private Node put(Node x, String key, int startIndex, int d) {
        if (x == null){
        	x = new Node();
        }
        if (d == key.length()) {
            if (x.val == null) N++;
            x.val = startIndex;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, startIndex, d+1);
        x.next[c].Character = c;
        x.next[c].count++;
        LetterCount[(int)c]++;
        return x;
    }

    /**
     * Returns the number of key-value pairs in this symbol table.
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        return N;
    }

    /**
     * Is this symbol table empty?
     * @return <tt>true</tt> if this symbol table is empty and <tt>false</tt> otherwise
     */
    public boolean isEmpty() {
        return size() == 0;
    }
    private void process(Node x){
    	if(!NodeConnect.containsKey(x.Character)){
    		List<Node> nodelist = new LinkedList<Node>();
    		nodelist.add(x);
    		NodeConnect.put(x.Character, nodelist);
    	}
    	else{
    		List<Node> nodelist = NodeConnect.get(x.Character);
    		nodelist.add(x);
    	}
    }
    
    public void Treetraversal(){
    	Stack<Node> st = new Stack<Node>();
    	st.push(root);
    	while(!st.isEmpty()){
    		Node top = st.peek();
    		if(top!=root){
    			process(top);
    		}
    		st.pop();
    		int count  = 0;
    		for(char c = 0; c <R; c++){;
    			if(top.next[c] == null){
    				continue;
    			}
    			st.push(top.next[c]);
    			count++;
    		}
    		top.degree = count;
    	}
    }

    /**
     * Returns all keys in the symbol table as an <tt>Iterable</tt>.
     * To iterate over all of the keys in the symbol table named <tt>st</tt>,
     * use the foreach notation: <tt>for (Key key : st.keys())</tt>.
     * @return all keys in the sybol table as an <tt>Iterable</tt>
     */
    public void printTable(){
    	for(Map.Entry<Character, List<Node>> entry : NodeConnect.entrySet()){
        	String s = entry.getKey().toString() + ' ' + LetterCount[entry.getKey()];
        	StdOut.println(s);
        }
    }
    public Iterable<String> keys() {
        return keysWithPrefix("");
    }

    /**
     * Returns all of the keys in the set that start with <tt>prefix</tt>.
     * @param prefix the prefix
     * @return all of the keys in the set that start with <tt>prefix</tt>,
     *     as an iterable
     */
    public Iterable<String> keysWithPrefix(String prefix) {
        Queue<String> results = new Queue<String>();
        Node x = get(root, prefix, 0);
        collect(x, new StringBuilder(prefix), results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, Queue<String> results) {
        if (x == null) return;
        if (x.val != null) results.enqueue(prefix.toString());
        for (char c = 0; c < R; c++) {
            prefix.append(c);
            collect(x.next[c], prefix, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns all of the keys in the symbol table that match <tt>pattern</tt>,
     * where . symbol is treated as a wildcard character.
     * @param pattern the pattern
     * @return all of the keys in the symbol table that match <tt>pattern</tt>,
     *     as an iterable, where . is treated as a wildcard character.
     */
    public Iterable<String> keysThatMatch(String pattern) {
        Queue<String> results = new Queue<String>();
        collect(root, new StringBuilder(), pattern, results);
        return results;
    }

    private void collect(Node x, StringBuilder prefix, String pattern, Queue<String> results) {
        if (x == null) return;
        int d = prefix.length();
        if (d == pattern.length() && x.val != null)
            results.enqueue(prefix.toString());
        if (d == pattern.length())
            return;
        char c = pattern.charAt(d);
        if (c == '.') {
            for (char ch = 0; ch < R; ch++) {
                prefix.append(ch);
                collect(x.next[ch], prefix, pattern, results);
                prefix.deleteCharAt(prefix.length() - 1);
            }
        }
        else {
            prefix.append(c);
            collect(x.next[c], prefix, pattern, results);
            prefix.deleteCharAt(prefix.length() - 1);
        }
    }

    /**
     * Returns the string in the symbol table that is the longest prefix of <tt>query</tt>,
     * or <tt>null</tt>, if no such string.
     * @param query the query string
     * @throws NullPointerException if <tt>query</tt> is <tt>null</tt>
     * @return the string in the symbol table that is the longest prefix of <tt>query</tt>,
     *     or <tt>null</tt> if no such string
     */
    public String longestPrefixOf(String query) {
        int length = longestPrefixOf(root, query, 0, -1);
        if (length == -1) return null;
        else return query.substring(0, length);
    }

    // returns the length of the longest string key in the subtrie
    // rooted at x that is a prefix of the query string,
    // assuming the first d character match and we have already
    // found a prefix match of given length (-1 if no such match)
    private int longestPrefixOf(Node x, String query, int d, int length) {
        if (x == null) return length;
        if (x.val != null) length = d;
        if (d == query.length()) return length;
        char c = query.charAt(d);
        return longestPrefixOf(x.next[c], query, d+1, length);
    }

    /**
     * Removes the key from the set if the key is present.
     * @param key the key
     * @throws NullPointerException if <tt>key</tt> is <tt>null</tt>
     */
    public void delete(String key) {
        root = delete(root, key, 0);
    }

    private Node delete(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) {
            if (x.val != null) N--;
            x.val = null;
        }
        else {
            char c = key.charAt(d);
            x.next[c] = delete(x.next[c], key, d+1); 
        }

        // remove subtrie rooted at x if it is completely empty
        if (x.val != null) return x;
        for (int c = 0; c < R; c++)
            if (x.next[c] != null)
                return x;
        return null;
    }
    public void fileReader(String fileName, int minimumSupport) throws IOException{
    	File file = new File(fileName);
    	String path = file.getAbsolutePath();
    	FileReader filePath = new FileReader(path);
    	BufferedReader textReader = new BufferedReader(filePath);
    	String s;
    	Map<Character, Integer> characterList = new TreeMap<Character, Integer>();
    	while((s = textReader.readLine())!=null){
    		for(int i = 0; i < s.length(); i++){
    			if(characterList.get(s.charAt(i))!=null)
    			characterList.put(s.charAt(i), characterList.get(s.charAt(i))+1);
    			else
    			characterList.put(s.charAt(i), 1);
    		}
    	}
    	minSupport = minimumSupport;
    	textReader.close();
    	filePath.close();
    	FileReader filePath1 = new FileReader(path);
    	BufferedReader newtextReader = new BufferedReader(filePath1);
    	int index = 0;
    	while((s = newtextReader.readLine())!=null){
    		List<Character> list = new ArrayList<Character>();
    		for(int i = 0; i < s.length(); i++){
    			if(characterList.get(s.charAt(i))>=minimumSupport){
    				list.add(s.charAt(i));
    			}
    		}
    		Collections.sort(list, new Comparator<Character>(){
    			public int compare(Character a, Character b){
    				if(characterList.get(a)!=characterList.get(b))
    				return (characterList.get(b)).compareTo(characterList.get(a));
    				else
    					return a.compareTo(b);
    			}
    		});
    		StringBuilder tempString = new StringBuilder(list.size());
    		for(Character ch: list){
    			tempString.append(ch);
    		}
    		put(tempString.toString(), index);
    		index++;
    	}
    	newtextReader.close();
    	filePath1.close();
    }

    /**
     * Unit tests the <tt>TrieST</tt> data type.
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        TrieST<Integer> st = new TrieST<Integer>();
        st.put("fcamp",0);
        st.put("fcabm", 1);
        st.put("fb", 2);
        st.put("cbp", 3);
        st.put("fcamp", 4);
        st.Treetraversal();
        //st.printTable();
        TrieST<Integer> st1 = new TrieST<Integer>();
        st1.fileReader("TrieDB.txt",2);
        st1.Treetraversal();
        st1.printTable();
        // print results
        if (st1.size() < 100) {
            StdOut.println("keys(\"\"):");
            for (String key : st1.keys()) {
                StdOut.println(key + " " + st1.get(key));
            }
            StdOut.println();
        }

        StdOut.println("longestPrefixOf(\"shellsort\"):");
        StdOut.println(st1.longestPrefixOf("shellsort"));
        StdOut.println();

        StdOut.println("longestPrefixOf(\"quicksort\"):");
        StdOut.println(st1.longestPrefixOf("quicksort"));
        StdOut.println();

        StdOut.println("keysWithPrefix(\"shor\"):");
        for (String s : st1.keysWithPrefix("shor"))
            StdOut.println(s);
        StdOut.println();

        StdOut.println("keysThatMatch(\".he.l.\"):");
        for (String s : st1.keysThatMatch(".he.l."))
            StdOut.println(s);
    }
}



