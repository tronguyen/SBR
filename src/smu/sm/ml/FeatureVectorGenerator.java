package smu.sm.ml;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import smu.sm.analyzer.BigramAnalyzer;
import smu.sm.analyzer.UnigramAnalyzer;
import smu.sm.entity.FeatureDictionary;
import smu.sm.entity.FeatureGenerator;
import smu.sm.entity.FeatureVector;
import smu.sm.entity.MyDocument;
import smu.sm.entity.Token;
import smu.sm.processing.DocumentCollector;
import smu.sm.processing.TokenGenerator;
import edu.smu.utils.StringUtils;

public class FeatureVectorGenerator {

	public static void generate(String inputDir, FeatureGenerator featureGenerator, String outputFile, boolean baseline, boolean fiveClass) throws Exception{
		MyDocument[] docs = DocumentCollector.collectFromDirectory(inputDir);

		System.out.println("Total docs: " + docs.length);

		FeatureDictionary featureDict = new FeatureDictionary();
		TokenGenerator tokenGenerator = new TokenGenerator();
		List<FeatureVector> fVectors = new ArrayList<FeatureVector>();

		for(int i = 0 ; i < docs.length; i++){
			MyDocument doc = docs[i];

			String targetFeature = getTrueTargetFeature(doc.getCategory());
			if(baseline){
				if(StringUtils.isNullOrEmpty(targetFeature) && !fiveClass) continue;
				if(StringUtils.isNullOrEmpty(targetFeature) && fiveClass) targetFeature = TARGET_LABELS.length + 1  + "";
			} else {
				if(StringUtils.isNullOrEmpty(targetFeature)) targetFeature = "-1";
				else targetFeature = "1";
			}
			
			Token[] tokens = tokenGenerator.generate(doc);
			FeatureVector fVector = featureGenerator.generate(tokens, featureDict, false);
			fVector.setTargetFeature(targetFeature);
			fVectors.add(fVector);
		}

		PrintStream out = new PrintStream(outputFile);
		for(FeatureVector fVector: fVectors){
			fVector.populate(featureDict, docs.length);
			String toPrintVector = fVector.toCompactString(featureDict);
			out.println(toPrintVector);
		}

		out.close();
	}

	private static String[] TARGET_LABELS = new String[]{"CWE-119", "CWE-20", "CWE-264", "CWE-399"};
	private static String getTrueTargetFeature(String category){
		for(int i = 0; i < TARGET_LABELS.length; i++){
			if(StringUtils.areEqual(TARGET_LABELS[i], category))
				return "" + (i+1);
		}
		return "";
	}

	private static FeatureGenerator createFeatureGenerator(String model){
		if("uni".equalsIgnoreCase(model)) 
			return new FeatureGenerator(new UnigramAnalyzer());
		else
			return new FeatureGenerator(new BigramAnalyzer());

	}

	public static void main(String[] args) throws Exception{
		String[] dataset = new String[]{"linux", "microsoft"};
		String[] models = new String[]{"uni", "big"};

		boolean baseline = true;
		boolean fiveClass = true;

		for(String model: models){
			FeatureGenerator fGenerator = createFeatureGenerator(model);
			for(String dat: dataset){
				String inputDir = "data/raw2/" + dat;
				String outputFile = "data/train/" + dat;
				if(baseline) outputFile +=  "_baseline";
				if(fiveClass) outputFile += "_5class";
				outputFile += "_" + model +".txt";

				FeatureVectorGenerator.generate(inputDir, fGenerator, outputFile, baseline, fiveClass);
			}
		}

	}

}
