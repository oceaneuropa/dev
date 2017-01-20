package org.plutus.stock.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.origin.common.util.BigDecimalUtil;
import org.plutus.stock.model.dto.YahooFinanceStockData;
import org.plutus.stock.model.runtime.StockData;

public class Converter {

	public static Converter INSTANCE = new Converter();

	/**
	 * 
	 * @param yahooStockDataList
	 * @return
	 */
	public List<StockData> toStockData(List<YahooFinanceStockData> yahooStockDataList) {
		if (yahooStockDataList == null) {
			return null;
		}
		List<StockData> stockDataList = new ArrayList<StockData>();
		for (YahooFinanceStockData yahooStockData : yahooStockDataList) {
			StockData stockData = toStockData(yahooStockData);
			if (stockData != null) {
				stockDataList.add(stockData);
			}
		}
		return stockDataList;
	}

	/**
	 * 
	 * @param yahooStockData
	 * @return
	 */
	public StockData toStockData(YahooFinanceStockData yahooStockData) {
		if (yahooStockData == null) {
			return null;
		}

		StockData stockData = new StockData();

		Date date = yahooStockData.getDate();
		double open = yahooStockData.getOpen();
		double high = yahooStockData.getHigh();
		double low = yahooStockData.getLow();
		double close = yahooStockData.getClose();
		double adjClose = yahooStockData.getAdjClose();
		long volume = yahooStockData.getVolume();

		stockData.setDate(date);
		stockData.setOpen(BigDecimalUtil.toBigDecimal(open));
		stockData.setHigh(BigDecimalUtil.toBigDecimal(high));
		stockData.setLow(BigDecimalUtil.toBigDecimal(low));
		stockData.setClose(BigDecimalUtil.toBigDecimal(close));
		stockData.setAdjClose(BigDecimalUtil.toBigDecimal(adjClose));
		stockData.setVolume(BigDecimalUtil.toBigDecimal(volume));

		return stockData;
	}

}
