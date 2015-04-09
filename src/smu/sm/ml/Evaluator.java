package smu.sm.ml;

import smu.sm.entity.MyDocument;
import smu.sm.processing.DocumentCollector;
import edu.smu.utils.FileUtils;
import edu.smu.utils.StringUtils;

public class Evaluator {

	public void evaluateAllFolds(String foldDir, String labeledDirectory){
		String[] foldPaths = FileUtils.listName(foldDir, true);
		double totalTest = 0.0;
		double overallAccuracy = 0.0;

		for(String foldPath: foldPaths){
			System.out.println("\n------------------------------------------------------------");
			System.out.println("Processing fold " + foldPath);
			double[] vals = evaluate(foldPath + "/Test", labeledDirectory, foldPath + "/Result");
			totalTest += vals[0];
			overallAccuracy += vals[1]*vals[0];
		}

		System.out.println("\nAverage accuracy: " + overallAccuracy/totalTest);
	}

	public double[] evaluate(String testFile, String labeledDirectory, String predictFile){
		String[] documentIds = FileUtils.readLines(testFile);

		MyDocument[] docs = DocumentCollector.collectFromDirectory(labeledDirectory);
		String[] actualLabels = new String[documentIds.length];
		int i = 0;
		for(MyDocument doc: docs){
			if(i >= actualLabels.length) break;
			String currentDocId = doc.getDocumentId();
			for(int j = i; j < actualLabels.length; j++){
				if(currentDocId.equalsIgnoreCase(documentIds[j])){
					actualLabels[i++] = getTrueTargetFeature(doc.getCategory());
					break;
				}
			}
		}
		String[] predictLabels = FileUtils.readLines(predictFile);
		double accuracy = evaluate(actualLabels, predictLabels);
		System.out.println("Accuracy on " + actualLabels.length + " instances: " + accuracy);
		return new double[]{actualLabels.length, accuracy};
	}

	private String[] TARGET_LABELS = new String[]{"CWE-119", "CWE-20", "CWE-264", "CWE-399"};
	private String getTrueTargetFeature(String category){
		for(int i = 0; i < TARGET_LABELS.length; i++){
			if(StringUtils.areEqual(TARGET_LABELS[i], category))
				return "" + (i+1);
		}
		return "";
	}

	public double evaluate(String[] actualLabels, String[] predictLabels){
		if(actualLabels.length != predictLabels.length) return -1;
		int count = 0;

		for(int i = 0; i < actualLabels.length; i++)
			if(actualLabels[i].equalsIgnoreCase(predictLabels[i]))
				count++;


		return (double)count/actualLabels.length;
	}



	public static void main(String[] args){
		String[] dataset = new String[]{"linux"};

		for(String dat: dataset){
			String foldDir = "D:/Program/sm/FinalProject/data/" + dat;
			String labeledDirectory = "data/raw2/" + dat; 
			
			Evaluator eval = new Evaluator();
			eval.evaluateAllFolds(foldDir, labeledDirectory);
			System.out.println("**************************************************************");
		}
	}

}
