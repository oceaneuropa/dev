package org.plutus.lottery.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.origin.common.io.IOUtil;
import org.origin.common.util.SystemUtils;
import org.plutus.lottery.powerball.Draw;
import org.plutus.lottery.powerball.DrawReader;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PowerballTest {

	public PowerballTest() {
		setUp();
	}

	protected void setUp() {
	}

	@Test
	public void test001() {
		System.out.println("--- --- --- test001() --- --- ---");

		File pbWinNums = new File(SystemUtils.getUserDir(), "doc/pb-winnums-text_02-22-2017.txt");
		if (!pbWinNums.exists()) {
			System.err.println(pbWinNums.getAbsolutePath() + " does not exist.");
			return;
		}

		FileInputStream fis = null;
		try {
			fis = new FileInputStream(pbWinNums);
			DrawReader reader = new DrawReader();
			List<Draw> draws = reader.read(fis);
			System.out.println("draws.size() = " + draws.size());
			System.out.println("----------------------------------------------------------------------");
			for (Draw draw : draws) {
				System.out.println(draw);
			}
			System.out.println("----------------------------------------------------------------------");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			IOUtil.closeQuietly(fis, true);
		}
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(PowerballTest.class);
		System.out.println("--- --- --- PowerballTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
