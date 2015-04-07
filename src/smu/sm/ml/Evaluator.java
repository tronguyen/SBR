package smu.sm.ml;

import smu.sm.entity.MyDocument;
import smu.sm.processing.DocumentCollector;
import edu.smu.utils.FileUtils;

public class Evaluator {
	
	
	public void evaluate(String testFile, String labeledDirectory, String predictFile){
		String[] documentIds = FileUtils.readLines(testFile);
		
		MyDocument[] docs = DocumentCollector.collectFromDirectory(labeledDirectory);
		String[] actualLabels = new String[documentIds.length];
		int i = 0;
		for(MyDocument doc: docs){
			if(i >= actualLabels.length) break;
			String currentDocId = doc.getDocumentId();
			for(int j = i; j < actualLabels.length; j++){
				if(currentDocId.equalsIgnoreCase(documentIds[j])){
					actualLabels[i++] = doc.getCategory();
					break;
				}
			}
		}
		String[] predictLabels = FileUtils.readLines(predictFile);
		double accuracy = evaluate(actualLabels, predictLabels);
		System.out.println("Accuracy on " + actualLabels.length + " instances: " + accuracy);
	}

	public double evaluate(String[] actualLabels, String[] predictLabels){
		if(actualLabels.length != predictLabels.length) return -1;
		int count = 0;
		
		for(int i = 0; i < actualLabels.length; i++)
			if(actualLabels[i].equalsIgnoreCase(predictLabels[i]))
				count++;
		
		
		return (double)count/actualLabels.length;
	}
	
}
