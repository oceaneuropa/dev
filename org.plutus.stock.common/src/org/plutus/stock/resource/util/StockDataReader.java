package org.plutus.stock.resource.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
import org.origin.common.util.BigDecimalUtil;
import org.origin.common.util.DateUtil;
import org.plutus.stock.model.runtime.StockData;
import org.plutus.stock.resource.StockDataResource;

/*

Example:
-----------------------------------------------------------------------
{
    "stockData": [
    	{
    		"date": "2016-10-01",
			"open": 123.00,
			"high": 124.00
			"low": 122.00,
			"close": 123.78,
			"adjClose": 123.45,
			"volume": 102000
		},
    	{
    		"date": "2016-10-02",
			"open": 123.45,
			"high": 125.00
			"low": 123.00,
			"close": 124.80,
			"adjClose": 124.95,
			"volume": 103000
		}
	]
}
-----------------------------------------------------------------------

*/
/**
 * Write stock data from input stream and store the contents in StockDataResource.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class StockDataReader {

	public static String STOCK_DATA_KEY = "stockData";

	/**
	 * Read contents of the resource from input stream.
	 * 
	 * @param resource
	 * @param inputStream
	 * @throws IOException
	 */
	public void read(StockDataResource resource, InputStream inputStream) throws IOException {
		read(resource, inputStream, false);
	}

	/**
	 * Read contents of the resource from input stream.
	 * 
	 * @param resource
	 * @param inputStream
	 * @param closeInputStream
	 * @throws IOException
	 */
	public void read(StockDataResource resource, InputStream inputStream, boolean closeInputStream) throws IOException {
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			List<StockData> stockDataList = documentToContents(document);
			if (stockDataList != null) {
				// resource.getContents().clear();
				resource.getContents().addAll(stockDataList);
			}
		}
	}

	/**
	 * Read an JSON document to get the contents for StockDataResource.
	 * 
	 * @param document
	 * @return
	 */
	protected List<StockData> documentToContents(JSONObject document) {
		if (document == null) {
			return null;
		}

		List<StockData> stockDataList = new ArrayList<StockData>();

		// "stockData" array
		if (document.has(STOCK_DATA_KEY)) {
			JSONArray stockDataJSONArray = document.getJSONArray(STOCK_DATA_KEY);
			if (stockDataJSONArray != null) {
				int length = stockDataJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject stockDataJSONObject = stockDataJSONArray.getJSONObject(i);
					if (stockDataJSONObject != null) {
						StockData stockData = jsonToStockData(stockDataJSONObject);
						if (stockData != null) {
							stockDataList.add(stockData);
						}
					}
				}
			}
		}

		return stockDataList;
	}

	/**
	 * Converts JSON object to a StockData object.
	 * 
	 * @param stockDataJSONObject
	 * @return
	 */
	protected StockData jsonToStockData(JSONObject stockDataJSONObject) {
		if (stockDataJSONObject == null) {
			return null;
		}

		StockData stockData = new StockData();

		// "date" attribute
		Date date = null;
		if (stockDataJSONObject.has("date")) {
			String dateString = stockDataJSONObject.getString("date");
			if (dateString != null) {
				date = DateUtil.toDate(dateString, DateUtil.YEAR_MONTH_DAY_FORMAT1);

			}
		}
		stockData.setDate(date);

		// "open" attribute
		if (stockDataJSONObject.has("open")) {
			double open = stockDataJSONObject.getDouble("open");
			stockData.setOpen(BigDecimalUtil.toBigDecimal(open));
		}

		// "high" attribute
		if (stockDataJSONObject.has("high")) {
			double high = stockDataJSONObject.getDouble("high");
			stockData.setHigh(BigDecimalUtil.toBigDecimal(high));
		}

		// "low" attribute
		if (stockDataJSONObject.has("low")) {
			double low = stockDataJSONObject.getDouble("low");
			stockData.setLow(BigDecimalUtil.toBigDecimal(low));
		}

		// "close" attribute
		if (stockDataJSONObject.has("close")) {
			double close = stockDataJSONObject.getDouble("close");
			stockData.setClose(BigDecimalUtil.toBigDecimal(close));
		}

		// "adjClose" attribute
		if (stockDataJSONObject.has("adjClose")) {
			double adjClose = stockDataJSONObject.getDouble("adjClose");
			stockData.setAdjClose(BigDecimalUtil.toBigDecimal(adjClose));
		}

		// "volume" attribute
		if (stockDataJSONObject.has("volume")) {
			long volume = stockDataJSONObject.getLong("volume");
			stockData.setVolume(BigDecimalUtil.toBigDecimal(volume));
		}

		return stockData;
	}

}
