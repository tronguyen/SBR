package smu.sm.mining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smu.sm.global.ClassifierType;
import smu.sm.global.Global;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.SVMLightLoader;

public class Voting {
	int TP, TN, FP, FN;
	double ACC, P, R;
	File testFile, outFile;
	Map<String, List<ClassifierL2>> allClassifiers = new HashMap<String, List<ClassifierL2>>();

	public Voting(File testFile, File outFile) {
		this.testFile = testFile;
		this.outFile = outFile;
		File temp;
		String modelID;
		ClassifierL2 tempCls = null;
		// Load all classifiers
		for (int i = 1; i <= 4; i++)
			for (int j = i + 1; j <= 4; j++) {
				List<ClassifierL2> pairLst = new ArrayList<ClassifierL2>();
				for (ClassifierType type : ClassifierType.values()) {
					modelID = i + "" + j + "_" + type;
					temp = new File(Global.csvPath + "model/" + modelID);
					switch (type) {
					// case UNIGRAM:
					// break;
					// case BIGRAM:
					// break;
					case LDA:
						tempCls = new LDAClassifier(i, j, type);
						break;
					}
					tempCls.classLoader(temp);
					pairLst.add(tempCls);
				}
				allClassifiers.put(i + "_" + j, pairLst);
			}
	}

	public void vote() {
		try {
			SVMLightLoader loader = new SVMLightLoader();
			loader.setSource(new FileInputStream(testFile));
			Instances validIns = loader.getDataSet();
			Instance ins = null;
			int res, max;
			int[] c = new int[4];
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			// predict instance to penalize weight
			for (int i = 0; i < validIns.numInstances(); i++) {
				ins = validIns.instance(i);
				for (String key : allClassifiers.keySet()) {
					String[] classIdex = key.split("_");
					res = pairVote(allClassifiers.get(key), ins);
					// Count voting
					if (res == 1) { // small class
						c[Integer.valueOf(classIdex[0])]++;
					} else if (res == -1) { // large class
						c[Integer.valueOf(classIdex[1])]++;
					}

					// Find max variables
					max = 0;
					for (int j = 0; j < 4; j++) {
						if (c[max] < c[j])
							max = j;
					}
					bw.write((max + 1) + "\n");
				}
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int pairVote(List<ClassifierL2> clsLst, Instance ins) {
		// Val = +1 -> smaller index class; otherwise: -1 -> larger index class
		int val = 0;
		for (ClassifierL2 cls : clsLst) {
			val += cls.getWeight() * ((cls.predict(ins) == 0) ? 1 : -1);
		}
		return (val > Global.thresholdDecision) ? 1 : -1;
	}

	public void output() {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			bw.write("TP: " + TP + "\n");
			bw.write("FP: " + FP + "\n");
			bw.write("TN: " + TN + "\n");
			bw.write("FN: " + FN + "\n");
			bw.write("ACC: " + ACC);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
