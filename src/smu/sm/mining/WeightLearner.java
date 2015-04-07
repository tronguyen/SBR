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
import java.util.Random;

import smu.sm.global.ClassifierType;
import smu.sm.global.Global;
import smu.sm.processing.DataProcessing;
import weka.classifiers.misc.SerializedClassifier;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.SVMLightLoader;

public class WeightLearner {

	public void learner(List<ClassifierL2> clsLst, String ID) {
		// Weight Voting algorithm
		Instance ins = null;
		double[] w = new double[clsLst.size()];
		int q0, q1, res;
		// DataProcessing dp = new DataProcessing();
		List<Map<String, Instance>> mpLst = new ArrayList<Map<String, Instance>>();
		Map<String, Instance> mp = null;
		for (ClassifierType type : ClassifierType.values()) {
			mp = createFeatureMap(ID + type, ID + "_ID");
			mpLst.add(mp);
		}
		// Pick any class same ID list
		File valid = new File(ID + "_ID");
		// Initialize weight for each classifier
		for (int k = 0; k < clsLst.size(); k++) {
			w[k] = 1;
		}
		// predict instance to penalize weight
		BufferedReader br;
		try {
			// Read ID list
			br = new BufferedReader(new FileReader(valid));
			String idnum = "";
			while ((idnum = br.readLine()) != null) {
				q0 = q1 = 0;
				for (int c = 0; c < clsLst.size(); c++) {
					ClassifierL2 cls = clsLst.get(c);
					ins = mpLst.get(c).get(idnum);
					if (cls.predict(ins) == 0)
						q0 += w[c];
					else if (cls.predict(ins) == 1)
						q1 += w[c];
					res = (q0 > q1) ? 0 : ((q0 == q1) ? ((new Random()
							.nextDouble() > 0.5) ? 1 : 0) : 1);
					if (res != (int) ins.classValue()) {
						w[c] = w[c] * 0.95;
					}
				}
			}
			// Write down weights
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(ID
					+ "_w")));
			for (int k = 0; k < clsLst.size(); k++) {
				bw.write(w[k] + "\n");
			}
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static Map<String, Instance> createFeatureMap(String featureLink,
			String idLink) {
		Map<String, Instance> mp = new HashMap<String, Instance>();
		File featureFile = new File(featureLink);
		File idFile = new File(idLink);
		List<String> idList = new ArrayList<String>();
		BufferedReader br;
		try {
			// Read ID list
			br = new BufferedReader(new FileReader(idFile));
			String temp = "";
			while ((temp = br.readLine()) != null) {
				idList.add(temp.trim());
			}
			// Read Instance Set
			SVMLightLoader loader = new SVMLightLoader();
			loader.setSource(new FileInputStream(featureFile));
			Instances validIns = loader.getDataSet();
			// predict instance to penalize weight
			for (int i = 0; i < validIns.numInstances(); i++) {
				mp.put(idList.get(i), validIns.instance(i));
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return mp;
	}

	public Instance convert2Instance(String feature) {
		Instance ins = null;
		FastVector fv = new FastVector();
		String[] ft = feature.split(" ");
		for (String p : ft) {
			String[] pair = p.split(":");

		}
		return ins;
	}

	public void factorizeWeight() {
		String linkData, linkModel;
		SerializedClassifier classifier = null;
		String modelID, pairID;
		ClassifierL2 cls = null;
		List<ClassifierL2> clsLst;
		for (int f = 0; f < Global.folds; f++) {
			linkData = Global.csvPath + "Folds/" + Global.maindata + "/Fold"
					+ (f + 1) + "/";
			linkModel = Global.csvPath + "model/" + Global.maindata + "/Fold"
					+ (f + 1) + "/";
			for (int i = 1; i <= 4; i++)
				for (int j = i + 1; j <= 4; j++) {
					clsLst = new ArrayList<ClassifierL2>();
					pairID = i + "" + j;
					for (ClassifierType type : ClassifierType.values()) {
						// Load all model for each pair data
						modelID = pairID + type + ".model";
						classifier = new SerializedClassifier();
						classifier.setModelFile(new File(linkModel + modelID));
						switch (type) {
						case UNI:
							cls = new UNIClassifier(type);
							break;
						case BIG:
							cls = new BIGClassifier(type);
							break;
						case LDA:
							cls = new LDAClassifier(type);
							break;
						}
						cls.setClassifier(classifier);
						clsLst.add(cls);
					}
					// learn weights for a bundle of classifiers
					learner(clsLst, linkData + pairID);
				}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
