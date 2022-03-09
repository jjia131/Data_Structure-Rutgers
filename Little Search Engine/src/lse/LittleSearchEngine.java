package lse;

import java.io.*;
import java.util.*;

/**
 * This class builds an index of keywords. Each keyword maps to a set of pages
 * in which it occurs, with frequency of occurrence in each page.
 *
 */
public class LittleSearchEngine {

	/**
	 * This is a hash table of all keywords. The key is the actual keyword, and the
	 * associated value is an array list of all occurrences of the keyword in
	 * documents. The array list is maintained in DESCENDING order of frequencies.
	 */
	HashMap<String, ArrayList<Occurrence>> keywordsIndex;

	/**
	 * The hash set of all noise words.
	 */
	HashSet<String> noiseWords;

	/**
	 * Creates the keyWordsIndex and noiseWords hash tables.
	 */
	public LittleSearchEngine() {
		keywordsIndex = new HashMap<String, ArrayList<Occurrence>>(1000, 2.0f);
		noiseWords = new HashSet<String>(100, 2.0f);
	}

	/**
	 * Scans a document, and loads all keywords found into a hash table of keyword
	 * occurrences in the document. Uses the getKeyWord method to separate keywords
	 * from other words.
	 * 
	 * @param docFile Name of the document file to be scanned and loaded
	 * @return Hash table of keywords in the given document, each associated with an
	 *         Occurrence object
	 * @throws FileNotFoundException If the document file is not found on disk
	 */
	public HashMap<String, Occurrence> loadKeywordsFromDocument(String docFile) throws FileNotFoundException {
		/** COMPLETE THIS METHOD **/
		if (docFile == null) {
			throw new FileNotFoundException();
		}
		HashMap<String, Occurrence> hashmap = new HashMap<String, Occurrence>();
		Scanner sc = new Scanner(new File(docFile));
		while (sc.hasNext()) {
			String word = sc.next();
			word = getKeyword(word);
			if (getKeyword(word) != null) {
				if (!hashmap.containsKey(getKeyword(word))) {
					hashmap.put(getKeyword(word), new Occurrence(docFile, 1));
				} else {
					hashmap.get(getKeyword(word)).frequency = hashmap.get(getKeyword(word)).frequency + 1;
				}
			}
		}
		sc.close();
		return hashmap;
	}

	/**
	 * Merges the keywords for a single document into the master keywordsIndex hash
	 * table. For each keyword, its Occurrence in the current document must be
	 * inserted in the correct place (according to descending order of frequency) in
	 * the same keyword's Occurrence list in the master hash table. This is done by
	 * calling the insertLastOccurrence method.
	 * 
	 * @param kws Keywords hash table for a document
	 */
	public void mergeKeywords(HashMap<String, Occurrence> kws) {
		ArrayList<Occurrence> list = new ArrayList<Occurrence>();
		for (String key : kws.keySet()) {
			if (!keywordsIndex.containsKey(key)) {
				keywordsIndex.put(key, new ArrayList<>());
				keywordsIndex.get(key).add(kws.get(key));
			} else {
				// inserts the where the word is in the proper location of the master index w/
				// insertLastOccurrence
				keywordsIndex.get(key).add(kws.get(key));
				insertLastOccurrence(keywordsIndex.get(key));
			}
			/*
			if (keywordsIndex.containsKey(key)) {
				list = keywordsIndex.get(key);
				list.add(kws.get(key));
				insertLastOccurrence(list);
				keywordsIndex.put(key, list);
			} else {
				ArrayList<Occurrence> occurList = new ArrayList<Occurrence>();
				occurList.add(kws.get(key));
				keywordsIndex.put(key, occurList);
			}*/
		}
	}

