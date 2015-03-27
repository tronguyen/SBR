package smu.sm.util;

import java.io.IOException;
import java.io.PrintStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class StatisticMap {

	private Map<String, Map<String, Integer>> maps = new HashMap<String, Map<String, Integer>>();

	public void incr(String cat, String subCat, int freq){
		Map<String, Integer> subMap = maps.get(cat);

		if(ObjectUtils.isNull(subMap)){
			subMap = new HashMap<String, Integer>();
			subMap.put(subCat, freq);
		} else {
			doIncr(subMap, subCat, freq);
		}
		doIncr(subMap, "all", freq);
		maps.put(cat, subMap);
	}

	public void report(boolean withPercentage){
		report(System.out, withPercentage, true);
	}

	public void report(String filePath, boolean withPercentage) throws IOException{
		report(new PrintStream(filePath), withPercentage, true);
	}
	
	public void report(PrintStream out, boolean withPercentage){
		report(out, withPercentage, true);
	}

	public void report(PrintStream out, boolean withPercentage, boolean sortSubset){
		for(String key: maps.keySet()){
			out.println("----------------------------------------------------------------------------------------");
			out.println("Category: " + key);
			out.println("----------------------------------------------------------------------------------------");

			Map<String, Integer> subMap = maps.get(key);

			if(sortSubset) subMap = sortByComparator(subMap, true);
			int allFreq = subMap.get("all");

			for(String subKey: subMap.keySet()){
				if(subKey.equals("all")) out.println("all" + StringUtils.indent("all", 60) + allFreq);
				else {
					int subFreq = subMap.get(subKey);
					double percentage = NumberUtils.roundDouble((double)subFreq*100/allFreq, 2);
					out.print(subKey + StringUtils.indent(subKey, 60) + subFreq);
					if(withPercentage) out.print(" ( " + percentage + "% )");
					out.println();
				}
			}
		}
	}

	public Set<String> getAllCategories(){
		return maps.keySet();
	}

	public Map<String, Integer> getSubmap(String cat){
		Map<String, Integer> subMap = maps.get(cat);
		if(ObjectUtils.isNull(subMap)) return null;
		return subMap;
	}

	public int getValue(String cat, String subCat){
		Map<String, Integer> subMap = maps.get(cat);
		if(ObjectUtils.isNull(subMap)) return 0;
		Integer subFreq = subMap.get(subCat);
		return (!ObjectUtils.isNull(subFreq)) ? subFreq : 0;
	}

	public void compactByFrequency(String cat, int freqThreshold){
		Map<String, Integer> subMap = maps.get(cat);
		if(ObjectUtils.isNull(subMap)) return;

		Set<String> subKeys = subMap.keySet();
		Set<String> removingSubKeys = new HashSet<String>();

		int smallValue = 0;

		for(String subKey: subKeys){
			int subFreq = subMap.get(subKey);
			if(subFreq < freqThreshold) {
				smallValue += subFreq;
				removingSubKeys.add(subKey);
			}
		}

		subMap.put("lowerGroup", smallValue);

		for(String subKey: removingSubKeys)
			remove(cat, subKey);
	}


	public void remove(String cat, String subKey){
		Map<String, Integer> subMap = maps.get(cat);
		if(ObjectUtils.isNull(subMap)) return;
		subMap.remove(subKey);
	}

	public void clearAll(){
		maps.clear();
	}

	private void doIncr(Map<String, Integer> subMap, String subCat, int freq){
		Integer curFreq = subMap.get(subCat);
		if(ObjectUtils.isNull(curFreq)) subMap.put(subCat, freq);
		else subMap.put(subCat, curFreq + freq);
	}

	private static Map<String, Integer> sortByComparator(Map<String, Integer> unsortMap, boolean desc) {

		// Convert Map to List
		List<Map.Entry<String, Integer>> list = 
				new LinkedList<Map.Entry<String, Integer>>(unsortMap.entrySet());

		// Sort list with comparator, to compare the Map values
		Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1,
					Map.Entry<String, Integer> o2) {
				if(!desc)
					return (o1.getValue()).compareTo(o2.getValue());
				else {
					if(o1.getValue() > o2.getValue()) return -1;
					else if(o1.getValue() < o2.getValue()) return 1;
					return 0;
				}
			}
		});

		// Convert sorted map back to a Map
		Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
		for (Iterator<Map.Entry<String, Integer>> it = list.iterator(); it.hasNext();) {
			Map.Entry<String, Integer> entry = it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
