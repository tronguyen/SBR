package smu.sm.global;

public class Global {
	// Link to raw data
	public static String csvPath = "data/";
	public static String maindata = "microsoft"; // "linux"
	public static double crossvalid = 0.9; // 10-folds
	public static double validprob = 0.2;
	public static int folds = 10; // max =10
	public static double thresholdDecision = 0;
	public static int vloop = 5;
	public static double penalize = 0.995;
}
