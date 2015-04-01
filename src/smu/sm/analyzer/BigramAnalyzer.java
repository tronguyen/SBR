package smu.sm.analyzer;

import smu.sm.entity.FeatureHolder;
import smu.sm.entity.Token;

public class BigramAnalyzer implements Analyzer {
	private static final String PREFIX = "bigr:";

	@Override
	public void analyze(Token[] tokens, FeatureHolder featureHolder) {
		for(int i = 0; i < tokens.length; i++)
			featureHolder.addFeature(PREFIX + tokens[i].getToken());

	}

}
