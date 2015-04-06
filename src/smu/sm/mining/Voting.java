package smu.sm.mining;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

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
	Map<String, Double[]> allWeights = new HashMap<String, Double[]>();
	List<Map<String, Instance>> mpLst = new ArrayList<Map<String, Instance>>();

	public Voting(File testFile, File outFile, String linkModel, String linkData)
			throws FileNotFoundException {
		this.testFile = testFile;
		this.outFile = outFile;
		File temp;
		String modelID;
		ClassifierL2 tempCls = null;
		double weight;

		Map<String, Instance> mp = null;
		for (ClassifierType type : ClassifierType.values()) {
			mp = WeightLearner.createFeatureMap(linkData + "Test" + type,
					linkData + "Test");
			mpLst.add(mp);
		}
		// Load all classifiers
		for (int i = 1; i <= 4; i++)
			for (int j = i + 1; j <= 4; j++) {
				List<ClassifierL2> subLst = new ArrayList<ClassifierL2>();
				Scanner sc = new Scanner(new File(linkData + i + "" + j + ".w"));
				for (ClassifierType type : ClassifierType.values()) {
					modelID = i + "" + j + type;
					temp = new File(linkModel + modelID + ".model");
					// Get weights
					weight = sc.nextDouble();
					switch (type) {
					// case UNIGRAM:
					// break;
					// case BIGRAM:
					// break;
					case LDA:
						tempCls = new LDAClassifier(i, j, type);
						break;
					}
					tempCls.setWeight(weight);
					tempCls.classLoader(temp);
					subLst.add(tempCls);

				}
				allClassifiers.put(i + "_" + j, subLst);
			}
	}

	public void vote() {
		try {
			Instance ins = null;
			int res, max;
			int[] c;
			BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
			BufferedReader br = new BufferedReader(new FileReader(testFile));
			String idnum = "";
			// predict instance to penalize weight
			while ((idnum = br.readLine()) != null) {
				c = new int[4];
				for (String key : allClassifiers.keySet()) {
					if(!key.equals("2_3")) continue;
					String[] classIdex = key.split("_");
					res = pairVote(allClassifiers.get(key), idnum);
					// Count voting
					if (res == 1) { // small class
						c[Integer.valueOf(classIdex[0]) - 1]++;
					} else if (res == -1) { // large class
						c[Integer.valueOf(classIdex[1]) - 1]++;
					}
				}
				// Find max variables
				max = 0;
				for (int j = 0; j < 4; j++) {
					if (c[max] < c[j])
						max = j;
				}
				bw.write((max + 1) + "\n"); 
			}
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private int pairVote(List<ClassifierL2> clsLst, String id) {
		// Val = +1 -> smaller index class; otherwise: -1 -> larger index class
		double val = 0;
		double weightSum = 0;
		Instance ins = null;
		ClassifierL2 cls = null;
		for (int c = 0; c < clsLst.size(); c++) {
			ins = mpLst.get(c).get(id);
			cls = clsLst.get(c);
			val += cls.getWeight() * ((cls.predict(ins) == 0) ? 1 : -1);
			weightSum += cls.getWeight();
		}
		val = val / weightSum;
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
