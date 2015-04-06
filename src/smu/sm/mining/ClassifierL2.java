package smu.sm.mining;

import java.io.File;

import smu.sm.global.ClassifierType;
import weka.classifiers.Classifier;
import weka.core.Instance;

public abstract class ClassifierL2 {
	private ClassifierType type;
	private Classifier classifier;
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

	public abstract void train();

	public abstract void test();

	public abstract double predict(Instance ins);

	public ClassifierType getType() {
		return type;
	}

	public void setType(ClassifierType type) {
		this.type = type;
	}

	public abstract void classLoader(File store);

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
