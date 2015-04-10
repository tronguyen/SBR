package smu.sm.ml;

import smu.sm.entity.MyDocument;
import smu.sm.global.Global;
import smu.sm.processing.DocumentCollector;
import edu.smu.statistic.StatisticMap;
import edu.smu.utils.FileUtils;
import edu.smu.utils.NumberUtils;
import edu.smu.utils.StringUtils;

public class Evaluator {
	private StatisticMap stat = new StatisticMap();
	
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

		for(int i = 0; i < actualLabels.length; i++){
			stat.incr("ClassStat", "Actual-" + actualLabels[i], 1);
			stat.incr("ClassStat", "Predict-" + predictLabels[i], 1);
			if(actualLabels[i].equalsIgnoreCase(predictLabels[i])){
				stat.incr("ClassStat", "Correct-" + actualLabels[i], 1);
				count++;
			}
		}


		return (double)count/actualLabels.length;
	}

	public void singleClassReport(){
		System.out.println("\nPrecision, recall, F1 by class");
		
		for(int i = 1; i <= TARGET_LABELS.length; i++){
			int actualCount = NumberUtils.parseInt(stat.getValue("ClassStat", "Actual-" + i));
			int predictCount = NumberUtils.parseInt(stat.getValue("ClassStat", "Predict-" + i));
			int correctCount = NumberUtils.parseInt(stat.getValue("ClassStat", "Correct-" + i));
			
			double p = (double) correctCount / predictCount;
			double r = (double) correctCount / actualCount;
			double f = 2*p*r/(p+r);
			
			System.out.println(TARGET_LABELS[i-1] 
				+ "\nPrecision = " + p
				+ "\nRecall = " + r
				+ "\nF1 = " + f);
			System.out.println();
		}
	}


	public static void main(String[] args){
		String[] dataset = new String[]{"linux", "microsoft"};

		for(String dat: dataset){
			String foldDir = Global.csvPath +"Folds/" + dat;
			String labeledDirectory = Global.csvPath + "raw2/" + dat; 
			
			Evaluator eval = new Evaluator();
			eval.evaluateAllFolds(foldDir, labeledDirectory);
			System.out.println("**************************************************************");
			
			eval.singleClassReport();
		}
		
		
	}

}
