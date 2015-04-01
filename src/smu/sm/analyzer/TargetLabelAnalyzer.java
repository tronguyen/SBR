package smu.sm.analyzer;

import smu.sm.entity.FeatureHolder;
import smu.sm.entity.Token;

public class TargetLabelAnalyzer implements Analyzer {
	public static final String TARGET_LABEL_PREFIX = "target:";
	
	@Override
	public void analyze(Token[] tokens, FeatureHolder featureHolder) {
		for(Token token: tokens){
			String tkStr = token.getToken();
			if(tkStr.startsWith(TARGET_LABEL_PREFIX)){
				featureHolder.addFeature(getTargetLabel(tkStr));
			}
		}
	}
	
	private String getTargetLabel(String tk){
		int idx = tk.indexOf(":");
		return tk.substring(idx+1, tk.length());
	}

}
