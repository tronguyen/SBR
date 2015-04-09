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
	
	public void merge(String foldDir) throws IOException {
		String dataset = FileUtils.getBaseName(foldDir);
		
		Map<String, String> actualLabels = getActualLabel("data/raw2/" + dataset);
		
		String[] foldPaths = FileUtils.listName(foldDir, true);
		String[] models = new String[]{"UNI", "BIG", "LDA"};

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
				String[] lines = FileUtils.readLines(foldDir + "/" + dataset + "_" + model + ".csv");
				
				PrintStream trainOut = new PrintStream(foldPath + "/" + dataset + "_" + model + "_train.txt");
				PrintStream testFeatureOut = new PrintStream(foldPath + "/" + dataset + "_" + model + "_fVector_test.txt");
				PrintStream testLabelOut = new PrintStream(foldPath + "/" + dataset + "_" + model + "_label_test.txt");
				
				for(String line: lines){
					if(line.contains("NaN")) continue;
					
					int idx = line.indexOf(" ");
					String documentId = line.substring(0, idx);
					String fVector = line.substring(idx+1, line.length());
					
					String actualLabel = actualLabels.get(documentId);
					String printLabel = getPrintTargetFeature(actualLabel);
					
					if(trainSet.contains(documentId)) trainOut.println(printLabel + " " + fVector);
					if(testSet.contains(documentId)) {
						testFeatureOut.println(printLabel + " " + fVector);
						testLabelOut.println(printLabel);
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
		String foldDir = "D:/Program/sm/FinalProject/data/microsoft";
		
		
		DataMerger merger = new DataMerger();
		merger.merge(foldDir);
				
	}

}
