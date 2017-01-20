package org.plutus.stock.common.test;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runners.MethodSorters;
import org.plutus.stock.common.builder.StockDataBuilder;
import org.plutus.stock.common.builder.StockDataHelper;
import org.plutus.stock.common.util.Comparators;
import org.plutus.stock.common.util.SetupUtil;
import org.plutus.stock.model.runtime.StockData;
import org.plutus.stock.resource.StockDataResourceFactory;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StockDataTest {

	public StockDataTest() {
		setUp();
	}

	protected void setUp() {
		// set stock_home
		System.setProperty(SetupUtil.STOCK_HOME, "/Users/yayang/Downloads/stock_home");

		// registry working copy factory
		StockDataResourceFactory.register();
	}

	@Ignore
	@Test
	public void test001_importStockData_BAC() throws IOException {
		System.out.println("--- --- --- test001_importStockData_BAC() --- --- ---");

		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 11, 10);

		Calendar endDate = Calendar.getInstance();
		endDate.set(2016, 11, 18);

		StockDataBuilder.INSTANCE.syncStockData("BAC", startDate, endDate);
	}

	@Test
	public void test002_getStockData_BAC() throws IOException {
		System.out.println("--- --- --- test002_getStockData_BAC() --- --- ---");

		List<StockData> stockDataList = StockDataHelper.INSTANCE.getStockData("BAC", Comparators.StockDataComparator.ASC);
		if (stockDataList != null) {
			File file = StockDataHelper.INSTANCE.getStockDataFile(stockDataList);
			System.out.println("file is " + file);

			for (StockData stockData : stockDataList) {
				System.out.println(stockData.toString());
			}
		}
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(StockDataTest.class);

		System.out.println("--- --- --- StockDataTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
