package smu.sm.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
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
		linkFile = pathData + "/raw/" + Global.maindata + "_" + type + ".csv";
		br = new BufferedReader(new FileReader(new File(linkFile)));
		while ((temp = br.readLine()) != null) {
			String[] ins = temp.split(" ",2);
			mp.put(ins[0].trim(), ins[1].trim());
		}
		return mp;
	}

	// Get a pair of class of data
	// Class1 < Class2
	public void exrtactPairData(int class1, int class2, ClassifierType type) {
		// File contains ID of each class
		File f1 = new File(pathData + "/bin/" + class1 + ".csv");
		File f2 = new File(pathData + "/bin/" + class2 + ".csv");
		BufferedWriter bwTrain = null, bwTest = null;
		BufferedReader br = null;
		Map<String, String> classData = null;
		String label = "+1";
		String id;
		String temp = "", instance = "";
		Set<String> trainSet = new HashSet<String>();
		Set<String> testSet = new HashSet<String>();
		try {
			// Get data (all classes) with feature for ClassifierType
			classData = this.getClassData(type);
			bwTrain = new BufferedWriter(new FileWriter(new File(pathData + "/"
					+ class1 + "" + class2 + "_" + type + "_train")));
			bwTest = new BufferedWriter(new FileWriter(new File(pathData + "/"
					+ class1 + "" + class2 + "_" + type + "_test")));

			br = new BufferedReader(new FileReader(f1));
			rd = new Random();
			// Pick negative instances
			while ((temp = br.readLine()) != null) {
				id = temp.split(",")[0];
				instance = label + " " + classData.get(id).trim();
				if (rd.nextDouble() < Global.crossvalid) {
					bwTrain.write(instance + "\n");
				} else {
					bwTest.write(instance + "\n");
				}
			}
			// Pick positive instances
			label = "-1";
			br = new BufferedReader(new FileReader(f2));
			rd = new Random();
			while ((temp = br.readLine()) != null) {
				id = temp.split(",")[0];
				instance = label + " " + classData.get(id).trim();
				if (rd.nextDouble() < Global.crossvalid) {
					bwTrain.write(instance + "\n");
				} else {
					bwTest.write(instance + "\n");
				}
			}
			br.close();
			bwTest.flush();
			bwTest.close();
			bwTrain.flush();
			bwTrain.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
