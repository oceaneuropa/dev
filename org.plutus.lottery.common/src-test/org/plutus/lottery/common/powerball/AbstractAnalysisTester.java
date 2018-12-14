package org.plutus.lottery.common.powerball;

import java.io.File;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.plutus.lottery.powerball.Analysis;
import org.plutus.lottery.powerball.AnalysisContext;
import org.plutus.lottery.powerball.AnalysisRegistry;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawReaderV1;
import org.plutus.lottery.powerball.analysis.A11_MinMaxAvgAnalysis;
import org.plutus.lottery.powerball.analysis.A21_OddEvenAnalysis;
import org.plutus.lottery.powerball.analysis.A22_SumAnalysis;
import org.plutus.lottery.powerball.analysis.A23_HotColdAnalysis;
import org.plutus.lottery.powerball.analysis.A24_RepetitionAnalysis;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public abstract class AbstractAnalysisTester {

	protected static File file;
	protected static boolean printDraws;
	protected static AnalysisContext context = new AnalysisContext();

	/**
	 * 
	 * @param file
	 */
	public AbstractAnalysisTester(File file) {
		AbstractAnalysisTester.file = file;
		setup();
	}

	protected void setup() {
		printDraws = false;

		A11_MinMaxAvgAnalysis.INSTANCE.start();

		A21_OddEvenAnalysis.INSTANCE.start();
		A24_RepetitionAnalysis.INSTANCE.start();
		A22_SumAnalysis.INSTANCE.start();
		A23_HotColdAnalysis.INSTANCE.start();
	}

	@Test
	public void test001_readDraws() {
		System.out.println("--- --- --- test001_readDraws() --- --- ---");

		try {
			List<Draw> draws = DrawReaderV1.read(file);
			context.setDraws(draws);

			if (printDraws) {
				System.out.println("Total number of draws is: " + draws.size());

				System.out.println("----------------------------------------------------------------------");
				for (Draw draw : draws) {
					System.out.println(draw);
				}
				System.out.println("----------------------------------------------------------------------");
			}
			System.out.println("Total number of draws is: " + draws.size());

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	@Test
	public void test002_runAnalyses() {
		System.out.println("--- --- --- test002_runAnalyses() --- --- ---");

		try {
			List<Analysis> analyses = AnalysisRegistry.getInstance().getAnalyses();
			for (Analysis analysis : analyses) {
				try {
					analysis.run(context);

					System.out.println("Analyzed by [" + analysis.getClass().getSimpleName() + "] (id=" + analysis.getId() + ", priority=" + analysis.getPriority() + ")");

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println();
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
