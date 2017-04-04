package org.plutus.lottery.powerball.report;

public class NumberReport {

	protected Integer number;
	protected Integer count;

	public NumberReport(Integer number) {
		this.number = number;
	}

	public Integer getNumber() {
		return this.number;
	}

	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((number == null) ? 0 : number.hashCode());
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
		NumberReport other = (NumberReport) obj;
		if (number == null) {
			if (other.number != null)
				return false;
		} else if (!number.equals(other.number))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "NumberReport [number=" + number + ", count=" + count + "]";
	}

}
