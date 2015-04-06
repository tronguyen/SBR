package smu.sm.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import weka.core.Instance;

public class MyHelper {
	public static void refineLDAData(File ldaInput, File ldaOutput) {
		BufferedReader br = null;
		BufferedWriter bw = null;
		String temp = "";
		StringBuffer sb = new StringBuffer("");
		int index;
		String[] line;
		try {
			br = new BufferedReader(new FileReader(ldaInput));
			bw = new BufferedWriter(new FileWriter(ldaOutput));
			while ((temp = br.readLine()) != null) {
				index = 1;
				line = temp.split(",");
				sb.append(line[0]);
				for (int i = 1; i < line.length; i++) {
					sb.append(" " + (index++) + ":" + line[i]);
				}
				bw.write(sb.toString().trim() + "\n");
				sb.setLength(0);
			}
			bw.flush();
			bw.close();
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	public static Instance createInstance(String line) {
		Instance ins = null;
//		try {
//			
//            ArrayList<Attribute> attributeList = new ArrayList<Attribute>(2);
//
//            Attribute latitude = new Attribute("latitude");
//            Attribute longitude = new Attribute("longitude");
//            Attribute carbonmonoxide = new Attribute("co");
//
//            ArrayList<String> classVal = new ArrayList<String>();
//            classVal.add("ClassA");
//            classVal.add("ClassB");
//
//
//            attributeList.add(latitude);
//            attributeList.add(longitude);
//            attributeList.add(carbonmonoxide);
//            attributeList.add(new Attribute("@@class@@",classVal));
//
//            Instances data = new Instances("TestInstances",attributeList,0);
//
//
//            // Create instances for each pollutant with attribute values latitude,
//            // longitude and pollutant itself
//            ins = new DenseInstance(data.numAttributes());
//            ins.setValue(latitude, lat);
//
//
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
		return ins;
	}
}
