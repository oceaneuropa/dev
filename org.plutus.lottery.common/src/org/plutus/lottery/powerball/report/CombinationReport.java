package org.plutus.lottery.powerball.report;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.origin.common.util.DateUtil;
import org.plutus.lottery.common.Comparators;
import org.plutus.lottery.powerball.Draw;

public class CombinationReport {

	protected String combinationKey;
	protected int[] combination;
	protected List<Draw> draws = new ArrayList<Draw>();
	protected List<Integer> numberOfDrawsBetween = new ArrayList<Integer>();

	/**
	 * 
	 * @param combinationKey
	 * @param combination
	 */
	public CombinationReport(String combinationKey, int[] combination) {
		this.combinationKey = combinationKey;
		this.combination = combination;
	}

	public String getCombinationKey() {
		return this.combinationKey;
	}

	public void setCombinationKey(String combinationKey) {
		this.combinationKey = combinationKey;
	}

	public int[] getCombination() {
		return this.combination;
	}

	public void setCombination(int[] combination) {
		this.combination = combination;
	}

	public List<Draw> getDraws() {
		return this.draws;
	}

	public void setDraws(List<Draw> draws) {
		this.draws = draws;

		if (!this.draws.isEmpty()) {
			Collections.sort(this.draws, Comparators.DrawComparator.ASC);
		}
		updateNumberOfDrawsBetween();
	}

	public boolean addDraw(Draw draw) {
		boolean succeed = false;
		if (draw != null && !this.draws.contains(draw)) {
			succeed = this.draws.add(draw);
		}
		if (succeed) {
			if (!this.draws.isEmpty()) {
				Collections.sort(this.draws, Comparators.DrawComparator.ASC);
			}
			updateNumberOfDrawsBetween();
		}
		return succeed;
	}

	public boolean removeDraws(List<Draw> draws) {
		boolean succeed = this.draws.removeAll(draws);
		if (succeed) {
			if (!this.draws.isEmpty()) {
				Collections.sort(this.draws, Comparators.DrawComparator.ASC);
			}
			updateNumberOfDrawsBetween();
		}
		return succeed;
	}

	public int getCount() {
		return this.draws.size();
	}

	public void updateNumberOfDrawsBetween() {
		this.numberOfDrawsBetween.clear();

		boolean printNumOfDrawsBetween = true;
		if (printNumOfDrawsBetween) {
			for (int i = 1; i < this.draws.size(); i++) {
				Draw prevDraw = this.draws.get(i - 1);
				Draw currDraw = this.draws.get(i);

				int prevIndex = prevDraw.getIndex();
				int currIndex = currDraw.getIndex();

				int numOfDrawsBetween = currIndex - prevIndex;
				this.numberOfDrawsBetween.add(numOfDrawsBetween);
			}
		}
	}

	public List<Integer> getNumberOfDrawsBetween() {
		return this.numberOfDrawsBetween;
	}

	@Override
	public String toString() {
		String str = "";

		str += "数字组合 (" + this.combinationKey + ")";
		str += ", 重复" + getCount() + "次";

		String dateStr = "";
		for (int i = 0; i < this.draws.size(); i++) {
			Draw draw = this.draws.get(i);
			if (i > 0) {
				dateStr += ", ";
			}
			dateStr += ("#" + draw.getIndex() + " " + draw.getDateString());
		}
		str += ", 日期 (" + dateStr + ")";

		boolean printDaysBetween = false;
		if (printDaysBetween) {
			String daysBetweenStr = "";
			for (int i = 1; i < this.draws.size(); i++) {
				Draw prevDraw = this.draws.get(i - 1);
				Draw currDraw = this.draws.get(i);

				Date prevDate = prevDraw.getDate();
				Date currDate = currDraw.getDate();

				long daysBetween = DateUtil.getDaysBetween(prevDate, currDate);

				if (i > 1) {
					daysBetweenStr += " $ ";
				}
				daysBetweenStr += (String.valueOf(daysBetween) + "天");
			}
			str += ", 间隔 ($ " + daysBetweenStr + " $)";
		}

		boolean printNumOfDrawsBetween = true;
		if (printNumOfDrawsBetween) {
			String numOfDrawsBetweenStr = "";
			for (int i = 0; i < this.numberOfDrawsBetween.size(); i++) {
				int numOfDrawsBetween = numberOfDrawsBetween.get(i);
				if (i > 0) {
					numOfDrawsBetweenStr += " $ ";
				}
				numOfDrawsBetweenStr += (String.valueOf(numOfDrawsBetween) + "期");
			}
			str += ", 间隔 ($ " + numOfDrawsBetweenStr + " $)";
		}
		return str;
	}

}
