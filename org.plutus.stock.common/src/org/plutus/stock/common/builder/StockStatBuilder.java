package org.plutus.stock.common.builder;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.origin.common.resource.Resource;
import org.origin.common.util.BigDecimalUtil;
import org.origin.common.util.ListUtil;
import org.origin.common.workingcopy.WorkingCopy;
import org.origin.common.workingcopy.WorkingCopyUtil;
import org.plutus.stock.common.StockConstants;
import org.plutus.stock.common.util.Comparators;
import org.plutus.stock.model.runtime.StockData;
import org.plutus.stock.model.runtime.StockStat;

public class StockStatBuilder {

	public static StockStatBuilder INSTANCE = new StockStatBuilder();

	/**
	 * 
	 * @param symbol
	 * @throws IOException
	 */
	public void buildDailyStat(String symbol) throws IOException {
		symbol = StockSymbolHelper.INSTANCE.checkSymbol(symbol);

		List<StockData> stockDataList = StockDataHelper.INSTANCE.getStockData(symbol, Comparators.StockDataComparator.ASC);
		buildDailyStat(symbol, stockDataList);
	}

	/**
	 * 
	 * @param symbol
	 * @param stockDataList
	 * @throws IOException
	 */
	public void buildDailyStat(String symbol, List<StockData> stockDataList) throws IOException {
		symbol = StockSymbolHelper.INSTANCE.checkSymbol(symbol);

		if (stockDataList == null || stockDataList.isEmpty()) {
			return;
		}

		List<StockStat> existingStockStatList = StockStatHelper.INSTANCE.getStockStat(symbol, StockConstants.TYPE_DAILY, Comparators.StockStatComparator.ASC);

		List<StockStat> newStockStatList = new ArrayList<StockStat>();
		for (StockData stockData : stockDataList) {
			Date date = stockData.getDate();
			BigDecimal close = stockData.getClose();
			BigDecimal adjClose = stockData.getAdjClose();
			BigDecimal volume = stockData.getVolume();

			BigDecimal marketCap = BigDecimalUtil.multiply(close, volume);
			BigDecimal adjMarketCap = BigDecimalUtil.multiply(adjClose, volume);

			StockStat stat = new StockStat();
			stat.setDate(date);
			stat.setMarketCap(marketCap);
			stat.setAdjMarketCap(adjMarketCap);

			newStockStatList.add(stat);
		}
		Collections.sort(newStockStatList, Comparators.StockStatComparator.ASC);

		List<StockStat> result = ListUtil.merge(existingStockStatList, newStockStatList, Comparators.StockStatComparator.ASC);

		Path path = StockStatHelper.INSTANCE.getStockStatFile(symbol, StockConstants.TYPE_DAILY);
		WorkingCopy workingCopy = WorkingCopyUtil.getWorkingCopy(path.toUri());
		if (workingCopy != null) {
			Resource resource = workingCopy.getResource();
			if (resource != null) {
				// create file if not exist
				if (!resource.exists()) {
					resource.createNewResource();
				}

				// set contents
				resource.clear();
				resource.getContents().addAll(result);

				// save changes
				workingCopy.save();
			}
		}
	}

}
