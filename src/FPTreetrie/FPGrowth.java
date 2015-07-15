package FPTreetrie;
import FPTreetrie.TrieST.Node;
import FPTreetrie.TrieST;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class FPGrowth {
	private static final int R = 256;
	private TrieST<Integer> FPTree;
	public List<List<Node>> paths = new ArrayList<List<Node>>();
	public FPGrowth(TrieST<Integer> trie){
		FPTree = trie;
	}
	/*This method is used to construct all the paths
	from the root to the desired character.
	It will give all the paths required.
	This method also calls multiPath method
	in case if the degree of a node > 1.
	The recursive routine toggles between singlePath and multiPath routine calls.*/
	public List<List<Node>> singlePath(Node node, Character c){
		Node start = node;
		List<Node> path = new ArrayList<Node>();
		List<List<Node>> pathList = new ArrayList<List<Node>>();
		boolean flag = false;
		while(start!=null){
			path.add(start);
			if(start.Character == c){
				List<Node> copyPath = new ArrayList<Node>(path);
				flag = true;
				pathList.add(copyPath);
			}
			if(start.degree == 1){
				for(char i = 0;i<R;i++){
					if(start.next[i] == null)
						continue;
					start = start.next[i];
					break;
				}
			}
			else if(start.degree == 0){
				start = null;
			}
			else{
				List<List<Node>> distributedPaths = multiPath(start, c);
				if(distributedPaths!=null){
					for(List<Node> path1:distributedPaths){
						List<Node> tempPath = new ArrayList<Node>(path);
						tempPath.addAll(path1);
						pathList.add(tempPath);
					}
					
					flag = true;
				}
				break;
			}
		}
		if(flag)
			return pathList;
		else
			return null;
		
	}
	private List<List<Node>> multiPath(Node node, Character c){
		List<List<Node>> pathList = new ArrayList<List<Node>>();
		for(char i = 0; i < R; i++){
			if(node.next[i] == null)
				continue;
			else{
			List<List<Node>> path = singlePath(node.next[i], c);
			if(path!=null){
			for(List<Node> tempPath:path){
				  pathList.add(tempPath);
				 }
			}
		}
	}
		if(pathList.size() == 0){
			return null;
		}
		else{
			return pathList;
		}
	}
	private String generateString(List<Node> path, int minimumSupport){
		String s = "";
		for(Node newNode:path){
			if(newNode.Character!=null){
				if(newNode.count>= minimumSupport)
					s += newNode.Character.toString();
			}
		}
		return s;
	}
	/*This method and the method below
	generates a combination of the paths
	by recursion.*/
	private List<String> combination(String nodeList) {
		List<String> pathList = new ArrayList<String>();
		combination("",nodeList, pathList);
		return pathList;
		}
	
    private static void combination(String prefixNodes, String NodeList, List<String> pathList) {
        pathList.add(prefixNodes);
    	for(int i = 0; i<NodeList.length(); i++){
    		combination(prefixNodes + NodeList.charAt(i), NodeList.substring(i+1), pathList);
    	}
    }
	 
	public List<String> patternGenerator(TrieST<Integer> trie,Character c) throws IOException{
		FPGrowth FPTree_internal = new FPGrowth(trie);
		List<List<Node>> paths = FPTree_internal.singlePath(trie.root, c);
		if(paths == null)
			return null;
		
		if(paths.size() == 1){
			String path_string = generateString(paths.get(0), FPTree.minSupport);
			List<String> finalString = combination(path_string);
			return finalString;
		}
		else{
			TrieST<Integer> trie_internal = new TrieST<Integer>();
			String fileName = "shellST.txt";
			File file = new File(fileName);
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
			List<String> finalString = new ArrayList<String>();
			FileWriter writer = new FileWriter(file);
			for(List<Node> path: paths){
				List<Node> tempPath = path;
				Node tempNode = path.get(path.size()-1);
				tempPath.remove(tempPath.size()-1);
				String tempString = generateString(tempPath,0);
				if(tempString == ""){
					String s = c.toString();
					finalString.add(s);
					continue;
				}
				for(int i = 0; i<tempNode.count; i++){
					writer.write(tempString);
					writer.write('\n');
				}
			}
			writer.flush();
			writer.close();
			trie_internal.fileReader(fileName, FPTree.minSupport);
			trie_internal.Treetraversal();
			for(Character temp_char: trie_internal.NodeConnect.keySet()){
				List<String> stringPaths = patternGenerator(trie_internal, temp_char);
				if(stringPaths.size() == 0){
					continue;
				}
				else{
					for(int i = 0; i < stringPaths.size(); i++){
						String tempString = stringPaths.get(i);
						tempString += c.toString();
						stringPaths.set(i, tempString);
					}
					finalString.addAll(stringPaths);
				}
			}
			return finalString;
		}
	}
	public Set<String> generateAllPatterns() throws IOException{
		Set<String> listOfPatterns = new HashSet<String>();
		for(Character c: FPTree.NodeConnect.keySet()){
			for(String s: patternGenerator(FPTree, c)){
				if(s == "")
					continue;
				char[] chars = s.toCharArray();
				Arrays.sort(chars);
				String sorted = new String(chars);
				listOfPatterns.add(sorted);
			}
		}
		return listOfPatterns;
	}

public static void main(String[] args) {
	TrieST<Integer> st = new TrieST<Integer>();
    try {
		st.fileReader("TrieDB.txt", 2);
	} catch (IOException e) {
		e.printStackTrace();
	}
    st.Treetraversal();
    FPGrowth fpgrowth = new FPGrowth(st);
    Character ch = 'e';
    List<List<Node>> paths = fpgrowth.singlePath(st.root,ch);
    if(paths != null){
    for(List<Node> path1: paths){
    	for(Node node: path1){
    		    if(node.Character!=null)
    			StdOut.print(node.Character);
    		    if(path1.indexOf(node) == path1.size()-1)
    		    StdOut.print(" " + node.count);
    	}
    	StdOut.print('\n');
    }
    }
    Set<String> patterns = new HashSet<String>();
	try {
		patterns = fpgrowth.generateAllPatterns();
	} catch (IOException e) {
		e.printStackTrace();
	}
    System.out.println("patterns");
    List<String> stringList = new ArrayList<String>();
    stringList.addAll(patterns);
    Collections.sort(stringList,new Comparator<String>(){
		public int compare(String a, String b){
			if(a.length()!=b.length())
			return a.length() > b.length() ? +1 : a.length() < b.length() ? -1 : 0;
			else
				return a.compareTo(b);
		}
	});
    for(String s: stringList){
    	System.out.println(s);
    }
    System.out.println(stringList.size());
    /*List<String> stringList = fpgrowth.patternGenerator(ch);
    if(stringList!=null){
    for(String s:stringList){
    	StdOut.println(s);
    }
    }*/
}
    
}


