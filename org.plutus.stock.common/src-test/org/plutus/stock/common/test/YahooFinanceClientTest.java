package org.plutus.stock.common.test;

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
import org.origin.common.rest.client.ClientException;
import org.plutus.stock.common.client.YahooFinanceClient;
import org.plutus.stock.common.util.Comparators;
import org.plutus.stock.model.dto.YahooFinanceStockData;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class YahooFinanceClientTest {

	protected YahooFinanceClient yahooFinanceClient;

	public YahooFinanceClientTest() {
		this.yahooFinanceClient = getClient();
	}

	protected void setUp() {
		this.yahooFinanceClient = getClient();
	}

	protected YahooFinanceClient getClient() {
		return new YahooFinanceClient();
	}

	// @Ignore
	@Test
	public void test001_listStockData_BAC() throws IOException {
		System.out.println("--- --- --- test001_listStockData_BAC() --- --- ---");

		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 11, 8);

		Calendar endDate = Calendar.getInstance();
		endDate.set(2016, 11, 18);

		try {
			List<YahooFinanceStockData> stockDataList = this.yahooFinanceClient.getStockData("BAC", startDate, endDate, YahooFinanceClient.TYPE_DAILY, YahooFinanceClient.IGNORE_CSV, Comparators.YahooStockDataComparator.ASC);

			// System.out.println("BAC stockDataList.size() = " + stockDataList.size());
			//
			// for (YahooFinanceStockData stockData : stockDataList) {
			// System.out.println(stockData.toString());
			// }
		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	@Ignore
	@Test
	public void test002_listStockData_BABA() throws IOException {
		System.out.println("--- --- --- test002_listStockData_BABA() --- --- ---");

		Calendar startDate = Calendar.getInstance();
		startDate.set(2016, 0, 8);

		Calendar endDate = Calendar.getInstance();
		endDate.set(2016, 11, 28);

		try {
			List<YahooFinanceStockData> stockDataList = this.yahooFinanceClient.getStockData("BABA", startDate, endDate, YahooFinanceClient.TYPE_DAILY, YahooFinanceClient.IGNORE_CSV, Comparators.YahooStockDataComparator.ASC);
			System.out.println("BABA stockDataList.size() = " + stockDataList.size());

			for (YahooFinanceStockData stockData : stockDataList) {
				System.out.println(stockData.toString());
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		System.out.println();
	}

	public static void main(String[] args) {
		Result result = JUnitCore.runClasses(YahooFinanceClientTest.class);

		System.out.println("--- --- --- YahooFinanceClientTest.main() --- --- ---");
		for (Failure failure : result.getFailures()) {
			System.out.println(failure.toString());
		}
		System.out.println("result.wasSuccessful() = " + result.wasSuccessful());
	}

}
