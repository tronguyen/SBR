package smu.sm.mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import smu.sm.entity.MyDocument;
import smu.sm.global.ClassifierType;
import smu.sm.global.Global;
import smu.sm.processing.MyHelper;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LibSVM;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.SVMLightLoader;

public class LDAClassifier extends ClassifierL2 {
	public LDAClassifier(ClassifierType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	public LDAClassifier(int i, int j, ClassifierType type) {
		super(i, j, type);
	}

	static String path = Global.csvPath + "script";

	// Enrich bug reports
	public void enrichContext(MyDocument doc) {

	}

	public void extractTopic() throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		String cmd = "java -jar libs/tmt-0.4.0.jar " + path
				+ "/lda-learn.scala";
		Process proc = runtime.exec(cmd);
		InputStream stdin = proc.getErrorStream();
		InputStreamReader isr = new InputStreamReader(stdin);
		BufferedReader br = new BufferedReader(isr);

		String line = null;
		System.out.println("<OUTPUT>");

		while ((line = br.readLine()) != null)
			System.out.println(line);

		System.out.println("</OUTPUT>");
		int exitVal = proc.waitFor();
		System.out.println("Process exitValue: " + exitVal);
		System.out.println("Done!");
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void train() {
		// TODO Auto-generated method stub
		SVMLightLoader loader;
		Instances traindata = null;
		String linkData, linkModel;
		String modelID = "";
		try {
			for (int f = 0; f < Global.folds; f++) {
				linkData = Global.csvPath + "Folds/Fold" + (f + 1) + "/";
				linkModel = Global.csvPath + "model/Fold" + (f + 1) + "/";
				new File(linkModel).mkdir();
				for (int i = 1; i <= 4; i++)
					for (int j = i + 1; j <= 4; j++) {
						// Get pair data
						modelID = i + "" + j + this.getType();
						loader = new SVMLightLoader();
						loader.setSource(new FileInputStream(new File(linkData
								+ modelID)));
						traindata = loader.getDataSet();
						// Training with svm
						setClassifier(new LibSVM());
						getClassifier().buildClassifier(traindata);
						// Write down learned model
						Debug.saveToFile(linkModel + modelID + ".model",
								getClassifier());
					}
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public double predict(Instance ins) {
		// TODO Auto-generated method stub
		double val = 0;
		try {
			val = this.getClassifier().classifyInstance(ins);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception in prediction!!!");
			e.printStackTrace();
		}
		return val;
	}

}
