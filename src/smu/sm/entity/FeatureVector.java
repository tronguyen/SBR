package smu.sm.entity;

import java.util.HashMap;
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
		
		for(String key: features.keySet()){
			MyFeature feature = features.get(key);
			int featureIdx = dict.getFeatureIndex(key);
			buffer.append(" ").append(featureIdx).append(":").append(feature.getWeight());
		}
		
		return buffer.toString().trim();
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
