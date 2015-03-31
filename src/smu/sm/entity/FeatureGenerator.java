package smu.sm.entity;

import smu.sm.analyzer.Analyzer;

public class FeatureGenerator {
	private Analyzer[] analyzers;
	private Analyzer targetAnalyzer;
	
	public FeatureGenerator(Analyzer ... analyzers){
		this.analyzers = analyzers;
	}
	
	public void setTargetAnalyzer(Analyzer targetAnalyzer){
		this.targetAnalyzer = targetAnalyzer;
	}
	
	public FeatureVector generate(Token[] tokens, FeatureDictionary featureDictionary, boolean generateTarget){
		FeatureHolder featureHolder = new FeatureHolder();
		
		String targetLabel = ""; 
		if(generateTarget){
			targetAnalyzer.analyze(tokens, featureHolder);
			targetLabel = featureHolder.getFeatures()[0];
			featureHolder = new FeatureHolder();
		}
		
		for(Analyzer analyzer: analyzers){
			analyzer.analyze(tokens, featureHolder);
		}
		
		String[] featureStr = featureHolder.getFeatures();
		MyFeature[] features = new MyFeature[featureStr.length];
		
		
		for(int i = 0; i < featureStr.length; i++){
			String feature = featureStr[i];
			featureDictionary.incrDocFrequency(feature);
			features[i] = new MyFeature(feature);
			features[i].setFrequency(featureHolder.getFrequency(feature));
		}
		
		FeatureVector fVector = new FeatureVector();
		fVector.setTargetFeature(targetLabel);
		fVector.addFeatureArray(features);
		return fVector;
		
	}

}
