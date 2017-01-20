package org.plutus.stock.model.runtime;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import org.origin.common.resource.impl.RObjectImpl;
import org.origin.common.util.DateUtil;

public class StockStat extends RObjectImpl {

	protected Date date;
	protected BigDecimal marketCap;
	protected BigDecimal adjMarketCap;

	/**
	 * 
	 */
	public StockStat() {
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getMarketCap() {
		return marketCap;
	}

	public void setMarketCap(BigDecimal marketCap) {
		this.marketCap = marketCap;
	}

	public void setMarketCap(double marketCap) {
		BigDecimal value = BigDecimal.valueOf(marketCap);
		value = value.setScale(2, RoundingMode.CEILING);
		this.marketCap = value;
	}

	public BigDecimal getAdjMarketCap() {
		return adjMarketCap;
	}

	public void setAdjMarketCap(BigDecimal adjMarketCap) {
		this.adjMarketCap = adjMarketCap;
	}

	public void setAdjMarketCap(double adjMarketCap) {
		BigDecimal value = BigDecimal.valueOf(adjMarketCap);
		value = value.setScale(2, RoundingMode.CEILING);
		this.adjMarketCap = value;
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
		StockStat other = (StockStat) obj;
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

		sb.append("StockStat [");
		sb.append("date=").append(dateStr);
		sb.append(", marketCap=").append(this.marketCap != null ? this.marketCap.toPlainString() : "null");
		sb.append(", adjMarketCap=").append(this.adjMarketCap != null ? this.adjMarketCap.toPlainString() : "null");
		sb.append("]");

		return sb.toString();
	}

}
