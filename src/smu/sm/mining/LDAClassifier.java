package smu.sm.mining;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import smu.sm.entity.MyDocument;
import smu.sm.global.ClassifierType;
import smu.sm.processing.MyHelper;

public class LDAClassifier extends ClassifierL2 {
	public LDAClassifier(ClassifierType type) {
		super(type);
		// TODO Auto-generated constructor stub
	}

	static String path = "/Volumes/DATA/Universe/SMU/Software Mining/Project/lda_se/cmd";

	// Enrich bug reports
	public void enrichContext(MyDocument doc) {
		
	}

	public void extractTopic() throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		String cmd = "java -jar " + path + "/tmt-0.4.0.jar " + path
				+ "/lda-learn.scala";
		Process process = runtime.exec(cmd);
		process.waitFor(10, TimeUnit.MINUTES);
		System.out.println("Done!");
	}

	@Override
	public void test() {
		// TODO Auto-generated method stub

	}

	@Override
	public void train() {
		// TODO Auto-generated method stub
		
	}

}
