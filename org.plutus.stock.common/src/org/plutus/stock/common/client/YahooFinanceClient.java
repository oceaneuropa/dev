package org.plutus.stock.common.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.AbstractClient;
import org.origin.common.rest.client.ClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.client.ClientUtil;
import org.origin.common.util.DateUtil;
import org.plutus.stock.model.dto.YahooFinanceStockData;

/**
 * YahooFinance web service client to retrieve stock data.
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class YahooFinanceClient extends AbstractClient {

	// type constants
	public static final String TYPE_DAILY = "d";
	public static final String TYPE_WEEKLY = "w";
	public static final String TYPE_YEARLY = "y";

	// ignore constants
	public static final String IGNORE_CSV = ".csv";

	protected static final String NEW_LINE = "\n";

	// log
	protected boolean info = true;
	protected boolean debug = false;

	/**
	 * 
	 */
	public YahooFinanceClient() {
		super(ClientConfiguration.get("http://chart.finance.yahoo.com:80", "table.csv", null));
	}

	/**
	 * 
	 * @param config
	 */
	public YahooFinanceClient(ClientConfiguration config) {
		super(config);
	}

	/**
	 * Get stock data.
	 * 
	 * Example URL:
	 * 
	 * http://chart.finance.yahoo.com/table.csv?s=BAC&a=10&b=8&c=2015&d=11&e=28&f=2016&g=d&ignore=.csv
	 * 
	 * @param symbol
	 * @param startDate
	 * @param endDate
	 * @param type
	 *            'd' - daily. 'w' - weekly. 'y' - yearly.
	 * @param ignore
	 *            '.csv'
	 * @param comparator
	 * 
	 * @return
	 * @throws ClientException
	 */
	public List<YahooFinanceStockData> getStockData(String symbol, Calendar startDate, Calendar endDate, String type, String ignore, Comparator<YahooFinanceStockData> comparator) throws ClientException {
		if (this.info) {
			String startDateString = null;
			if (startDate != null) {
				int year = startDate.get(Calendar.YEAR);
				int month = startDate.get(Calendar.MONTH) + 1;
				int day = startDate.get(Calendar.DATE);
				startDateString = year + "-" + month + "-" + day;
			} else {
				startDateString = "null";
			}

			String endDateString = null;
			if (endDate != null) {
				int year = endDate.get(Calendar.YEAR);
				int month = endDate.get(Calendar.MONTH) + 1;
				int day = endDate.get(Calendar.DATE);
				endDateString = year + "-" + month + "-" + day;
			} else {
				endDateString = "null";
			}

			System.out.println("getStockData()");
			System.out.println("    symbol: " + symbol);
			System.out.println("    startDate: " + startDateString);
			System.out.println("    endDate: " + endDateString);
			System.out.println("    type: " + type);
			System.out.println("    ignore: " + ignore);
		}

		// stock symbol
		if (symbol == null || symbol.trim().isEmpty()) {
			throw new RuntimeException("Stock symbol is empty.");
		}
		symbol = symbol.trim();

		// start date
		if (startDate == null) {
			startDate = Calendar.getInstance();
			startDate.set(1900, 0, 1);
		}
		int startYear = startDate.get(Calendar.YEAR);
		int startMonth = startDate.get(Calendar.MONTH);
		int startDay = startDate.get(Calendar.DATE);

		// end date
		if (endDate == null) {
			endDate = Calendar.getInstance();
			endDate.setTime(new Date());
		}
		int endYear = endDate.get(Calendar.YEAR);
		int endMonth = endDate.get(Calendar.MONTH);
		int endDay = endDate.get(Calendar.DATE);

		// type
		type = checkType(type);

		// ignore
		if (ignore == null || ignore.trim().isEmpty()) {
			ignore = IGNORE_CSV;
		}

		List<YahooFinanceStockData> stockDataList = new ArrayList<YahooFinanceStockData>();
		Response response = null;
		try {
			WebTarget target = getRootPath();

			// stock symbol
			target = target.queryParam("s", symbol);

			// start date
			target = target.queryParam("a", startMonth);
			target = target.queryParam("b", startDay);
			target = target.queryParam("c", startYear);

			// end date
			target = target.queryParam("d", endMonth);
			target = target.queryParam("e", endDay);
			target = target.queryParam("f", endYear);

			// type
			target = target.queryParam("g", type);

			// ignore
			target = target.queryParam("ignore", ignore);

			URI uri = target.getUri();
			if (this.debug) {
				try {
					System.out.println("    uri = " + uri.toURL().toExternalForm());
					System.out.println();
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(response);

			String responseString = response.readEntity(String.class);

			// if (this.debug) {
			// System.out.println("responseString = " + responseString);
			// }
			// stockDataList = response.readEntity(new GenericType<List<YahooFinanceStockData>>() {
			// });

			if (responseString != null) {
				String[] lines = responseString.split(NEW_LINE);
				if (this.debug) {
					System.out.println("lines.length = " + lines.length);
				}

				int i = 0;
				for (String line : lines) {
					if (this.debug) {
						System.out.println("line[" + i + "] = " + line);
					}

					// skip the first line, which are column names.
					if (line.contains("Date,")) {
						i++;
						continue;
					}

					YahooFinanceStockData stockData = convertStringToStockData(line);
					if (stockData != null) {
						stockDataList.add(stockData);
					}
					i++;
				}
			}

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ClientUtil.closeQuietly(response, true);
		}

		if (comparator != null) {
			Collections.sort(stockDataList, comparator);
		}

		return stockDataList;
	}

	protected String checkType(String type) {
		if (!TYPE_DAILY.equals(type) && !TYPE_WEEKLY.equals(type) && !TYPE_YEARLY.equals(type)) {
			return TYPE_DAILY;
		}
		return type;
	}

	protected String checkIgnore(String ignore) {
		if (ignore == null || ignore.trim().isEmpty()) {
			return IGNORE_CSV;
		}
		return ignore;
	}

	/**
	 * Convert a line of string to a YahooFinanceStockData object.
	 * 
	 * Example:
	 * 
	 * line[0] = Date,Open,High,Low,Close,Volume,Adj Close
	 * 
	 * line[1] = 2016-12-27,22.709999,22.74,22.540001,22.610001,39891000,22.610001
	 * 
	 * @param line
	 * @return
	 */
	protected YahooFinanceStockData convertStringToStockData(String line) {
		YahooFinanceStockData stockData = null;

		if (line != null) {
			String[] segments = line.split(",");
			if (segments == null || segments.length < 7) {
				System.err.println("Invalid line: " + line);
				return null;
			}

			String dateStr = segments[0];
			String openStr = segments[1];
			String highStr = segments[2];
			String lowStr = segments[3];
			String closeStr = segments[4];
			String volumeStr = segments[5];
			String adjCloseStr = segments[6];

			try {
				Date date = DateUtil.toDate(dateStr, DateUtil.YEAR_MONTH_DAY_FORMAT1);
				Double open = Double.parseDouble(openStr);
				Double high = Double.parseDouble(highStr);
				Double low = Double.parseDouble(lowStr);
				Double close = Double.parseDouble(closeStr);
				Double adjClose = Double.parseDouble(adjCloseStr);
				Long volume = Long.parseLong(volumeStr);

				stockData = new YahooFinanceStockData();
				stockData.setDate(date);
				stockData.setOpen(open);
				stockData.setHigh(high);
				stockData.setLow(low);
				stockData.setClose(close);
				stockData.setAdjClose(adjClose);
				stockData.setVolume(volume);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Invalid line: " + line);
				return null;
			}
		}

		return stockData;
	}

}
