package org.plutus.stock.resource.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
 * Write stock stat from input stream and store the contents in StockStatResource.
 *
 */
public class StockStatReader {

	public static String STOCK_STAT_KEY = "stockStat";

	/**
	 * Read contents of the resource from input stream.
	 * 
	 * @param resource
	 * @param inputStream
	 * @throws IOException
	 */
	public void read(StockStatResource resource, InputStream inputStream) throws IOException {
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
	public void read(StockStatResource resource, InputStream inputStream, boolean closeInputStream) throws IOException {
		JSONObject document = JSONUtil.load(inputStream, closeInputStream);
		if (document != null) {
			List<StockStat> stockStatList = documentToContents(document);
			if (stockStatList != null) {
				resource.getContents().clear();
				resource.getContents().addAll(stockStatList);
			}
		}
	}

	/**
	 * Read an JSON document to get the contents for StockDataResource.
	 * 
	 * @param document
	 * @return
	 */
	protected List<StockStat> documentToContents(JSONObject document) {
		if (document == null) {
			return null;
		}

		List<StockStat> stockStatList = new ArrayList<StockStat>();

		// "stockStat" array
		if (document.has(STOCK_STAT_KEY)) {
			JSONArray stockStatJSONArray = document.getJSONArray(STOCK_STAT_KEY);
			if (stockStatJSONArray != null) {
				int length = stockStatJSONArray.length();
				for (int i = 0; i < length; i++) {
					JSONObject stockStatJSONObject = stockStatJSONArray.getJSONObject(i);
					if (stockStatJSONObject != null) {
						StockStat stockStat = jsonToStockStat(stockStatJSONObject);
						if (stockStat != null) {
							stockStatList.add(stockStat);
						}
					}
				}
			}
		}

		return stockStatList;
	}

	/**
	 * Converts JSON object to a StockStat object.
	 * 
	 * @param stockStatJSONObject
	 * @return
	 */
	protected StockStat jsonToStockStat(JSONObject stockStatJSONObject) {
		if (stockStatJSONObject == null) {
			return null;
		}

		StockStat stockStat = new StockStat();

		// "date" attribute
		Date date = null;
		if (stockStatJSONObject.has("date")) {
			String dateString = stockStatJSONObject.getString("date");
			if (dateString != null) {
				date = DateUtil.toDate(dateString, DateUtil.YEAR_MONTH_DAY_FORMAT1);

			}
		}
		stockStat.setDate(date);

		// "marketCap" attribute
		if (stockStatJSONObject.has("marketCap")) {
			double marketCap = stockStatJSONObject.getDouble("marketCap");
			stockStat.setMarketCap(marketCap);
		}

		// "adjMarketCap" attribute
		if (stockStatJSONObject.has("adjMarketCap")) {
			double adjMarketCap = stockStatJSONObject.getDouble("adjMarketCap");
			stockStat.setAdjMarketCap(adjMarketCap);
		}

		return stockStat;
	}

}
