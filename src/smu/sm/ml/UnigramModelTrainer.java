package smu.sm.ml;

import smu.sm.analyzer.TargetLabelAnalyzer;
import smu.sm.analyzer.UnigramAnalyzer;
import smu.sm.entity.FeatureDictionary;
import smu.sm.entity.FeatureGenerator;
import smu.sm.entity.FeatureVector;
import smu.sm.entity.MyDocument;
import smu.sm.entity.Token;
import smu.sm.processing.DocumentCollector;
import smu.sm.processing.TokenGenerator;
import smu.sm.util.MLUtils;


public class UnigramModelTrainer implements ModelTrainer {

	@Override
	public void train(String inputDir, String outputDir) {
		try{
			MyDocument[] docs = DocumentCollector.collectFromJson(inputDir);
			
			FeatureDictionary featureDict = new FeatureDictionary();
			FeatureGenerator featureGenerator = createFeatureGenerator();
			TokenGenerator tokenGenerator = new TokenGenerator();
			
			FeatureVector[] fVectors = new FeatureVector[docs.length];
			
			for(int i = 0 ; i < docs.length; i++){
				MyDocument doc = docs[i];
				Token[] tokens = tokenGenerator.generate(doc);
				fVectors[i] = featureGenerator.generate(tokens, featureDict, true);
			}
			
			for(FeatureVector fVector: fVectors){
				fVector.populate(featureDict, docs.length);
				String toPrintVector = fVector.toCompactString(featureDict);
				System.out.println(toPrintVector);
			}
			
			double cosine = MLUtils.cosine(fVectors[0], fVectors[1]);
			System.out.println(cosine);
		} catch (Exception ex){

		}
	}

	@Override
	public FeatureGenerator createFeatureGenerator() {
		FeatureGenerator fGenerator = new FeatureGenerator(
				new UnigramAnalyzer()
		);
		fGenerator.setTargetAnalyzer(new TargetLabelAnalyzer());
		return fGenerator;
	}
	
	

}
