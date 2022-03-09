package trie;

import java.util.ArrayList;

/**
 * This class implements a Trie. 
 * 
 * @author Sesh Venugopal
 *
 */
public class Trie {
	
	// prevent instantiation
	private Trie() { }
	
	/**
	 * Builds a trie by inserting all words in the input array, one at a time,
	 * in sequence FROM FIRST TO LAST. (The sequence is IMPORTANT!)
	 * The words in the input array are all lower case.
	 * 
	 * @param allWords Input array of words (lowercase) to be inserted.
	 * @return Root of trie with all words inserted from the input array
	 */
	public static TrieNode buildTrie(String[] allWords) {
		if (allWords.length == 0) {
	            return null;
	        }
		if(allWords.length==1) {
			TrieNode root= new TrieNode(null, null, null);
			root.firstChild = buildNode(0,(short) 0,(short) (allWords[0].length()-1));
			return root;
		}
		TrieNode root= new TrieNode(null, null, null);
	       for (int i = 0; i < allWords.length; i++) {
	            if (i == 0) {
	            	root.firstChild = buildNode(0,(short) 0,(short) (allWords[0].length()-1));
	            }
	            
	            TrieNode ptr = root.firstChild;
	            TrieNode parent = root;
	            String word = allWords[i];
	            short startIndex = 0;

	            while (ptr != null) {
	            	char a = allWords[ptr.substr.wordIndex].charAt(startIndex);	    

	            	if(word.charAt(startIndex) != a) {
	            		if(ptr.sibling!=null){
	  	                  parent = ptr;
	  	                  ptr = ptr.sibling;
	  	                  System.out.println("reached");
	  	                }
	  	                else {	  	                    
	  	                    short endIndex= (short)(word.length()-1);
	  	                    TrieNode nodeF = buildNode(i, startIndex, endIndex);
	  	                    ptr.sibling = nodeF;	                    

	  	                } 
	            	}
	            	
	            	else {
	                    short endIndex= 0;
	                    short j;
	                    for ( j= 0; j < word.length(); j++) {	                    	
	                        if (word.charAt(j) != allWords[ptr.substr.wordIndex].charAt(j)||j > ptr.substr.endIndex) {
	                            break;
	                        }                                         
	                    }
	                    endIndex = (short)(j-1);
	                    if (endIndex== ptr.substr.endIndex) {
	                        startIndex = (short) (endIndex+ 1);
	                        parent = ptr;
	                        ptr = ptr.firstChild;
	                    } 
	                    else {
	                        TrieNode node = buildNode(ptr.substr.wordIndex, startIndex, endIndex);
	                        TrieNode NodeS = buildNode(ptr.substr.wordIndex, (short) (endIndex+1), ptr.substr.endIndex);
	                        TrieNode NodeT = buildNode(i, (short) (endIndex+ 1), (short) (word.length() - 1));
	                        
	                        if(parent ==root) {
	                        	parent.firstChild = node;
	                        }
	                        else if(parent.firstChild == ptr) {
	                            parent.firstChild = node;
	                        } 
	                        else {
	                            parent.sibling = node;
	                        }
	                        buildParent(NodeS,NodeT,node,ptr);
	                        if (ptr.firstChild != null) {
	                            NodeS.firstChild = ptr.firstChild;
	                        }
	                        break;
	                    }
	                }
	            }
	        }
	       return root;
	}

