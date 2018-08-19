package org.plutus.stock.common.client;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.origin.common.rest.client.WSClient;
import org.origin.common.rest.client.WSClientConfiguration;
import org.origin.common.rest.client.ClientException;
import org.origin.common.rest.util.ResponseUtil;
import org.plutus.stock.model.dto.StockCompany;

/**
 * nasdaq.com web service client to retrieve stock company data.
 * 
 * URL: http://www.nasdaq.com/screening/company-list.aspx
 *
 */
public class NasdaqComClient extends WSClient {

	// stock exchange constants
	public static final String EXCHANGE_NASDAQ = "NASDAQ";
	public static final String EXCHANGE_NYSE = "NYSE";
	public static final String EXCHANGE_AMEX = "AMEX";

	// double quotes constant
	protected static final String DOUBLE_QUOTES = "\"";
	protected static final String NEW_LINE = "\n";
	protected static final String RETURN = "\r";

	// log
	protected boolean info = true;
	protected boolean debug = false;

	/**
	 * 
	 */
	public NasdaqComClient() {
		super(WSClientConfiguration.create("orbit", null, "http://www.nasdaq.com:80", "screening/companies-by-industry.aspx"));
	}

	/**
	 * 
	 * @param config
	 */
	public NasdaqComClient(WSClientConfiguration config) {
		super(config);
	}

	/**
	 * Get stock company list.
	 * 
	 * @param exchange
	 * @param comparator
	 * @return
	 * @throws ClientException
	 */
	public List<StockCompany> getStockCompanies(String exchange, Comparator<StockCompany> comparator) throws ClientException {
		return getStockCompanies(exchange, false, false, null, null, comparator);
	}

	/**
	 * Get stock company list.
	 * 
	 * Example URLs:
	 * 
	 * http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=NASDAQ&render=download
	 * 
	 * http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=NYSE&render=download
	 * 
	 * http://www.nasdaq.com/screening/companies-by-industry.aspx?exchange=AMEX&render=download
	 * 
	 * @param exchange
	 * @param searchBySector
	 * @param searchBySectorAndIndustry
	 * @param sector
	 * @param industry
	 * @param comparator
	 * @return
	 * @throws ClientException
	 */
	public List<StockCompany> getStockCompanies(String exchange, boolean searchBySector, boolean searchBySectorAndIndustry, String sector, String industry, Comparator<StockCompany> comparator) throws ClientException {
		if (this.info) {
			System.out.println("getStockCompanies()");
			System.out.println("    exchange: " + exchange);
		}

		// type
		exchange = checkExchange(exchange);

		List<StockCompany> stockCompanies = new ArrayList<StockCompany>();
		Response response = null;
		try {
			WebTarget target = getRootPath();

			// exchange
			target = target.queryParam("exchange", exchange);

			// render
			target = target.queryParam("render", "download");

			URI uri = target.getUri();
			if (this.debug) {
				try {
					System.out.println("uri = " + uri.toURL().toExternalForm());
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}

			Builder builder = target.request(MediaType.APPLICATION_JSON);
			response = updateHeaders(builder).get();
			checkResponse(target, response);

			String responseString = response.readEntity(String.class);

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
					if (line.contains("\"Symbol\",")) {
						i++;
						continue;
					}

					StockCompany stockCompany = convertStringToStockCompany(line);
					if (stockCompany != null) {
						if (searchBySector) {
							boolean matchSector = false;
							if (sector == null) {
								if (stockCompany.getSector() == null) {
									matchSector = true;
								}
							} else {
								if (sector.equals(stockCompany.getSector())) {
									matchSector = true;
								}
							}
							if (matchSector) {
								stockCompanies.add(stockCompany);
							}

						} else if (searchBySectorAndIndustry) {
							boolean matchSector = false;
							boolean matchIndustry = false;

							if (sector == null) {
								if (stockCompany.getSector() == null) {
									matchSector = true;
								}
							} else {
								if (sector.equals(stockCompany.getSector())) {
									matchSector = true;
								}
							}

							if (industry == null) {
								if (stockCompany.getIndustry() == null) {
									matchIndustry = true;
								}
							} else {
								if (industry.equals(stockCompany.getIndustry())) {
									matchIndustry = true;
								}
							}

							if (matchSector && matchIndustry) {
								stockCompanies.add(stockCompany);
							}

						} else {
							stockCompanies.add(stockCompany);
						}
					}
					i++;
				}
			}

		} catch (ClientException e) {
			handleException(e);
		} finally {
			ResponseUtil.closeQuietly(response, true);
		}

