package smu.sm.ml;

import java.io.IOException;

import smu.sm.ml.svm.SVMPredictor;
import smu.sm.ml.svm.SVMTrainer;
import edu.smu.utils.FileUtils;


public class L1Classifier {

	public void train(String[] args) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.train(args);
	}
	
	public void train(String dataRes, String modelRes) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.train(dataRes, modelRes);
	}

	public void classify(String testRes, String modelRes, String outputFile) throws IOException{
		SVMPredictor predictor = new SVMPredictor();
		predictor.predictFile(testRes, modelRes, outputFile);
	}
	
	public void crossValidation(String[] args) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.crossValidation(args);
	}

	public void crossValidation(String dataRes, String modelRes, int nbFolds) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.crossValidation(dataRes, modelRes, nbFolds);
	}

	public void testCrossValidation() throws IOException {
		String[] dataset = new String[]{"linux", "microsoft"};
		String[] models = new String[]{"uni", "big"};

		boolean baseline = true;
		boolean fiveClass = true;

		for(String dat: dataset){
			for(String model: models){
				String modelRes = "data/train/" + dat;
				if(baseline) modelRes += "_baseline";
				modelRes += "_" + model + ".model";

				String dataRes = "data/train/" + dat;
				if(baseline) dataRes += "_baseline";
				if(fiveClass) dataRes += "_5class";
				dataRes += "_" + model + ".txt";

				crossValidation(dataRes, modelRes, 5);

				System.out.println("Dataset: " + dat + " | Model: " + model );
				System.out.println("\n*****************************************************\n");
			}
		}
	}

	public void testDataFolds(String foldDir) throws IOException{
		String dataset = FileUtils.getBaseName(foldDir);
		String[] models = new String[]{"UNI", "BIG", "LDA"};

		String[] foldPaths = FileUtils.listName(foldDir, true);

		for(String foldPath: foldPaths){
			System.out.println("\n------------------------------------------------------------");
			System.out.println("Processing fold " + foldPath);

			for(String model: models){

				String trainRes = foldPath + "/" + dataset + "_" + model + "_train.txt";
				String modelRes = foldPath + "/" + dataset + "_" + model + ".model";
				String testRes = foldPath + "/" + dataset + "_" + model + "_fVector_test.txt";
				String outputFile = foldPath + "/" + dataset + "_" + model + "_predict.txt";

				train(trainRes, modelRes);

				classify(testRes, modelRes, outputFile);
				System.err.println(dataset + " | " + model);
			}

		}
	}

	public static void main(String[] args) throws IOException{
		String[] datas = new String[]{"letter", "svmguide1", "splice"};

		for(String data: datas){
			String trainRes = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/data/" + data + ".train.txt";
			String modelRes = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/" + data + ".model";
			String testRes = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/data/" + data + ".test.txt";
			String outputFile = "D:/Dropbox/SMU/PhD-Program/Term_2/IS712-ML/Labs/Lab04/" + data + ".predict.txt";



			L1Classifier classifier = new L1Classifier();
			classifier.crossValidation(trainRes, modelRes, 10);
		}
	}
}
