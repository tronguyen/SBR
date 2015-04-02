package smu.sm.ml;

import java.io.PrintStream;

import smu.sm.analyzer.BigramAnalyzer;
import smu.sm.analyzer.TargetLabelAnalyzer;
import smu.sm.entity.FeatureDictionary;
import smu.sm.entity.FeatureGenerator;
import smu.sm.entity.FeatureVector;
import smu.sm.entity.MyDocument;
import smu.sm.entity.Token;
import smu.sm.ml.svm.svm_train;
import smu.sm.processing.DocumentCollector;
import smu.sm.processing.TokenGenerator;

public class BigramModelTrainer implements ModelTrainer {

	
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
			
			String trainingFile = outputDir + "\\train.dat";
			PrintStream out = new PrintStream(trainingFile);
			for(FeatureVector fVector: fVectors){
				fVector.populate(featureDict, docs.length);
				String toPrintVector = fVector.toCompactString(featureDict);
				out.println(toPrintVector);
			}
			
			out.close();
			
			String modelRes = outputDir + "\\bigram.svm.model";
			svm_train trainer = new svm_train();
			trainer.train(trainingFile, modelRes);
			
		} catch (Exception ex){

		}
	}

	@Override
	public FeatureGenerator createFeatureGenerator() {
		FeatureGenerator fGenerator = new FeatureGenerator(
				new BigramAnalyzer()
		);
		fGenerator.setTargetAnalyzer(new TargetLabelAnalyzer());
		return fGenerator;
	}
	
	

}
