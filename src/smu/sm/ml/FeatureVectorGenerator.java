package smu.sm.ml;

import java.io.PrintStream;

import smu.sm.analyzer.UnigramAnalyzer;
import smu.sm.entity.FeatureDictionary;
import smu.sm.entity.FeatureGenerator;
import smu.sm.entity.FeatureVector;
import smu.sm.entity.MyDocument;
import smu.sm.entity.Token;
import smu.sm.processing.DocumentCollector;
import smu.sm.processing.TokenGenerator;

public class FeatureVectorGenerator {
	
	public static void generate(String inputDir, FeatureGenerator featureGenerator, String outputFile) throws Exception{
		MyDocument[] docs = DocumentCollector.collectFromDirectory(inputDir);
		
		System.out.println("Total docs: " + docs.length);

		FeatureDictionary featureDict = new FeatureDictionary();
		TokenGenerator tokenGenerator = new TokenGenerator();
		FeatureVector[] fVectors = new FeatureVector[docs.length];
		
		for(int i = 0 ; i < docs.length; i++){
			MyDocument doc = docs[i];
			Token[] tokens = tokenGenerator.generate(doc);
			fVectors[i] = featureGenerator.generate(tokens, featureDict, false);
			fVectors[i].setTargetFeature(doc.getDocumentId());
		}
		
		PrintStream out = new PrintStream(outputFile);
		for(FeatureVector fVector: fVectors){
			fVector.populate(featureDict, docs.length);
			String toPrintVector = fVector.toCompactString(featureDict);
			out.println(toPrintVector);
		}
		
		out.close();
	}
	
	public static void main(String[] args) throws Exception{
		String inputDir = "data/raw2/linux";
		String outputFile = "data/raw2/linux_uni.txt";
		
		FeatureGenerator fGenerator = new FeatureGenerator(
				new UnigramAnalyzer()
		);
		
		FeatureVectorGenerator.generate(inputDir, fGenerator, outputFile);
		
		
	}

}
