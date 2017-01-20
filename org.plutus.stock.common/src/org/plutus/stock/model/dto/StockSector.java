package org.plutus.stock.model.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StockSector {

	protected String sector;
	protected List<String> industries = new ArrayList<String>();

	/**
	 * 
	 */
	public StockSector() {
	}

	/**
	 * 
	 * @param sector
	 */
	public StockSector(String sector) {
		this.sector = sector;
	}

	/**
	 * 
	 * @param sector
	 * @param industries
	 */
	public StockSector(String sector, List<String> industries) {
		this.sector = sector;
		this.industries = industries;
		if (this.industries == null) {
			this.industries = new ArrayList<String>();
		}
	}

	public String getSector() {
		return this.sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public List<String> getIndustries() {
		return this.industries;
	}

	public boolean containsIndustry(String industry) {
		return getIndustries().contains(industry);
	}

	public int industrySize() {
		return getIndustries().size();
	}

	public boolean isIndustryEmpty() {
		return getIndustries().isEmpty();
	}

	public void sortIndustries(Comparator<String> comparator) {
		Collections.sort(getIndustries(), comparator);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		StockSector other = (StockSector) obj;
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

		sb.append("StockSector [\n");
		sb.append("  sector=").append(this.sector).append(",\n");
		sb.append("  industries.size()=").append(this.industries.size()).append(",\n");
		sb.append("  industries=[\n");
		for (String industry : this.industries) {
			sb.append("    ").append(industry).append(",\n");
		}
		sb.append("  ]\n");
		sb.append("]");

		return sb.toString();
	}

}
