package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class TrieSol {
	
	/**
	 * Root node of this trie.
	 */
	TrieNode root;
	
	/**
	 * Initializes a trie with words to be indexed, and root node set to
	 * null fields.
	 * 
	 * @param words
	 */
	public TrieSol() {
		root = new TrieNode(null, null, null);
	}
	
	/**
	 * Builds a trie by inserting all words in the input list, one at a time,
	 * in order from first to last. (The sequence is important!)
	 * The words in the input list are all lower case.
	 * 
	 * @param allWords Input list of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input list
	 */
	public static TrieNode buildTrie(String[] allWords) {
		// create a new root node with nulls
		TrieNode root = new TrieNode(null,null,null);
		// insert words in tree
		for (int i=0; i < allWords.length; i++) {
			insertWord(allWords[i], root, allWords, 0, i);
		}
		return root;
	}
	
	/**
	 * Given a trie, returns the "completion list" for a prefix, i.e. all the leaf nodes in the 
	 * trie whose words start with this prefix. 
	 * For instance, if the trie had the words "bear", "bull", "stock", and "bell",
	 * the completion list for prefix "b" would be the leaf nodes that hold "bear", "bull", and "bell"; 
	 * for prefix "be", the completion would be the leaf nodes that hold "bear" and "bell", 
	 * and for prefix "bell", completion would be the leaf node that holds "bell". 
	 * (The last example shows that an input prefix can be an entire word.) 
	 * The order of returned leaf nodes DOES NOT MATTER. So, for prefix "be",
	 * the returned list of leaf nodes can be either hold [bear,bell] or [bell,bear].
	 *
	 * @param root Root of Trie that stores all words to search on for completion lists
	 * @param allWords Array of words that have been inserted into the trie
	 * @param prefix Prefix to be completed with words in trie
	 * @return List of all leaf nodes in trie that hold words that start with the prefix, 
	 * 			order of leaf nodes does not matter.
	 *         If there is no word in the tree that has this prefix, null is returned.
	 */
	public static ArrayList<TrieNode> completionList(TrieNode root,
										String[] allWords, String prefix) {
		// search until all of the prefix is matched
		TrieNode ptr = root.firstChild;
		int i=0;
		while (ptr != null) {
			if (prefix.charAt(i) == allWords[ptr.substr.wordIndex].charAt(ptr.substr.startIndex)) {
				int ix=ptr.substr.startIndex+1;
				i++;
				while (i < prefix.length() && ix <= ptr.substr.endIndex) {
					if (prefix.charAt(i) == allWords[ptr.substr.wordIndex].charAt(ix)) {
						i++;
						ix++;
					} else {
						// can't match, return
						return null;
					}
				}
				if (i == prefix.length()) {  // prefix eaten up, gather matches
					// return list
					ArrayList<TrieNode> matches = new ArrayList<>();
					gatherMatches(ptr, matches);
					return matches;
				}
				// descend to eat more
				ptr = ptr.firstChild;
			} else {
				ptr = ptr.sibling;
			}
		}
		// no match
		return null;
	}
	
	
	/**
	 * Gather all words at leaves of the subtree rooted at ptr
	 * 
	 * @param ptr Subtree under which all leaves need to be gathered
	 * @param matches To be populated with list of leaf nodes
	 */
	private static void gatherMatches(TrieNode ptr, ArrayList<TrieNode> matches) {
		if (ptr == null) {
			return;
		}
		if (ptr.firstChild == null) { // leaf node, add word to matches
			matches.add(ptr);
			return;
		}
		// recurse on all of the children
		TrieNode ctn = ptr.firstChild;
		while (ctn != null) {
			gatherMatches(ctn, matches);
			ctn = ctn.sibling;
		}
	}
	
	/**
	 * Recursive method to insert a word into the tree. This method returns without
	 * making any changes if any of the following is true:
	 *   - The word is already in the tree, OR
	 *   - The word is a prefix of a word that is already in the tree, OR
	 *   - A word in the tree is a prefix of this word
	 * 
	 * @param word Word to insert
	 * @param root Root of the trie
	 * @param words Array list of all words so far
	 * @param i Current index of word from where match should start when descending
	 * @param index Index of word in array
	 */
	private static void insertWord(String word, TrieNode root, String[] words,
									int i, int index) {
		
		// check all nodes at this level for a match with first character
		TrieNode ptr = root.firstChild, prev=root;
		int ix=0;
		while (ptr != null) {
			if (word.charAt(i) == words[ptr.substr.wordIndex].charAt(ptr.substr.startIndex)) {
				ix=ptr.substr.startIndex+1;
				i++;
				while (i < word.length() && ix <= ptr.substr.endIndex) {
					if (word.charAt(i) == words[ptr.substr.wordIndex].charAt(ix)) {
						i++;
						ix++;
					} else {
						break;
					}
				}
				break;
			}
			prev = ptr;
			ptr = ptr.sibling;
		}
			
		// termination 1: this word is a prefix of an existing word
		if (i == word.length()) {  
			return;
		}
			
		// termination 2: none of the characters were matched
		if (ptr == null) {
			// insert new node here
			Indexes idxs = new Indexes(index, (short)i, (short)(word.length()-1));
			TrieNode cptr = new TrieNode(idxs, null, null);
			if (prev == root) {
				prev.firstChild = cptr;
			} else {
				prev.sibling = cptr;
			}
			return;
		}	
		
		// if all characters at this node have been matched
		if (ix == (ptr.substr.endIndex+1)) {
			// termination 3: if we are at leaf node => existing word is prefix of this word
			if (ptr.firstChild == null) {
				return;
			}
			// otherwise, simple descent (Case 1)
			insertWord(word, ptr, words, i, index);
			return;
		}
		
		// Case 2: need to break node into two, trailing part of existing becomes first child
		// and new node becomes sibling 
		// the insertion can be done right here, but might be simpler to recurse one more time
		Indexes idxs = new Indexes(ptr.substr.wordIndex, (short)ix, ptr.substr.endIndex);
		// any existing first child becomes first child of new node
		ptr.firstChild = new TrieNode(idxs, ptr.firstChild, null);
		ptr.substr.endIndex = (short)(ix-1);
		insertWord(word, ptr, words, i, index);
	}
	
	public static void print(TrieNode root, String[] allWords) {
		print(root, 1, allWords);
	}
	
	private static void print(TrieNode root, int indent, String[] words) {
		if (root == null) {
			return;
		}
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		
		if (root.substr != null) {
			String pre = words[root.substr.wordIndex]
							.substring(0, root.substr.endIndex+1);
			System.out.println("      " + pre);
			//System.out.println("      " + words[root.substr.wordIndex]);
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		System.out.println("(" + root.substr + ")");
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
