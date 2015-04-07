package smu.sm.mining;

import java.io.File;
import java.io.FileInputStream;

import smu.sm.global.ClassifierType;
import smu.sm.global.Global;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LibSVM;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.Debug;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.SVMLightLoader;

public abstract class ClassifierL2 {
	protected ClassifierType type;
	protected Classifier classifier;
	private double weight;
	private int i, j;

	protected ClassifierL2(ClassifierType type) {
		this.setType(type);
	}

	protected ClassifierL2(int i, int j, ClassifierType type) {
		this.setType(type);
		this.i = i;
		this.j = j;
	}

	public void train() {
		// TODO Auto-generated method stub
		SVMLightLoader loader;
		Instances traindata = null;
		String linkData, linkModel;
		String modelID = "";
		try {
			for (int f = 0; f < Global.folds; f++) {
				linkData = Global.csvPath + "Folds/" + Global.maindata
						+ "/Fold" + (f + 1) + "/";
				linkModel = Global.csvPath + "model/" + Global.maindata
						+ "/Fold" + (f + 1) + "/";
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

	public abstract void test();

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

	public ClassifierType getType() {
		return type;
	}

	public void setType(ClassifierType type) {
		this.type = type;
	}

	public void classLoader(File store) {
		SerializedClassifier clf = new SerializedClassifier();
		clf.setModelFile(store);
		this.classifier = clf;
	}

	public double getWeight() {
		return weight;
	}

	public void setWeight(double weight) {
		this.weight = weight;
	}

	public Classifier getClassifier() {
		return classifier;
	}

	public void setClassifier(Classifier classifier) {
		this.classifier = classifier;
	}

}