		if (comparator != null) {
			Collections.sort(stockCompanies, comparator);
		}

		return stockCompanies;
	}

	/**
	 * 
	 * @param exchange
	 * @return
	 */
	protected String checkExchange(String exchange) {
		if (!EXCHANGE_NASDAQ.equals(exchange) && !EXCHANGE_NYSE.equals(exchange) && !EXCHANGE_AMEX.equals(exchange)) {
			return EXCHANGE_NASDAQ;
		}
		return exchange;
	}

	/**
	 * Convert a line of string to a StockCompany object.
	 * 
	 * Example:
	 * 
	 * line[0] = "Symbol","Name","LastSale","MarketCap","ADR TSO","IPOyear","Sector","Industry","Summary Quote",
	 * 
	 * line[0] = "PIH","1347 Property Insurance Holdings, Inc.","7.25","43201778.5","n/a","2014","Finance","Property-Casualty Insurers","http://www.nasdaq.com/symbol/pih",
	 * 
	 * line[1] = "FLWS","1-800 FLOWERS.COM, Inc.","10.7","698514040.2","n/a","1999","Consumer Services","Other Specialty Stores","http://www.nasdaq.com/symbol/flws",
	 * 
	 * line[2] = "FCCY","1st Constitution Bancorp (NJ)","18","143699292","n/a","n/a","Finance","Savings Institutions","http://www.nasdaq.com/symbol/fccy",
	 * 
	 * line[3] = "SRCE","1st Source Corporation","45.33","1172947838.16","n/a","n/a","Finance","Major Banks","http://www.nasdaq.com/symbol/srce",
	 * 
	 * line[4] = "VNET","21Vianet Group, Inc.","6.96","401939707.68","57749958","2011","Technology","Computer Software: Programming, Data Processing","http://www.nasdaq.com/symbol/vnet",
	 * 
	 * line[5] = "TWOU","2U, Inc.","29.77","1400994389.47","n/a","2014","Technology","Computer Software: Prepackaged Software","http://www.nasdaq.com/symbol/twou",
	 * 
	 * line[6] = "JOBS","51job, Inc.","33.7","902936602.7","26793371","2004","Technology","Diversified Commercial Services","http://www.nasdaq.com/symbol/jobs",
	 * 
	 * @param line
	 * @return
	 */
	protected StockCompany convertStringToStockCompany(String line) {
		StockCompany stockCompany = null;

		if (line != null) {
			String[] segments = line.split("\",\"");
			if (segments == null || segments.length < 9) {
				System.err.println("Invalid line: " + line);
				return null;
			}

			String symbolStr = segments[0];
			String nameStr = segments[1];
			String lastSaleStr = segments[2];
			String marketCapStr = segments[3];
			String adrTsoStr = segments[4];
			String ipoYearStr = segments[5];
			String sectorStr = segments[6];
			String industryStr = segments[7];
			String summaryQuoteStr = segments[8];

			symbolStr = removeWrappingDoubleQuotes(symbolStr);
			nameStr = removeWrappingDoubleQuotes(nameStr);
			lastSaleStr = removeWrappingDoubleQuotes(lastSaleStr);
			marketCapStr = removeWrappingDoubleQuotes(marketCapStr);
			adrTsoStr = removeWrappingDoubleQuotes(adrTsoStr);
			ipoYearStr = removeWrappingDoubleQuotes(ipoYearStr);
			sectorStr = removeWrappingDoubleQuotes(sectorStr);
			industryStr = removeWrappingDoubleQuotes(industryStr);
			summaryQuoteStr = removeWrappingDoubleQuotes(summaryQuoteStr);

			try {
				String symbol = symbolStr != null ? symbolStr.trim() : "";
				String name = nameStr != null ? nameStr.trim() : "";

				Double lastSale = 0.0;
				try {
					if ("".equalsIgnoreCase(lastSaleStr)) {
						lastSale = 0.0;
					} else if ("n/a".equalsIgnoreCase(lastSaleStr)) {
						lastSale = -1.0;
					} else {
						lastSale = Double.parseDouble(lastSaleStr);
					}
				} catch (Exception e) {
					lastSale = -2.0;
				}

				Double marketCap = 0.0;
				try {
					marketCapStr = marketCapStr.trim();
					if ("".equalsIgnoreCase(marketCapStr)) {
						marketCap = 0.0;
					} else if ("n/a".equalsIgnoreCase(marketCapStr)) {
						marketCap = -1.0;
					} else {
						marketCap = Double.parseDouble(marketCapStr);
					}
				} catch (Exception e) {
					marketCap = -2.0;
				}

				String adrTSO = adrTsoStr != null ? adrTsoStr.trim() : "";

				int ipoYear = 0;
				try {
					ipoYearStr = ipoYearStr.trim();
					if ("".equalsIgnoreCase(ipoYearStr)) {
						ipoYear = 0;
					} else if ("n/a".equalsIgnoreCase(ipoYearStr)) {
						ipoYear = -1;
					} else {
						ipoYear = Integer.parseInt(ipoYearStr);
					}
				} catch (Exception e) {
					ipoYear = -2;
				}

				String sector = sectorStr != null ? sectorStr.trim() : "";
				String industry = industryStr != null ? industryStr.trim() : "";
				String summaryQuote = summaryQuoteStr != null ? summaryQuoteStr.trim() : "";

				stockCompany = new StockCompany();
				stockCompany.setSymbol(symbol);
				stockCompany.setName(name);
				stockCompany.setLastSale(lastSale);
				stockCompany.setMarketCap(marketCap);
				stockCompany.setAdrTSO(adrTSO);
				stockCompany.setIpoYear(ipoYear);
				stockCompany.setSector(sector);
				stockCompany.setIndustry(industry);
				stockCompany.setSummaryQuote(summaryQuote);

			} catch (Exception e) {
				e.printStackTrace();
				System.err.println("Invalid line: " + line);
				return null;
			}
		}

		return stockCompany;
	}

	/**
	 * 
	 * @param segment
	 * @return
	 */
	protected String removeWrappingDoubleQuotes(String segment) {
		if (segment != null) {
			// remove ending "\r"
			if (segment.endsWith(RETURN)) {
				if (segment.equals(RETURN)) {
					segment = "";
				} else {
					segment = segment.substring(0, segment.length() - 1);
				}
			}

			// remove "," from starting ",\""
			if (segment.startsWith("," + DOUBLE_QUOTES)) {
				segment = segment.substring(1);
			}

			// remove "," from ending "\","
			if (segment.endsWith(DOUBLE_QUOTES + ",")) {
				segment = segment.substring(0, segment.length() - 2);
			}

			// remove starting "\""
			if (segment.startsWith(DOUBLE_QUOTES)) {
				if (segment.equals(DOUBLE_QUOTES)) {
					segment = "";
				} else {
					segment = segment.substring(1);
				}
			}

			// remove ending "\""
			if (segment.endsWith(DOUBLE_QUOTES)) {
				if (segment.equals(DOUBLE_QUOTES)) {
					segment = "";
				} else {
					segment = segment.substring(0, segment.length() - 1);
				}
			}
		}
		return segment;
	}

}
