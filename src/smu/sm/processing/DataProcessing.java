package smu.sm.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import smu.sm.global.ClassifierType;
import smu.sm.global.Global;

public class DataProcessing {

	private String pathData = "";

	private Random rd = null;

	public String getPathData() {
		return pathData;
	}

	public void setPathData(String pathData) {
		this.pathData = pathData;
	}

	public Map<String, String> getClassData(ClassifierType type)
			throws IOException {
		Map<String, String> mp = new HashMap<String, String>();
		BufferedReader br = null;
		String linkFile = "", temp = "";
		linkFile = pathData + "raw/" + Global.maindata + "_" + type + ".csv";
		br = new BufferedReader(new FileReader(new File(linkFile)));
		while ((temp = br.readLine()) != null) {
			String[] ins = temp.split(" ", 2);
			mp.put(ins[0].trim(), ins[1].trim());
		}
		return mp;
	}

	// Get a pair of class of data
	// Class1 < Class2
	public void exrtactPairData(Set<String> idLst1, Set<String> idLst2,
			ClassifierType type, String fileID) {
		// File contains ID of each class
		BufferedWriter bwTrain = null;
		Map<String, String> classData = null;
		String label = "+1";
		String instance = "";
		Set<String> trainSet = new HashSet<String>();
		Set<String> testSet = new HashSet<String>();

		try {
			// Write ID corresponding to feature
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(
					fileID + "_ID")));
			// Get data (all classes) with feature for ClassifierType
			classData = this.getClassData(type);
			bwTrain = new BufferedWriter(
					new FileWriter(new File(fileID + type)));

			label = "+1";
			// Pick negative instances
			for (String id : idLst1) {
				instance = label + " " + classData.get(id).trim();
				bwTrain.write(instance + "\n");
				bw.write(id + "\n");
			}
			// Pick positive instances
			label = "-1";
			for (String id : idLst2) {
				instance = label + " " + classData.get(id).trim();
				bwTrain.write(instance + "\n");
				bw.write(id + "\n");
			}
			bwTrain.flush();
			bwTrain.close();
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Get 90% ID from each class
	public Set<String> getIDSet(int cid, BufferedWriter bw) {
		Set<String> idLst = new HashSet<String>();
		Random localRD = new Random();
		String temp = "", id;
		try {
			BufferedReader br = new BufferedReader(new FileReader(new File(
					Global.csvPath + "bin/" + Global.maindata + "/" + cid
							+ ".csv")));
			while ((temp = br.readLine()) != null) {
				id = temp.split(",")[0];
				if (localRD.nextDouble() < Global.crossvalid) {
					idLst.add(id);
				} else {
					bw.write(id + "\n");
					bw.flush();
				}
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return idLst;
	}

	public List<Set<String>> createTrainDataFold(String linkFold) {
		BufferedWriter bw;
		Set<String> trainClassSet = null;
		List<Set<String>> trainSet = new ArrayList<Set<String>>();
		int label;
		try {
			bw = new BufferedWriter(
					new FileWriter(new File(linkFold + "/Test")));
			for (int t = 0; t < 4; t++) {
				trainClassSet = getIDSet(t + 1, bw);
				trainSet.add(trainClassSet);
			}
			bw.flush();
			bw.close();
			// Create Test File Feature

			Map<String, String> classData = null;
			for (ClassifierType type : ClassifierType.values()) {
				classData = this.getClassData(type);
				Scanner sc = new Scanner(new File(linkFold + "/Test"));
				bw = new BufferedWriter(new FileWriter(new File(linkFold
						+ "/Test" + type)));
				while (sc.hasNext()) {
					label = (new Random()).nextDouble() > 0.5 ? +1 : -1;
					bw.write(label + " " + classData.get(sc.nextLine().trim())
							+ "\n");
				}
				bw.flush();
				bw.close();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return trainSet;
	}

	public void factorizeData() throws IOException {

		// 10-folds data
		BufferedWriter bw;
		String linkFold;
		List<Set<String>> trainSet = null;

		for (int i = 0; i < Global.folds; i++) {
			linkFold = Global.csvPath + "Folds/" + Global.maindata + "/Fold"
					+ (i + 1);
			new File(linkFold).mkdir();
			// Create data
			trainSet = createTrainDataFold(linkFold);
			for (int s = 0; s < 4; s++)
				for (int l = s + 1; l < 4; l++) {
					// Extract each pair of data
					String fileID = linkFold + "/" + (s + 1) + "" + (l + 1);
					for (ClassifierType type : ClassifierType.values()) {
						exrtactPairData(trainSet.get(s), trainSet.get(l), type,
								fileID);
					}
				}
		}

	}
}
