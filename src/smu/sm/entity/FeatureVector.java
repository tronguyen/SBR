package smu.sm.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smu.sm.util.MLUtils;

public class FeatureVector {
	
	private String targetFeature;
	private Map<String, MyFeature> features = new HashMap<String, MyFeature>();	
	
	public FeatureVector(){}
	
	public String getTargetFeature() { return targetFeature; }
	public void setTargetFeature(String targetFeature) { this.targetFeature = targetFeature; }

	public Map<String, MyFeature> getFeatures() { return features; }
	public MyFeature getFeature(String feature){
		return features.get(feature);
	}
	
	public void setFeatures(Map<String, MyFeature> features) { this.features = features; }
	public void addFeature(MyFeature feature){
		features.put(feature.getFeature(), feature);
	}
	
	public void addFeatureArray(MyFeature[] features){
		for(MyFeature feature: features)
			addFeature(feature);
	}

	public void populate(FeatureDictionary dict, int totalDoc){
		for(String key: features.keySet()){
			MyFeature feature = features.get(key);
			int termFreq = feature.getFrequency();
			int docFreq = dict.getDocFrequency(key);
			double tfidf = MLUtils.computeTFIDF(termFreq, docFreq, totalDoc);
			
			feature.setWeight(tfidf);
			features.put(key, feature);
		}
	}
	
	public String toCompactString(FeatureDictionary dict){
		StringBuffer buffer = new StringBuffer();
		
		buffer.append(targetFeature);
		
		String[] sortKeys = sortKeyByIndex(dict);
		
		for(String key: sortKeys){
			MyFeature feature = features.get(key);
			int featureIdx = dict.getFeatureIndex(key);
			buffer.append(" ").append(featureIdx).append(":").append(feature.getWeight());
		}
		
		int maxIdx = dict.getFeatureIndexs().size() + 2;
		buffer.append(" ").append(maxIdx).append(":").append(0);
		
		return buffer.toString().trim();
	}
	
	public String[] sortKeyByIndex(FeatureDictionary dict){
		Map<String, String> tmpMap = new HashMap<String, String>();
		List<Integer> indexs = new ArrayList<Integer>();

		for(String key: features.keySet()){
			int index = dict.getFeatureIndex(key);
			tmpMap.put("Index-" + index, key);
			indexs.add(index);
		}

		String[] returnTerms = new String[indexs.size()];
		Collections.sort(indexs);
		for(int i = 0; i < indexs.size(); i++){
			returnTerms[i] = tmpMap.get("Index-" + indexs.get(i));
		}
		return returnTerms;
	}
	
	public double getValue(){
		double value = 0.0;
		
		for(String key: features.keySet()){
			MyFeature feature = features.get(key);
			value += feature.getWeight()*feature.getWeight();
		}
		return Math.sqrt(value);
	}
}
