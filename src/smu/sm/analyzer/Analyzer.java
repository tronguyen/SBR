package smu.sm.analyzer;

import smu.sm.entity.FeatureHolder;
import smu.sm.entity.Token;

public interface Analyzer {
	public void analyze(Token[] tokens, FeatureHolder featureHolder);
}