	/**
	 * Given a word, returns it as a keyword if it passes the keyword test,
	 * otherwise returns null. A keyword is any word that, after being stripped of
	 * any trailing punctuation(s), consists only of alphabetic letters, and is not
	 * a noise word. All words are treated in a case-INsensitive manner.
	 * 
	 * Punctuation characters are the following: '.', ',', '?', ':', ';' and '!' NO
	 * OTHER CHARACTER SHOULD COUNT AS PUNCTUATION
	 * 
	 * If a word has multiple trailing punctuation characters, they must all be
	 * stripped So "word!!" will become "word", and "word?!?!" will also become
	 * "word"
	 * 
	 * See assignment description for examples
	 * 
	 * @param word Candidate word
	 * @return Keyword (word without trailing punctuation, LOWER CASE)
	 */
	public String getKeyword(String word) {
		/** COMPLETE THIS METHOD **/
		// System.out.println(word);
		ArrayList<Character> punctuations = new ArrayList<Character>(Arrays.asList('.', ',', '?', ';', ':', '!'));
		if (word == null) {
			return null;
		}
		if (word.matches("[,-.'0-9]+")) {
			// System.out.println(word);
			return null;
		}

		boolean pureWord = true;
		char[] chars = word.toCharArray();
		//System.out.println("reach " + chars.length);
		String keyWord = null;
		int count = 0;
		for (count = 0; count < chars.length; count++) {
			if (!Character.isLetter(chars[count])) {
				pureWord = false;
				// System.out.println(pureWord + " " + count + " " + chars[count]);
				break;
			}
		}
		int end = count;
		if (pureWord) {
			keyWord = word.substring(0, count);
		} else {
			if(!punctuations.contains(chars[count])) {
				return null;
			}
			for (int i = count+1; i < chars.length; i++) {
				//System.out.println( i + " " + punctuations.contains(chars[i]));		
				if (Character.isLetter(chars[i])) {
					return null;
				} else if (punctuations.contains(chars[i])) {
					count++;
					continue;
				} else {
					//System.out.println( count + " " + chars[count]);
					return null;
				}
			}
			if (count + 1 == chars.length) {
				if (!punctuations.contains(chars[count])) {
					//System.out.println( count + " " + chars[count]);					
					//System.out.println( count + " " + chars[count]);	
					return null;
				}
			}
		}		
		if (count == chars.length - 1) {
			keyWord = word.substring(0, end).toLowerCase();
		}
		if(keyWord==null) {
			return null;
		}
		keyWord = keyWord.toLowerCase();
		if (noiseWords.contains(keyWord)) {
			return null;
		}
		if (keyWord.chars().allMatch(Character::isWhitespace)) {
			return null;
		}
		// System.out.println(keyWord.length() + " " + keyWord.toLowerCase());

		// System.out.println(word.equals("3.0"));
		return keyWord;
	}

	/**
	 * Inserts the last occurrence in the parameter list in the correct position in
	 * the list, based on ordering occurrences on descending frequencies. The
	 * elements 0..n-2 in the list are already in the correct order. Insertion is
	 * done by first finding the correct spot using binary search, then inserting at
	 * that spot.
	 * 
	 * @param occs List of Occurrences
	 * @return Sequence of mid point indexes in the input list checked by the binary
	 *         search process, null if the size of the input list is 1. This
	 *         returned array list is only used to test your code - it is not used
	 *         elsewhere in the program.
	 */
	public ArrayList<Integer> insertLastOccurrence(ArrayList<Occurrence> occs) {
		
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		int leftIndex = 0;
		int rightIndex = occs.size() - 2;
		int midIndex = (leftIndex + rightIndex) / 2;
		Occurrence lastElement = occs.remove(occs.size() - 1);
		int target = lastElement.frequency;
		while (leftIndex <= rightIndex) {
			midIndex = (leftIndex + rightIndex) / 2;
			indexes.add(midIndex);
			if (target == occs.get(midIndex).frequency) {
				break;
			}
			if (target > occs.get(midIndex).frequency) {
				rightIndex = midIndex - 1;
			} else {
				leftIndex = midIndex + 1;
			}
		}	
		occs.add(leftIndex, lastElement);
		// System.out.println(indexes.toArray().toString());
		return indexes;
	}

