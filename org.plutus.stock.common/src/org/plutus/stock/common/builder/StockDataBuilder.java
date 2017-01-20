package org.plutus.stock.common.builder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Calendar;
import java.util.List;

import org.origin.common.resource.Resource;
import org.origin.common.rest.client.ClientException;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.plutus.stock.common.client.YahooFinanceClient;
import org.plutus.stock.common.util.Comparators;
import org.plutus.stock.model.Converter;
import org.plutus.stock.model.dto.YahooFinanceStockData;
import org.plutus.stock.model.runtime.StockData;

public class StockDataBuilder {

	public static StockDataBuilder INSTANCE = new StockDataBuilder();

	/**
	 * Download stock data and store it in a local file.
	 * 
	 * @param symbol
	 *            stock symbol
	 * @throws IOException
	 */
	public void syncStockData(String symbol) throws IOException {
		symbol = StockSymbolHelper.INSTANCE.checkSymbol(symbol);

		syncStockData(symbol, null, null);
	}

	/**
	 * Download stock data and store it in a local file.
	 * 
	 * @param symbol
	 *            stock symbol
	 * @param startDate
	 * @param endDate
	 * @throws IOException
	 */
	public void syncStockData(String symbol, Calendar startDate, Calendar endDate) throws IOException {
		symbol = StockSymbolHelper.INSTANCE.checkSymbol(symbol);

		// check start date
		if (startDate == null) {
			// a very early start date
			startDate = Calendar.getInstance();
			startDate.set(1900, 0, 1);
		}

		// check end date
		if (endDate == null) {
			// today
			endDate = Calendar.getInstance();
		}

		YahooFinanceClient yahooFinanceClient = new YahooFinanceClient();

		List<StockData> stockDataList = null;
		try {
			List<YahooFinanceStockData> yahooStockDataList = yahooFinanceClient.getStockData("BAC", startDate, endDate, YahooFinanceClient.TYPE_DAILY, YahooFinanceClient.IGNORE_CSV, Comparators.YahooStockDataComparator.ASC);
			if (yahooStockDataList != null) {
				for (YahooFinanceStockData stockData : yahooStockDataList) {
					System.out.println(stockData.toString());
				}
				stockDataList = Converter.INSTANCE.toStockData(yahooStockDataList);
			}
		} catch (ClientException e) {
			e.printStackTrace();
		}

		Path path = StockDataHelper.INSTANCE.getStockDataFile(symbol);
		if (path != null) {
			WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(path.toFile());
			if (workingCopy != null) {
				Resource resource = workingCopy.getResource();
				if (resource != null) {
					resource.getContents().clear();
					if (stockDataList != null) {
						resource.getContents().addAll(stockDataList);
					}
				}
				try {
					workingCopy.save();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
