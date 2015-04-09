package smu.sm.analyzer;

import smu.sm.entity.FeatureHolder;
import smu.sm.entity.Token;

public class UnigramAnalyzer implements Analyzer {
	private static final String PREFIX = "uni:";
	@Override
	public void analyze(Token[] tokens, FeatureHolder featureHolder) {
		for(int i = 0; i < tokens.length; i++){
			String tk = tokens[i].getToken();
			for(int j = 0 ; j < tk.length(); j++)
				featureHolder.addFeature(PREFIX + tk.charAt(j));
		}
	} 
}
