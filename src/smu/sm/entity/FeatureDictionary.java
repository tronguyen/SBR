package smu.sm.entity;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import edu.smu.utils.ObjectUtils;

public class FeatureDictionary {
	private Map<String, Integer> docFrequency = new HashMap<String, Integer>();
	private Map<String, Integer> featureIndexs = new TreeMap<String, Integer>();

	public Map<String, Integer> getDocFrequency() { return docFrequency; }
	public void setDocFrequency(Map<String, Integer> docFrequency) { this.docFrequency = docFrequency; }
	public void incrDocFrequency(String feature){
		Integer val = docFrequency.get(feature);
		if(ObjectUtils.isNull(val)) val = 1;
		else val++;
		docFrequency.put(feature, val);

		Integer idx = featureIndexs.get(feature);
		if(ObjectUtils.isNull(idx)){
			idx = featureIndexs.size() + 1;
			featureIndexs.put(feature, idx);
		}
	}
	
	public int getDocFrequency(String feature){
		Integer val = docFrequency.get(feature);
		if(ObjectUtils.isNull(val)) return 0;
		return val;
	}

	public Map<String, Integer> getFeatureIndexs() { return featureIndexs; }
	public void setFeatureIndexs(Map<String, Integer> featureIndexs) { this.featureIndexs = featureIndexs; }
	public int getFeatureIndex(String feature){
		Integer idx = featureIndexs.get(feature);
		if(ObjectUtils.isNull(idx)) return -1;
		return idx;
	}
}
