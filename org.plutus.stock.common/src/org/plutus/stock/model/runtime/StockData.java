package org.plutus.stock.model.runtime;

import java.math.BigDecimal;
import java.util.Date;

import org.origin.common.resource.impl.RObjectImpl;
import org.origin.common.util.DateUtil;

public class StockData extends RObjectImpl {

	protected Date date;
	protected BigDecimal open;
	protected BigDecimal high;
	protected BigDecimal low;
	protected BigDecimal close;
	protected BigDecimal adjClose;
	protected BigDecimal volume;

	/**
	 * 
	 */
	public StockData() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getOpen() {
		return open;
	}

	public void setOpen(BigDecimal open) {
		this.open = open;
	}

	public BigDecimal getHigh() {
		return high;
	}

	public void setHigh(BigDecimal high) {
		this.high = high;
	}

	public BigDecimal getLow() {
		return low;
	}

	public void setLow(BigDecimal low) {
		this.low = low;
	}

	public BigDecimal getClose() {
		return close;
	}

	public void setClose(BigDecimal close) {
		this.close = close;
	}

	public BigDecimal getAdjClose() {
		return adjClose;
	}

	public void setAdjClose(BigDecimal adjClose) {
		this.adjClose = adjClose;
	}

	public BigDecimal getVolume() {
		return volume;
	}

	public void setVolume(BigDecimal volume) {
		this.volume = volume;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
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
		StockData other = (StockData) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		String dateStr = DateUtil.toString(this.date, DateUtil.YEAR_MONTH_DAY_FORMAT1);

		sb.append("StockData [");
		sb.append("date=").append(dateStr);
		sb.append(", open=").append(this.open != null ? this.open.toPlainString() : "null");
		sb.append(", high=").append(this.high != null ? this.high.toPlainString() : "null");
		sb.append(", low=").append(this.low != null ? this.low.toPlainString() : "null");
		sb.append(", close=").append(this.close != null ? this.close.toPlainString() : "null");
		sb.append(", adjClose=").append(this.adjClose != null ? this.adjClose.toPlainString() : "null");
		sb.append(", volume=").append(this.volume != null ? this.volume.toPlainString() : "null");
		sb.append("]");

		return sb.toString();
	}

}
