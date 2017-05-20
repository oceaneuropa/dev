package org.plutus.stock.model.dto;

/**
 * Columns:
 * 
 * "Symbol","Name","LastSale","MarketCap","ADR TSO","IPOyear","Sector","Industry","Summary Quote",
 *
 */
public class StockCompany {

	protected String symbol;
	protected String name;
	protected double lastSale;
	protected double marketCap;
	protected String adrTSO;
	protected int ipoYear;
	protected String sector;
	protected String industry;
	protected String summaryQuote;

	/**
	 * 
	 */
	public StockCompany() {
	}

	/**
	 * 
	 * @param symbol
	 * @param name
	 * @param lastSale
	 * @param marketCap
	 * @param adrTSO
	 * @param ipoYear
	 * @param sector
	 * @param industry
	 * @param summaryQuote
	 */
	public StockCompany(String symbol, String name, double lastSale, double marketCap, String adrTSO, int ipoYear, String sector, String industry, String summaryQuote) {
		this.symbol = symbol;
		this.name = name;
		this.lastSale = lastSale;
		this.marketCap = marketCap;
		this.adrTSO = adrTSO;
		this.ipoYear = ipoYear;
		this.sector = sector;
		this.industry = industry;
		this.summaryQuote = summaryQuote;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLastSale() {
		return lastSale;
	}

	public void setLastSale(double lastSale) {
		this.lastSale = lastSale;
	}

	public double getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(double marketCap) {
		this.marketCap = marketCap;
	}

	public String getAdrTSO() {
		return adrTSO;
	}

	public void setAdrTSO(String adrTSO) {
		this.adrTSO = adrTSO;
	}

	public int getIpoYear() {
		return ipoYear;
	}

	public void setIpoYear(int ipoYear) {
		this.ipoYear = ipoYear;
	}

	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getIndustry() {
		return this.industry;
	}

	public void setIndustry(String industry) {
		this.industry = industry;
	}

	public String getSummaryQuote() {
		return summaryQuote;
	}

	public synchronized void setSummaryQuote(String summaryQuote) {
		this.summaryQuote = summaryQuote;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		StockCompany other = (StockCompany) obj;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("StockCompany [");
		sb.append("symbol=").append(symbol);
		sb.append(", name=").append(this.name);

		if (this.lastSale == 0.0) {
			sb.append(", lastSale=");
		} else if (this.ipoYear == -1.0) {
			sb.append(", lastSale=n/a");
		} else {
			sb.append(", lastSale=").append(this.lastSale);
		}

		if (this.marketCap == 0.0) {
			sb.append(", marketCap=");
		} else if (this.ipoYear == -1.0) {
			sb.append(", marketCap=n/a");
		} else {
			sb.append(", marketCap=").append(this.marketCap);
		}

		sb.append(", adrTSO=").append(this.adrTSO);

		if (this.ipoYear == 0) {
			sb.append(", ipoYear=");
		} else if (this.ipoYear == -1) {
			sb.append(", ipoYear=n/a");
		} else {
			sb.append(", ipoYear=").append(this.ipoYear);
		}

		sb.append(", sector=").append(this.sector);
		sb.append(", industry=").append(this.industry);
		sb.append(", summaryQuote=").append(this.summaryQuote);
		sb.append("]");

		return sb.toString();
	}

}
