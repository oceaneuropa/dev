package org.plutus.stock.resource.util;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.origin.common.json.JSONUtil;
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
 * Read stock data from StockDataResource and save it to output stream.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class StockDataWriter {

	public static String STOCK_DATA_KEY = "stockData";

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(StockDataResource resource, OutputStream output) throws IOException {
		write(resource, output, false);
	}

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @param closeOutputStream
	 * @throws IOException
	 */
	public void write(StockDataResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = contentsToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Write StockDataResource's contents to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject contentsToDocument(StockDataResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		// "stockData" array
		JSONArray stockDataJSONArray = new JSONArray();
		List<Object> contents = resource.getContents();
		int index = 0;
		for (Object content : contents) {
			if (content instanceof StockData) {
				StockData stockData = (StockData) content;
				JSONObject stockDataJSONObject = stockDataToJSON(stockData);
				if (stockDataJSONObject != null) {
					stockDataJSONArray.put(index++, stockDataJSONObject);
				}
			}
		}
		document.put(STOCK_DATA_KEY, stockDataJSONArray);

		return document;
	}

	/**
	 * Convert StockData object to JSONObject.
	 * 
	 * @param workspace
	 * @return
	 */
	protected JSONObject stockDataToJSON(StockData stockData) {
		JSONObject stockDataJSON = new JSONObject();

		// "date" attribute
		Date date = stockData.getDate();
		if (date != null) {
			stockDataJSON.put("date", DateUtil.toString(date, DateUtil.YEAR_MONTH_DAY_FORMAT1));
		}

		// "open" attribute
		BigDecimal open = stockData.getOpen();
		if (open != null) {
			stockDataJSON.put("open", open.toPlainString());
		}

		// "high" attribute
		BigDecimal high = stockData.getHigh();
		if (high != null) {
			stockDataJSON.put("high", high.toPlainString());
		}

		// "low" attribute
		BigDecimal low = stockData.getLow();
		if (low != null) {
			stockDataJSON.put("low", low.toPlainString());
		}

		// "close" attribute
		BigDecimal close = stockData.getClose();
		if (close != null) {
			stockDataJSON.put("close", close.toPlainString());
		}

		// "adjClose" attribute
		BigDecimal adjClose = stockData.getAdjClose();
		if (adjClose != null) {
			stockDataJSON.put("adjClose", adjClose.toPlainString());
		}

		// "volume" attribute
		BigDecimal volume = stockData.getVolume();
		if (volume != null) {
			stockDataJSON.put("volume", volume.toPlainString());
		}

		return stockDataJSON;
	}

}
