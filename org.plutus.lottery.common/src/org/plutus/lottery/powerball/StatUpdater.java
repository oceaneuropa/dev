package org.plutus.lottery.powerball;

import java.util.List;

public interface StatUpdater {

	/**
	 * Get updater id.
	 * 
	 * @return
	 */
	public String getId();

	/**
	 * Get priority. (highest to lowest) 1 to 10.
	 * 
	 * @return
	 */
	public int getPriority();

	/**
	 * Update the statistical results for draws.
	 * 
	 * @param globalStat
	 * @param draws
	 */
	public void update(DrawStat globalStat, List<Draw> draws);

}
