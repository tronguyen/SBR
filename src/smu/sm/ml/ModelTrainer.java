package smu.sm.ml;

import smu.sm.entity.FeatureGenerator;

public interface ModelTrainer {

	public void train(String inputDir, String outputDir);
	public FeatureGenerator createFeatureGenerator();
}
