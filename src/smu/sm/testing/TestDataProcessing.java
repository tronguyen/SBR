package smu.sm.testing;

import java.io.File;

import smu.sm.global.ClassifierType;
import smu.sm.global.Global;
import smu.sm.mining.ClassifierL2;
import smu.sm.mining.LDAClassifier;
import smu.sm.processing.DataProcessing;
import smu.sm.processing.MyHelper;

public class TestDataProcessing {
	// Tesing LDA
	public void testLDARefine() {
		String in = Global.csvPath + "/raw/microsoft_LDA_raw.csv";
		String out = Global.csvPath + "/raw/microsoft_LDA.csv";
		MyHelper.refineLDAData(new File(in), new File(out));
	}

	public void testCreateCrossData() {
		DataProcessing dp = new DataProcessing();
		dp.setPathData(Global.csvPath);
		for (int i = 1; i <= 4; i++)
			for (int j = i + 1; j <= 4; j++)
				dp.exrtactPairData(i, j, ClassifierType.LDA);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		TestDataProcessing test = new TestDataProcessing();
		test.testLDARefine();
		test.testCreateCrossData();
		ClassifierL2 slc = new LDAClassifier(ClassifierType.LDA);
		
	}

}
