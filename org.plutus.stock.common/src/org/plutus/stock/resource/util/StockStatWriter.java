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
import org.plutus.stock.model.runtime.StockStat;
import org.plutus.stock.resource.StockStatResource;

/*

Example:
-----------------------------------------------------------------------
{
    "stockStat": [
    	{
    		"date": "2016-10-01",
			"marketCap": 123000000
		},
    	{
    		"date": "2016-10-02",
			"marketCap": 131000000
		}
	]
}
-----------------------------------------------------------------------

*/

/**
 * Read stock data from StockStatResource and save it to output stream.
 *
 */
public class StockStatWriter {

	public static String STOCK_STAT_KEY = "stockStat";

	/**
	 * Write contents of the resource to output stream.
	 * 
	 * @param resource
	 * @param output
	 * @throws IOException
	 */
	public void write(StockStatResource resource, OutputStream output) throws IOException {
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
	public void write(StockStatResource resource, OutputStream output, boolean closeOutputStream) throws IOException {
		JSONObject document = contentsToDocument(resource);
		if (document != null) {
			JSONUtil.save(document, output, closeOutputStream);
		}
	}

	/**
	 * Write StockStatResource's contents to JSON document.
	 * 
	 * @param resource
	 * @return
	 */
	protected JSONObject contentsToDocument(StockStatResource resource) {
		if (resource == null) {
			return null;
		}

		JSONObject document = new JSONObject();

		// "stockStat" array
		JSONArray stockStatJSONArray = new JSONArray();
		List<Object> contents = resource.getContents();
		int index = 0;
		for (Object content : contents) {
			if (content instanceof StockStat) {
				StockStat stockStat = (StockStat) content;
				JSONObject stockStatJSONObject = stockStatToJSON(stockStat);
				if (stockStatJSONObject != null) {
					stockStatJSONArray.put(index++, stockStatJSONObject);
				}
			}
		}
		document.put(STOCK_STAT_KEY, stockStatJSONArray);

		return document;
	}

	/**
	 * Convert StockData object to JSONObject.
	 * 
	 * @param stockStat
	 * @return
	 */
	protected JSONObject stockStatToJSON(StockStat stockStat) {
		JSONObject stockStatJSON = new JSONObject();

		// "date" attribute
		Date date = stockStat.getDate();
		if (date != null) {
			stockStatJSON.put("date", DateUtil.toString(date, DateUtil.YEAR_MONTH_DAY_FORMAT1));
		}

		// "marketCap" attribute
		BigDecimal marketCap = stockStat.getMarketCap();
		if (marketCap != null) {
			stockStatJSON.put("marketCap", marketCap.toPlainString());
		}

		// "adjMarketCap" attribute
		BigDecimal adjMarketCap = stockStat.getAdjMarketCap();
		if (adjMarketCap != null) {
			stockStatJSON.put("adjMarketCap", adjMarketCap.toPlainString());
		}

		return stockStatJSON;
	}

}
