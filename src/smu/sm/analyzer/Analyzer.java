package smu.sm.analyzer;

import smu.sm.entity.Feature;
import smu.sm.entity.Token;

public interface Analyzer {
	public Feature[] analyze(Token[] tokens);
}
