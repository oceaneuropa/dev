package org.plutus.stock.common.test;

import java.io.IOException;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.plutus.stock.common.StockConstants;
import org.plutus.stock.common.builder.StockDataHelper;
import org.plutus.stock.common.builder.StockStatBuilder;
import org.plutus.stock.common.builder.StockStatHelper;
import org.plutus.stock.common.util.Comparators;
import org.plutus.stock.common.util.SetupUtil;
import org.plutus.stock.model.runtime.StockData;
import org.plutus.stock.model.runtime.StockStat;
import org.plutus.stock.resource.StockDataResourceFactory;
import org.plutus.stock.resource.StockStatResourceFactory;

/**
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockStatTest {

	public StockStatTest() {
		setUp();
	}

	protected void setUp() {
		// set stock_home
		// System.setProperty(SetupUtil.STOCK_HOME, "/Users/oceaneuropa/Downloads/stock_home");
		System.setProperty(SetupUtil.STOCK_HOME, "/Users/jessylxj/dev/stock_home");

		// registry working copy factory
		StockDataResourceFactory.register();
		StockStatResourceFactory.register();
	}

	@Ignore
	@Test
	public void test001_buildDailyStat_BAC() throws IOException {
		System.out.println("--- --- --- test001_buildDailyStat_BAC() --- --- ---");

		List<StockData> stockDataList = StockDataHelper.INSTANCE.getStockData("BAC", Comparators.StockDataComparator.ASC);
		StockStatBuilder.INSTANCE.buildDailyStat("BAC", stockDataList);
	}

	@Test
	public void test002_getDailyStat_BAC() throws IOException {
		System.out.println("--- --- --- test002_getDailyStat_BAC() --- --- ---");

		List<StockStat> stockStatList = StockStatHelper.INSTANCE.getStockStat("BAC", StockConstants.TYPE_DAILY, Comparators.StockStatComparator.ASC);
		for (StockStat stockStat : stockStatList) {
			System.out.println(stockStat.toString());
		}
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(StockStatTest.class);

		System.out.println("--- --- --- StockStatTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
