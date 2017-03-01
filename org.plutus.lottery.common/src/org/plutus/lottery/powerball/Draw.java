package org.plutus.lottery.powerball;

import java.util.Date;

import org.origin.common.util.DateUtil;

public class Draw {

	protected Date date;
	protected int num1;
	protected int num2;
	protected int num3;
	protected int num4;
	protected int num5;
	protected int pb;

	protected DrawStat stat;

	/**
	 * 
	 */
	public Draw() {
	}

	/**
	 * 
	 * @param date
	 * @param num1
	 * @param num2
	 * @param num3
	 * @param num4
	 * @param num5
	 * @param red
	 */
	public Draw(Date date, int num1, int num2, int num3, int num4, int num5, int pb) {
		this.date = date;
		this.num1 = num1;
		this.num2 = num2;
		this.num3 = num3;
		this.num4 = num4;
		this.num5 = num5;
		this.pb = pb;
	}

	public Date getDate() {
		return date;
	}

	public String getDateString() {
		String dateStr = this.date != null ? DateUtil.toString(this.date, DateUtil.MONTH_DAY_YEAR_FORMAT1) : "null";
		return dateStr;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getNumsString(boolean includePb) {
		if (includePb) {
			return addSpace(num1) + num1 + ", " + addSpace(num2) + num2 + ", " + addSpace(num3) + num3 + ", " + addSpace(num4) + num4 + ", " + addSpace(num5) + num5 + "  " + addSpace(pb) + pb;
		} else {
			return addSpace(num1) + num1 + ", " + addSpace(num2) + num2 + ", " + addSpace(num3) + num3 + ", " + addSpace(num4) + num4 + ", " + addSpace(num5) + num5;
		}
	}

	protected String addSpace(int num) {
		return num < 10 ? " " : "";
	}

	public int getNum1() {
		return num1;
	}

	public void setNum1(int num1) {
		this.num1 = num1;
	}

	public int getNum2() {
		return num2;
	}

	public void setNum2(int num2) {
		this.num2 = num2;
	}

	public int getNum3() {
		return num3;
	}

	public void setNum3(int num3) {
		this.num3 = num3;
	}

	public int getNum4() {
		return num4;
	}

	public void setNum4(int num4) {
		this.num4 = num4;
	}

	public int getNum5() {
		return num5;
	}

	public void setNum5(int num5) {
		this.num5 = num5;
	}

	public int getPB() {
		return pb;
	}

	public void setPB(int pb) {
		this.pb = pb;
	}

	public synchronized DrawStat getStat() {
		if (this.stat == null) {
			this.stat = new DrawStat();
		}
		return this.stat;
	}

	public synchronized void setStat(DrawStat stat) {
		this.stat = stat;
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
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		Draw other = (Draw) obj;
		if (date == null) {
			if (other.date != null) {
				return false;
			}
		} else if (!date.equals(other.date)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		String dateStr = getDateString();
		return "Draw [date=" + dateStr + ", num1=" + num1 + ", num2=" + num2 + ", num3=" + num3 + ", num4=" + num4 + ", num5=" + num5 + ", pb=" + pb + "]";
	}

}
