package smu.sm.processing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

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
}
