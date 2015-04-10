package smu.sm.testing;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import smu.sm.global.ClassifierType;
import smu.sm.global.Global;
import smu.sm.mining.BIGClassifier;
import smu.sm.mining.ClassifierL2;
import smu.sm.mining.LDAClassifier;
import smu.sm.mining.UNIClassifier;
import smu.sm.mining.Voting;
import smu.sm.mining.WeightLearner;
import smu.sm.processing.DataProcessing;
import smu.sm.processing.MyHelper;

public class TestDataProcessing {
	// Convert LDA feature to weka format
	public void testLDARefine() {
		String in = Global.csvPath + "/raw/" + Global.maindata
				+ "-LDA-dist.csv";
		String out = Global.csvPath + "/raw/" + Global.maindata + "_LDA.csv";
		MyHelper.refineLDAData(new File(in), new File(out));
	}

	public void testCreateCrossData() {
		DataProcessing dp = new DataProcessing();
		dp.setPathData(Global.csvPath);
		try {
			dp.factorizeData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testVoting() throws FileNotFoundException {
		String linkData, linkModel;
		Voting vote = null;
		File testFile, outFile;
		for (int f = 0; f < Global.folds; f++) {
			linkData = Global.csvPath + "Folds/" + Global.maindata + "/Fold"
					+ (f + 1) + "/";
			linkModel = Global.csvPath + "model/" + Global.maindata + "/Fold"
					+ (f + 1) + "/";
			// Get bug ID for testing
			testFile = new File(linkData + "Test");
			outFile = new File(linkData + "Result");
			vote = new Voting(testFile, outFile, linkModel, linkData);
			vote.vote();
		}
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		// TODO Auto-generated method stub
		TestDataProcessing test = new TestDataProcessing();
		
//		test.testLDARefine();
//		test.testCreateCrossData();
//		ClassifierL2 cls = null;
//		cls = new LDAClassifier(ClassifierType.LDA);
//		cls.train();
//		cls = new UNIClassifier(ClassifierType.UNI);
//		cls.train();
//		cls = new BIGClassifier(ClassifierType.BIG);
//		cls.train();

		WeightLearner wl = new WeightLearner();
		wl.factorizeWeight();
		test.testVoting();
	}

}
//