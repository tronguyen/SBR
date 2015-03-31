package smu.sm.util;

import edu.smu.utils.ObjectUtils;
import smu.sm.entity.FeatureVector;
import smu.sm.entity.MyFeature;

public class MLUtils {
	
	public static double computeTFIDF(int termFreq, int docFreq, int totalDoc){
		return Math.log(1 + termFreq)*Math.log(1 + (double)totalDoc/(1+docFreq));
	}
	
	public static double cosine(FeatureVector v1, FeatureVector v2){
		double den = v1.getValue()*v2.getValue();
		double num = 0.0;
		for(String feature: v1.getFeatures().keySet()){
			MyFeature f1 = v1.getFeature(feature);
			MyFeature f2 = v2.getFeature(feature);
			if(!ObjectUtils.isNull(f2)) {
				num += f1.getWeight()*f2.getWeight();
			}
		}
		return num/den;
	}

}
