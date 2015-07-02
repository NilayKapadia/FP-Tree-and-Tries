import java.util.Stack;

class Trie_Node{
	int occurence;
	int value;
	int flag;
	Trie_Node[] children;
	Trie_Node next;
	
	public Trie_Node(){
		super();
		this.children=new Trie_Node[256];
		this.next= new Trie_Node();
		this.flag=0;
		for(int i=0;i<256;i++){
			this.children[i]=null;
		}
		
	}
}
public class Trie{
	
	private Trie_Node root;
	int count;
	public Trie(){
		root = new Trie_Node();
		count=0;
	}
	public void add(String keyy){
		
		String key = keyy.toLowerCase();
		Trie_Node curr = this.root;
		this.count++;
		for(int i=0;i<key.length();i++){
			int c = key.charAt(i)-'a';
			if(curr.children[c]==null){
				curr.children[c]= new Trie_Node();
				curr.children[c].occurence++;
			}
			else{
				curr.children[c].occurence++;
			}
			curr=curr.children[c];
			
		}
		curr.value=count;
	}
	
	public boolean search(String keyy) {
		String key = keyy.toLowerCase();
		Trie_Node curr = this.root;
		this.count++;
		for (int i = 0; i < key.length(); i++) {
			int c = key.charAt(i) - 'a';
			if (curr.children[c] == null) {
				return false;
			}
			curr = curr.children[c];
		}
		return (curr.value != 0);
	}
	//Unit Test Case For Checking of Code
	public static void main(String[] args) {
		String[] keys = new String[] { "A", "ans", "and", "an", "Pineapple",
				"Apple", "Orange", "Banana" };
		Trie t = new Trie();
		for (String k : keys) {
			t.add(k);
		}
		System.out.println(t.search("Pineapple"));
	}
 
}
