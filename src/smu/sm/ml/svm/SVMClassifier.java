package smu.sm.ml.svm;

import java.io.IOException;

import smu.sm.ml.L1Classifier;


public class SVMClassifier {

	public void train(String dataRes, String modelRes) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.train(dataRes, modelRes);
	}

	public void classify(String testRes, String modelRes, String outputFile) throws IOException{
		SVMPredictor predictor = new SVMPredictor();
		predictor.predictFile(testRes, modelRes, outputFile);
	}

	public void crossValidation(String dataRes, String modelRes, int nbFolds) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.crossValidation(dataRes, modelRes, nbFolds);
	}

	public static void main(String[] args) throws IOException, Exception{
		String[] datas = new String[]{"letter", "svmguide1", "splice"};
		String data = datas[0];

		double C_vals[] = new double[]{
				0.10, 0.12, 0.17, 0.21, 0.25, 0.89, 1.07, 1.28, 1.85, 2.66, 4.60
		};

		for(int i = 0; i < C_vals.length; i++){
			double C = C_vals[i];
			for(int t = 0; t < 4; t++){
				System.out.println("\n****************************************************************");
				System.out.println("Data :" + data + " | C = " + C + " | T = " + 1 );
				String trainRes = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/data/" + data + ".train.txt";
				String modelRes = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/" + data + ".model";
				String testRes = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/data/" + data + ".test.txt";
				String outputFile = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/" + data + ".predict.txt";


				String[] argv = new String[] {
						"-s", "0",
						"-t", t + "",
						"-c", C + "",
						"-v", "5",
						trainRes,
						modelRes
				};

				L1Classifier classifier = new L1Classifier();
				classifier.crossValidation(argv);

				Thread.sleep(3000);
			}

		}

	}
}
