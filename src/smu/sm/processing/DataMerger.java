package smu.sm.processing;

import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import smu.sm.entity.MyDocument;
import edu.smu.utils.FileUtils;
import edu.smu.utils.StringUtils;

public class DataMerger {
	private static final String[] TARGET_LABELS = new String[]{"CWE-119", "CWE-20", "CWE-264", "CWE-399"};
	
	public void merge(String foldDir, String rawVectorDir, boolean oneClass) throws IOException {
		String dataset = FileUtils.getBaseName(foldDir);
		
		Map<String, String> actualLabels = getActualLabel("data/raw2/" + dataset);
		
		String[] foldPaths = FileUtils.listName(foldDir, true);
		String[] models = new String[]{
				"UNI", 
				"BIG", 
				"LDA"
				};

		for(String foldPath: foldPaths){
			System.out.println("\n------------------------------------------------------------");
			System.out.println("Processing fold " + foldPath);
			
			Set<String> trainSet = new HashSet<String>();
			Set<String> testSet = new HashSet<String>();
			
			String[] filePaths = FileUtils.listName(foldPath);
			for(String filePath: filePaths){
				if(filePath.endsWith("_ID")){
					String[] ids = FileUtils.readLines(filePath);
					for(String id: ids) trainSet.add(id);
				} else if(filePath.endsWith("Test")){
					String[] ids = FileUtils.readLines(filePath);
					for(String id: ids) testSet.add(id);
				}
			}
			
			for(String model: models){
				String[] lines = FileUtils.readLines(rawVectorDir + "/" + dataset + "_" + model + ".csv");
				
				String commonPath = foldPath + "/" + dataset + "_" + model;
				if(oneClass) commonPath += "_1Class";
				
				PrintStream trainOut = new PrintStream(commonPath + "_train.txt");
				PrintStream testFeatureOut = new PrintStream(commonPath + "_fVector_test.txt");
				PrintStream testLabelOut = new PrintStream(commonPath + "_label_test.txt");
				
				int count = 0;
				for(String line: lines){
					if(line.contains("NaN")) continue;
					
					int idx = line.indexOf(" ");
					String documentId = line.substring(0, idx);
					String fVector = line.substring(idx+1, line.length());
					
					String actualLabel = actualLabels.get(documentId);
					String printLabel = getPrintTargetFeature(actualLabel);
					
					if(oneClass && !StringUtils.isNullOrEmpty(printLabel)) printLabel = "1";
					else if (oneClass && StringUtils.isNullOrEmpty(printLabel)) printLabel = "-1";
					
					if(trainSet.contains(documentId)) trainOut.println(printLabel + " " + fVector);
					if(testSet.contains(documentId)) {
						testFeatureOut.println(printLabel + " " + fVector);
						testLabelOut.println(printLabel);
					}
					
					if(printLabel == "-1" && count < testSet.size()){
						testFeatureOut.println(printLabel + " " + fVector);
						testLabelOut.println(printLabel);
						count++;
					}
				}
				
				trainOut.close();
				testFeatureOut.close();
				testLabelOut.close();
			}
		}
	}
	
	private Map<String, String> getActualLabel(String labeledDirectory){
		Map<String, String> actualLabels = new HashMap<String, String>();
		MyDocument[] docs = DocumentCollector.collectFromDirectory(labeledDirectory);
		for(MyDocument doc: docs) actualLabels.put(doc.getDocumentId(), doc.getCategory());
		
		return actualLabels;
	}
	
	private static String getPrintTargetFeature(String category){
		for(int i = 0; i < TARGET_LABELS.length; i++){
			if(StringUtils.areEqual(TARGET_LABELS[i], category))
				return "" + (i+1);
		}
		return "";
	}
	
	public static void main(String[] args) throws IOException{
		String foldDir = "data/Folds/microsoft";
		String rawVectorDir = "data/raw";
		boolean oneClass = false;
		
		DataMerger merger = new DataMerger();
		merger.merge(foldDir, rawVectorDir, oneClass);
				
	}

}
