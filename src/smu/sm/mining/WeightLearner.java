package smu.sm.mining;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.SVMLightLoader;

public class WeightLearner {

	public void learner(List<ClassifierL2> clsLst, File valid) {
		// Weight Voting algorithm
		Instance ins = null;
		int[] w = new int[clsLst.size()];
		// Initialize weight for each classifier
		for (int k = 0; k < clsLst.size(); k++) {
			w[k] = 1;
		}
		// Learn weights
		try {

			SVMLightLoader loader = new SVMLightLoader();
			loader.setSource(new FileInputStream(valid));
			Instances validIns = loader.getDataSet();

			// predict instance to penalize weight
			for (int i = 0; i < validIns.numInstances(); i++) {
				ins = validIns.instance(i);
				for (ClassifierL2 cls : clsLst) {
					cls.predict(ins);
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
