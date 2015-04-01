package smu.sm.analyzer;

import smu.sm.entity.MyDocument;
import smu.sm.entity.MyFeature;

public interface MyAnalyzer {
	public MyFeature[] analyze(MyDocument doc);
}
