package smu.sm.analyzer;

import smu.sm.entity.Document;
import smu.sm.entity.Feature;

public interface Analyzer {
	public Feature[] analyze(Document doc);
}
