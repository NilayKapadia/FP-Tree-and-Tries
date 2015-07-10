package FPTreetrie;

import FPTreetrie.TrieST.Node;
import FPTreetrie.TrieST;
import java.util.*;

public class FPGrowth{
	private static final int R = 256;
	private TrieST<Integer> FPTree;
	public List<List<Node>> paths = new ArrayList<List<Node>>();
	private static Node root;
	public FPGrowth(TrieST<Integer> trie){
		FPTree = trie;
		root = FPTree.root;
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
	/*This method and the method below
	generates a combination of the paths
	by recursion.*/
	private List<List<Character>> combination(List<Character> nodeList) {
		List<Character> tempList = new ArrayList<Character>();
		List<List<Character>> pathsList = new ArrayList<List<Character>>();
		combination(tempList, nodeList, pathsList);
		return pathsList;
		}
	
    private static void combination(List<Character> prefixNodes,List<Character> nodeList, 
    		List<List<Character>> pathsList) {
    	pathsList.add(prefixNodes);
    	for(int i = 0; i<nodeList.size(); i++){
    		List<Character> tempNode = new ArrayList<Character>(prefixNodes);
    		tempNode.add(nodeList.get(i));
    		combination(tempNode, nodeList.subList(i+1, nodeList.size()), pathsList);
    	}
    }
    /*This is simply a routine to convert a list of characters to
    a string and append with the appropriate character.*/
    private List<String> permutationGenerator(List<Character> listOfCharacters, Character c){
    	List<List<Character>> pathList = combination(listOfCharacters);
    	List<String> stringList = new ArrayList<String>();
		for(List<Character> path:pathList){
			StringBuilder stringArray = new StringBuilder(); 
			for(Character newNode: path){
				stringArray.append(newNode);
			}
			stringArray.append(c);
			stringList.add(stringArray.toString());
		}
		return stringList;
    }
	/* This method is the main method that generates all the patterns.
	 */
	public List<String> patternGenerator(Character c){
		List<List<Node>>paths = singlePath(root,c);
		if(paths.size() == 0){
			return null;
		}
		else if(paths.size() == 1){
			List<Node> permutedPath = paths.get(0);
			Node permuteNode = permutedPath.get(permutedPath.size()-1);
			List<Character> tempList = new ArrayList<Character>();
			permutedPath.remove(permutedPath.size()-1);
			permutedPath.remove(0);
			for(Node tempNode:permutedPath){
				if(tempNode.count>=permuteNode.count)
					tempList.add(tempNode.Character);
			}
			List<String> listOfStrings = permutationGenerator(tempList,c);
			return listOfStrings;
		}
		else{
			String basestring = "";
			boolean flag = false;
			for(List<Node>path2:paths){
				String tempString = "";
				path2.remove(path2.size()-1);
				for(Node newNode: path2){
					if(newNode.Character!=null)
					tempString = tempString + newNode.Character.toString();
				}
				if(basestring == "" && flag == false){
					basestring = tempString;
					flag = true;
				}
				else{
					String checkstring = "";
					for(int i = 0; i < basestring.length(); i++){
						Character check = basestring.charAt(i);
						//StdOut.println(check);
						//StdOut.println(tempString);
						for(int j = 0; j<tempString.length(); j++){
							if(tempString.charAt(j) == check){
								checkstring = checkstring + tempString.charAt(j);
							}
						}
					}
					basestring = checkstring;
				}
			}
			        if(basestring!=""){
			        	List<Character> stringArray = new ArrayList<Character>();
			        	for(int i = 0; i < basestring.length(); i++){
			        		stringArray.add(basestring.charAt(i));
			        	}
					List<String> listOfStrings = permutationGenerator(stringArray,c);
					return listOfStrings;
			        }
			        else
			        	return null;
			}
		}

public static void main(String[] args){
	TrieST<Integer> st = new TrieST<Integer>();
    st.put("fcamp",0);
    st.put("fcabm", 1);
    st.put("fb", 2);
    st.put("cbp", 3);
    st.put("fcamp", 4);
    //st.put("the", 5);
    //st.put("sea", 6);
    //st.put("shore", 7);
    st.Treetraversal();
    FPGrowth fpgrowth = new FPGrowth(st);
    Character ch = 'p';
    List<List<Node>> paths = fpgrowth.singlePath(st.root,ch);
    if(paths != null){
    for(List<Node> path1: paths){
    	for(Node node: path1){
    		    if(node.Character!=null)
    			StdOut.print(node.Character);
    	}
    	StdOut.print('\n');
    }
    }
    List<String> stringList = fpgrowth.patternGenerator(ch);
    if(stringList!=null){
    for(String s:stringList){
    	StdOut.println(s);
    }
    }
}
    
}


