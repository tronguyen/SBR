package smu.sm.entity;

import java.util.HashMap;
import java.util.Map;

import edu.smu.utils.ObjectUtils;

public class FeatureHolder {
	private Map<String, Integer> features = new HashMap<String, Integer>();
	private String targetFeature;
	
	
	public void addFeature(String feature){
		Integer val = features.get(feature);
		if(ObjectUtils.isNull(val)) val = 1;
		else val ++;
		features.put(feature, val);
	}
	
	public String[] getFeatures() { 
		return features.keySet().toArray(new String[features.size()]); 
	}	
	
	public int getFrequency(String feature){
		Integer val = features.get(feature);
		if(ObjectUtils.isNull(val)) val = 0;
		return val;
	}
	
	public String getTargetFeature() { return targetFeature; }
	public void setTargetFeature(String targetFeature) { this.targetFeature = targetFeature; }
}
