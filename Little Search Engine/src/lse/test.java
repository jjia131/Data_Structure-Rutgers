package lse;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class test {

	
	public static ArrayList<Integer> insertLastOccurrence(ArrayList<Integer> occs) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		int leftIndex = 0;
		int rightIndex = occs.size() - 2;
		int midIndex = (leftIndex + rightIndex) / 2;
		int lastElement = occs.remove(occs.size() - 1);
		int target = lastElement;
		while (leftIndex <= rightIndex) {
			midIndex = (leftIndex + rightIndex) / 2;
			indexes.add(midIndex);
			if (target == occs.get(midIndex)) {
				break;
			}
			if (target > occs.get(midIndex)) {
				rightIndex = midIndex - 1;
			} else if (occs.get(midIndex) > target){
				leftIndex = midIndex + 1;
			}
		}
		occs.add(midIndex, lastElement);
		// System.out.println(indexes.toArray().toString());
		return indexes;
	}
	
	public static void main(String[] agrs) throws FileNotFoundException {
		System.out.println("ÎÒµÄ");
		LittleSearchEngine test = new LittleSearchEngine();
		test.makeIndex("docs.txt", "noisewords.txt");
		//System.out.println(test.getKeyword("paraphrase;"));
		/*HashMap<String, Occurrence> list = test.loadKeywordsFromDocument("pohlx.txt");
		HashMap<String, Occurrence> list2 = test.loadKeywordsFromDocument("pohly.txt");
		test.mergeKeywords(list);
		test.mergeKeywords(list2);
		/*System.out.println("Total:" + list.size());
		for (String name : list.keySet()) {
			String key = name.toString();
			String value = list.get(name).toString();
			System.out.println(key + " " + value);
		}*/

		System.out.println("Total:" + test.keywordsIndex.size());
		for (String name : test.keywordsIndex.keySet()) {
			String key = name.toString();
			String value = test.keywordsIndex.get(name).toString();
			System.out.println(key + " " + value);
		}
		System.out.println();
        try {
            System.out.println(test.top5search("red", "car"));

        } catch (Exception e) {
            System.out.println("ERROR");
        }
		//System.out.println(test.top5search("color", "strange"));
		//System.out.println(test.top5search("earnestly", "crowded").size());
		//ArrayList<Integer> list = new ArrayList<>(Arrays.asList(82,76,71,71,70,65,61,56,54,51,48,45,41,36,34,30,25,20,20,18,17,17,14,12,17));
		//System.out.println(insertLastOccurrence(list).toString());
		
	}
}
