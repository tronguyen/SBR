package smu.sm.processing;

import java.util.ArrayList;
import java.util.List;

import smu.sm.analyzer.TargetLabelAnalyzer;
import smu.sm.entity.MyDocument;
import smu.sm.entity.Token;
import edu.smu.utils.StringUtils;

public class TokenGenerator {
	private static final String TITLE_PREFIX = "t:";
	private static final String DESCRIPTION_PREFIX = "d:";
	
	public Token[] generate(MyDocument doc){
		List<Token> tokens = new ArrayList<Token>();
		
		String targetLabel = doc.getCategory();
		tokens.add(new Token(TargetLabelAnalyzer.TARGET_LABEL_PREFIX + ":" + targetLabel));
		
		String title = doc.getTitle();
		for(String tk: StringUtils.explode(title, "[\\s]"))
			tokens.add(new Token(TITLE_PREFIX + tk));
		
		String desc = doc.getDescription();
		for(String tk: StringUtils.explode(desc, "[\\s]"))
			tokens.add(new Token(DESCRIPTION_PREFIX + tk));	
		
		return tokens.toArray(new Token[tokens.size()]);
	}
	
}
