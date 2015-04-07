package smu.sm.analyzer;

import java.util.ArrayList;
import java.util.List;

import smu.sm.entity.FeatureHolder;
import smu.sm.entity.Token;

public class BigramAnalyzer implements Analyzer {
	private static final String PREFIX = "big:";

	@Override
	public void analyze(Token[] tokens, FeatureHolder featureHolder) {
		for(int i = 0; i < tokens.length; i++){
			String tk = tokens[i].getToken();
			for(String big: createBigram(tk))
				featureHolder.addFeature(PREFIX + big);
		}
	}
	
	private String[] createBigram(String tk){
		List<String> holder = new ArrayList<String>();
		for(int i = 0; i < tk.length() - 1; i++)
			holder.add(tk.charAt(i) + "" + tk.charAt(i+1));
		return holder.toArray(new String[holder.size()]);
	}

}