	private static void buildParent(TrieNode NodeS,TrieNode NodeT,TrieNode node,TrieNode ptr) {
		NodeS.sibling = NodeT;
        node.firstChild = NodeS;
        node.sibling = ptr.sibling;
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
	public static ArrayList<TrieNode> completionList(TrieNode root, String[] allWords, String prefix) {
		ArrayList<TrieNode> list = new ArrayList<TrieNode>();
		//TrieNode ptr;
		TrieNode prev;
		//int count = 0;
		if(root==null) {
			return null;
		}
		prev = findMain(root,prefix,allWords);
		list.addAll(findNode(prev,allWords,prefix));
		return list;
	}
		/*int[] matchIndex = matchingPrefix(allWords,prefix);
		if(matchIndex[0]==0) {
			return null;
		}
		else{					
			for(int i = 0; i<matchIndex.length&&matchIndex[i]>0;i++) {
			String word = allWords[matchIndex[i]-1];
			//System.out.println(matchIndex[i]-1 + "  " + (word.length()-1));
			//System.out.println(prev.substr.toString());
			
			list.addAll( findNode(prev,word,prefix));				
			}		
		}		
		System.out.println(list.toString());
		return list;
	}*/
	
	
	private static TrieNode findMain(TrieNode root,String prefix,String[] word) {
		TrieNode ptr = root.firstChild;
		TrieNode prev = root.firstChild;
		while(ptr!=null) {
		 prev = ptr;
		//for(int i = 0;i<word[i].length()&&i<prefix.length();i++) {
			if(word[ptr.substr.wordIndex].charAt(0)==prefix.charAt(0)) {
				break;
			}
		//}
		ptr = ptr.sibling;
	}
	//System.out.println(prev.substr.toString());
	return prev;
	}

	/*private static int[] matchingPrefix(String[] words,String prefix) {
		//System.out.println("Matching prefix");
		int[] array = new int[words.length];
		int count = 0;
		for(int i = 0;i<array.length;i++) {
			boolean match = false;
			if(prefix.charAt(0) == words[i].charAt(0)) {
				//System.out.println(i + "  " + prefix + "  " + words[i]);				
				for(int j=0; j < words[i].length()&&j < prefix.length();j++) {
					if(prefix.charAt(j) == words[i].charAt(j)) {
						//System.out.println(i + "  " + prefix.charAt(j) + "  " + words[i].charAt(j));
						match = true;
					}
					else {
						//System.out.println(i + "  " + prefix + "  " + words[i]);				
						match = false;
						break;
					}
				}								
			}
			if(match) {
				//System.out.print(i + " " + match);
				array[count] = i+1;
				count++;
				continue;
			}
		}
		//for(int i = 0;i<array.length;i++) {
		//	System.out.println(i + "  " + array[i]);
		//}
		return array;
	}*/
	
	private static ArrayList<TrieNode> findNode(TrieNode root,String[] allWords,String prefix){
		//System.out.println(root.substr.toString());
		ArrayList<TrieNode> answer = new ArrayList<TrieNode>();
	       TrieNode ptr = root;
	       while(ptr!=null){
	         String word = allWords[ptr.substr.wordIndex];
	         String a = word.substring(0, ptr.substr.endIndex+1);
	         if(word.startsWith(prefix) || prefix.startsWith(a)){
	           if(ptr.firstChild!=null){
	               answer.addAll(findNode(ptr.firstChild,allWords,prefix));
	               ptr=ptr.sibling;
	           }else{
	               answer.add(ptr);
	               ptr=ptr.sibling;
	           }
	         }
	         else{
	           ptr = ptr.sibling;
	         }
	       }
	      
	       return answer;
	}
		/*System.out.println(node.substr.toString());
		System.out.println(a + " " + endIndex);
		TrieNode ptr = node;
		 
		TrieNode ptrS = ptr;
		TrieNode prev = ptr;
		if(ptr.substr.wordIndex==a&&ptr.substr.endIndex==endIndex) {
			return ptr;
		}
		else {
			while(ptr!=null) {
				prev = ptr;
				if(ptr.sibling!=null) {
					ptrS = ptr.sibling;
					while(ptrS!=null) {
						if(ptrS.substr.wordIndex==a&&ptrS.substr.endIndex==endIndex) {
							return ptrS;
						}
						TrieNode kids = checkChild(a,endIndex,ptrS);
						if(kids!=null){
							return kids;
						}
						ptrS = ptrS.sibling;						
					}
				}
				ptr = ptr.firstChild;
			}	
		}
		return prev;
	}	*/	
	
	/*private static TrieNode checkChild(int a, short endIndex, TrieNode ptr) {
		TrieNode kids = ptr.firstChild;
		TrieNode yes = null;
		while(kids!=null) {
			if(kids.substr.wordIndex==a&&kids.substr.endIndex==endIndex) {
				yes = kids;
			}			
			kids = kids.sibling;
		}
		return yes;
	}*/
	
	
	private static TrieNode buildNode(int a, short start, short end) {
		Indexes index = new Indexes(a, start, end);
        TrieNode node = new TrieNode(index, null, null);
		return node;
	}
	
	public static void print(TrieNode root, String[] allWords) {
		System.out.println("\nTRIE\n");
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
		}
		
		for (int i=0; i < indent-1; i++) {
			System.out.print("    ");
		}
		System.out.print(" ---");
		if (root.substr == null) {
			System.out.println("root");
		} else {
			System.out.println(root.substr);
		}
		
		for (TrieNode ptr=root.firstChild; ptr != null; ptr=ptr.sibling) {
			for (int i=0; i < indent-1; i++) {
				System.out.print("    ");
			}
			System.out.println("     |");
			print(ptr, indent+1, words);
		}
	}
 }
