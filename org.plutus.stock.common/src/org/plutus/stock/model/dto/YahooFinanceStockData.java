package org.plutus.stock.model.dto;

import java.util.Date;

import org.origin.common.util.DateUtil;

/**
 * Columns:
 * 
 * Date,Open,High,Low,Close,Volume,Adj Close
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class YahooFinanceStockData {

	protected Date date;
	protected double open;
	protected double high;
	protected double low;
	protected double close;
	protected double adjClose;
	protected long volume;

	/**
	 * 
	 */
	public YahooFinanceStockData() {
	}

	/**
	 * 
	 * @param date
	 * @param open
	 * @param high
	 * @param low
	 * @param close
	 * @param adjClose
	 * @param volume
	 */
	public YahooFinanceStockData(Date date, double open, double high, double low, double close, double adjClose, long volume) {
		this.date = date;
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.adjClose = adjClose;
		this.volume = volume;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public double getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(double adjClose) {
		this.adjClose = adjClose;
	}

	public long getVolume() {
		return volume;
	}

	public void setVolume(long volume) {
		this.volume = volume;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String dateStr = DateUtil.toString(this.date, DateUtil.YEAR_MONTH_DAY_FORMAT1);

		sb.append("YahooFinanceStockData [");
		sb.append("date=").append(dateStr);
		sb.append(", open=").append(this.open);
		sb.append(", high=").append(this.high);
		sb.append(", low=").append(this.low);
		sb.append(", close=").append(this.close);
		sb.append(", adjClose=").append(this.adjClose);
		sb.append(", volume=").append(this.volume);
		sb.append("]");

		return sb.toString();
	}

}
