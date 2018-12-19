package org.plutus.lottery.powerball;

public interface Analysis {

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
	 * Register the analysis.
	 * 
	 */
	public void register();

	/**
	 * Unregister the analysis.
	 * 
	 */
	public void unregister();

	/**
	 * Run the analysis.
	 * 
	 * @param context
	 */
	public void run(AnalysisContext context);

}
