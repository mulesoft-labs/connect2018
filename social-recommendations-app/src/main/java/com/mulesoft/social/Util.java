package com.mulesoft.social;

public class Util {
	
	public static TopChoiceResult getTopRecommendation(ChoiceResult[][] allRecommendations) {
		TopChoiceResult topChoiceResult = new TopChoiceResult();
		topChoiceResult.setCount(-1);
		topChoiceResult.setTotal(0);
		for (ChoiceResult[] row : allRecommendations) {
			for (ChoiceResult choiceResult : row) {
				if (topChoiceResult.getCount() == -1 || choiceResult.getCount() > topChoiceResult.getCount()) {
					topChoiceResult.artPiece = choiceResult.artPiece;
					topChoiceResult.count = choiceResult.count;
					topChoiceResult.paintColor = choiceResult.paintColor;
					topChoiceResult.last = choiceResult.last;
				}
				topChoiceResult.total += choiceResult.getCount();
			}
		}
		return topChoiceResult;
	}
}
