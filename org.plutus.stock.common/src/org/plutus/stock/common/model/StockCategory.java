package org.plutus.stock.common.model;

public class StockCategory {

	protected String sector;
	protected String industry;

	/**
	 * 
	 */
	public StockCategory() {
	}

	/**
	 * 
	 * @param sector
	 * @param industry
	 */
	public StockCategory(String sector, String industry) {
		this.sector = sector;
		this.industry = industry;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((industry == null) ? 0 : industry.hashCode());
		result = prime * result + ((sector == null) ? 0 : sector.hashCode());
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
		StockCategory other = (StockCategory) obj;
		if (industry == null) {
			if (other.industry != null)
				return false;
		} else if (!industry.equals(other.industry))
			return false;
		if (sector == null) {
			if (other.sector != null)
				return false;
		} else if (!sector.equals(other.sector))
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		sb.append("StockCategory [");
		sb.append("sector=").append(this.sector);
		sb.append(", industry=").append(this.industry);
		sb.append("]");

		return sb.toString();
	}

}