	/**
	 * This method indexes all keywords found in all the input documents. When this
	 * method is done, the keywordsIndex hash table will be filled with all
	 * keywords, each of which is associated with an array list of Occurrence
	 * objects, arranged in decreasing frequencies of occurrence.
	 * 
	 * @param docsFile       Name of file that has a list of all the document file
	 *                       names, one name per line
	 * @param noiseWordsFile Name of file that has a list of noise words, one noise
	 *                       word per line
	 * @throws FileNotFoundException If there is a problem locating any of the input
	 *                               files on disk
	 */
	public void makeIndex(String docsFile, String noiseWordsFile) throws FileNotFoundException {
		// load noise words to hash table
		Scanner sc = new Scanner(new File(noiseWordsFile));
		while (sc.hasNext()) {
			String word = sc.next();
			noiseWords.add(word);
		}

		// index all keywords
		sc = new Scanner(new File(docsFile));
		while (sc.hasNext()) {
			String docFile = sc.next();
			HashMap<String, Occurrence> kws = loadKeywordsFromDocument(docFile);
			mergeKeywords(kws);
		}
		sc.close();
	}

	/**
	 * Search result for "kw1 or kw2". A document is in the result set if kw1 or kw2
	 * occurs in that document. Result set is arranged in descending order of
	 * document frequencies.
	 * 
	 * Note that a matching document will only appear once in the result.
	 * 
	 * Ties in frequency values are broken in favor of the first keyword. That is,
	 * if kw1 is in doc1 with frequency f1, and kw2 is in doc2 also with the same
	 * frequency f1, then doc1 will take precedence over doc2 in the result.
	 * 
	 * The result set is limited to 5 entries. If there are no matches at all,
	 * result is null.
	 * 
	 * See assignment description for examples
	 * 
	 * @param kw1 First keyword
	 * @param kw1 Second keyword
	 * @return List of documents in which either kw1 or kw2 occurs, arranged in
	 *         descending order of frequencies. The result size is limited to 5
	 *         documents. If there are no matches, returns null or empty array list.
	 */
	public ArrayList<String> top5search(String kw1, String kw2) {
		ArrayList<String> finalList = new ArrayList<>();
		String word1 = kw1.toLowerCase();
		String word2 = kw2.toLowerCase();
		if (!keywordsIndex.containsKey(word1) && !keywordsIndex.containsKey(word2)) {
			return null;
		} else if (keywordsIndex.containsKey(word1) && keywordsIndex.containsKey(word2)) {
			calculateTop5(finalList, word1, word2);
		} else if (keywordsIndex.containsKey(word1)) {
			if (keywordsIndex.get(word1).size() < 5) {
				for (int i = 0; i < keywordsIndex.get(word1).size(); i++) {
					finalList.add(keywordsIndex.get(word1).get(i).document);
				}
			} else {
				for (int i = 0; i < 5; i++) {
					finalList.add(keywordsIndex.get(word1).get(i).document);
				}
			}
		} else if (keywordsIndex.containsKey(word2)) {
			if (keywordsIndex.get(word2).size() < 5) {
				for (int i = 0; i < keywordsIndex.get(word2).size(); i++) {
					finalList.add(keywordsIndex.get(word2).get(i).document);
				}
			} else {
				for (int i = 0; i < 5; i++) {
					finalList.add(keywordsIndex.get(word2).get(i).document);
				}
			}
		}
		return finalList;
	}

	public void calculateTop5(ArrayList<String> list, String kw1, String kw2) {
		ArrayList<Occurrence> listOne = keywordsIndex.get(kw1);
		ArrayList<Occurrence> listTwo = keywordsIndex.get(kw2);
		int count = list.size();
		int i = 0;
		int j = 0;
		while ((i < listOne.size() || j < listTwo.size()) && count < 5)
		{
			if (i == listOne.size()) {
				if (!list.contains(listTwo.get(j).document)) {
					list.add(listTwo.get(j).document);
				}
				j++;
				count++;
			}
			else if (j == listTwo.size()) {
				if (!list.contains(listOne.get(i).document)) {
					list.add(listOne.get(i).document);
				}
				count++;
				i++;
			}
			else if (listOne.get(i).frequency >= listTwo.get(j).frequency && (!list.contains(listOne.get(i).document))) {
				list.add(listOne.get(i).document);
				i++;
				count++;
			} else if (listOne.get(i).frequency < listTwo.get(j).frequency && (!list.contains(listTwo.get(j).document))) {
				list.add(listTwo.get(j).document);
				j++;
				count++;
			} 
			else {
				if (list.contains(listOne.get(i).document)) {
					i++;
					} 	
				if (list.contains(listOne.get(j).document)) {
					j++;
					} 	
			}
		}
	 }
}
