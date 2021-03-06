package org.plutus.lottery.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.plutus.lottery.util.Comparators.StatAnalysisComparator;

public class AnalysisRegistry {

	private static AnalysisRegistry INSTANCE = new AnalysisRegistry();

	public static AnalysisRegistry getInstance() {
		return INSTANCE;
	}

	public List<Analysis> analyses = new ArrayList<Analysis>();

	/**
	 * 
	 * @param context
	 */
	public void run(AnalysisContext context) {
		if (context == null) {
			throw new IllegalArgumentException("context is null");
		}
		for (Analysis analysis : this.analyses) {
			try {
				analysis.run(context);

				System.out.println("Analyzed by [" + analysis.getId() + "]");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param context
	 */
	public void printResult(AnalysisContext context) {
		System.out.println();

		for (Analysis analysis : this.analyses) {
			try {
				System.out.println("///////////////////////////////////////////////////////////////////////////////");
				System.out.println("//                                                                           //");
				System.out.println("//\t\t\t[" + analysis.getId() + "] result");
				System.out.println("//                                                                           //");
				System.out.println("///////////////////////////////////////////////////////////////////////////////");
				analysis.printResult(context);
				System.out.println();
				System.out.println();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public List<Analysis> getAnalyses() {
		return this.analyses;
	}

	/**
	 * 
	 * @param analysisId
	 * @return
	 */
	public Analysis getAnalysis(String analysisId) {
		if (analysisId != null) {
			for (Analysis analysis : this.analyses) {
				if (analysisId.equals(analysis.getId())) {
					return analysis;
				}
			}
		}
		return null;
	}

	/**
	 * 
	 * @param analysisId
	 * @return
	 */
	public boolean contains(String analysisId) {
		if (analysisId != null) {
			for (Analysis analysis : this.analyses) {
				if (analysisId.equals(analysis.getId())) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * 
	 * @param analysis
	 * @return
	 */
	public boolean add(Analysis analysis) {
		if (analysis != null && !this.analyses.contains(analysis)) {
			this.analyses.add(analysis);

			sortByPriority();

			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param analysis
	 * @return
	 */
	public boolean remove(Analysis analysis) {
		if (analysis != null && this.analyses.contains(analysis)) {
			return this.analyses.remove(analysis);
		}
		return false;
	}

	/**
	 * 
	 * @param updaterId
	 * @return
	 */
	public boolean remove(String updaterId) {
		if (updaterId != null) {
			Analysis updater = getAnalysis(updaterId);
			if (updater != null) {
				return this.analyses.remove(updater);
			}
		}
		return false;
	}

	/**
	 * 
	 */
	protected void sortByPriority() {
		if (this.analyses.size() > 1) {
			Collections.sort(this.analyses, StatAnalysisComparator.ASC);
		}
	}

}
