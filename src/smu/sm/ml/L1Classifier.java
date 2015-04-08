package smu.sm.ml;

import java.io.IOException;

import smu.sm.ml.svm.SVMTrainer;


public class L1Classifier {

	public void train(String dataRes, String modelRes) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.train(dataRes, modelRes);
	}

	public void crossValidation(String dataRes, String modelRes, int nbFolds) throws IOException{
		SVMTrainer trainer = new SVMTrainer();
		trainer.crossValidation(dataRes, modelRes, nbFolds);
	}

	public static void main(String[] args) throws IOException{

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


				L1Classifier classifier = new L1Classifier();
				classifier.crossValidation(dataRes, modelRes, 5);

				System.out.println("Dataset: " + dat + " | Model: " + model );
				System.out.println("\n*****************************************************\n");
			}
		}
	}
}
