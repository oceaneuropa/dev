package org.plutus.lottery.powerball.impl;

import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;

public abstract class AnalysisImpl implements Analysis {

	@Override
	public void printResult(AnalysisContext context) {
		System.out.println("(empty)");
	}

	/**
	 * 
	 * @param count
	 * @param dashed
	 * @return
	 */
	protected String addSpace(int count, boolean dashed) {
		String spaces = "";
		for (int i = 0; i < count; i++) {
			if (dashed) {
				spaces = spaces + "---";
			} else {
				spaces = spaces + "   ";
			}
		}
		return spaces;
	}

}
