package org.plutus.lottery.powerball;

import java.util.List;

public class AnalysisContext {

	protected DrawStat globalStat;
	protected List<Draw> draws;

	public AnalysisContext() {
		this(new DrawStat());
	}

	public AnalysisContext(DrawStat globalStat) {
		this.globalStat = globalStat;
		if (this.globalStat == null) {
			this.globalStat = new DrawStat();
		}
	}

	public DrawStat getGlobalStat() {
		return this.globalStat;
	}

	public void setGlobalStat(DrawStat globalStat) {
		this.globalStat = globalStat;
	}

	public List<Draw> getDraws() {
		return this.draws;
	}

	public void setDraws(List<Draw> draws) {
		this.draws = draws;
	}

}
