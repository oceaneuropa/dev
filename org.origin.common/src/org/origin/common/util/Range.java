package org.origin.common.util;

import java.text.MessageFormat;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <E>
 */
public class Range<E extends Comparable<E>> implements Comparable<Range<E>> {

	protected E from;
	protected boolean isFromIncluded;
	protected boolean isFromUnlimited; // takes effect only when "from" is null

	protected E to;
	protected boolean isToIncluded;
	protected boolean isToUnlimited; // takes effect only when "to" is null

	public Range() {
	}

	/**
	 * 
	 * @param from
	 * @param isFromIncluded
	 * @param isFromUnlimited
	 * @param to
	 * @param isToIncluded
	 * @param isToUnlimited
	 */
	public Range(E from, boolean isFromIncluded, boolean isFromUnlimited, E to, boolean isToIncluded, boolean isToUnlimited) {
		this.from = from;
		this.isFromIncluded = isFromIncluded;
		this.isFromUnlimited = isFromUnlimited;
		this.to = to;
		this.isToIncluded = isToIncluded;
		this.isToUnlimited = isToUnlimited;
		checkRangeValue();
	}

	protected void checkRangeValue() {
		if (this.from != null && this.to != null) {
			int result = this.from.compareTo(to);

			if (result > 0) {
				throw new RuntimeException(MessageFormat.format("Invalid range. The from value [{0}] cannot be larger than the to value [{1}].", new Object[] { this.from, this.to }));

			} else if (result == 0) {
				if (!this.isFromIncluded || !this.isToIncluded) {
					throw new RuntimeException(MessageFormat.format("Invalid range. The from value [{0}] and the to value [{1}] can be same only when both are including range.", new Object[] { this.from, this.to }));
				}
			}
		}
	}

	// --------------------------------------------------------------------------------
	// build
	// --------------------------------------------------------------------------------
	public Range<E> from(E from, boolean include) {
		setFrom(from, include);
		return this;
	}

	public Range<E> from(boolean isFromUnlimited) {
		setFromUnlimited(isFromUnlimited);
		return this;
	}

	public Range<E> to(E to, boolean include) {
		setTo(to, include);
		return this;
	}

	public Range<E> to(boolean isToUnlimited) {
		setToUnlimited(isToUnlimited);
		return this;
	}

	// --------------------------------------------------------------------------------
	// set/get
	// --------------------------------------------------------------------------------
	public E getFrom() {
		return from;
	}

	public void setFrom(E from, boolean include) {
		this.from = from;
		this.isFromIncluded = include;
		checkRangeValue();
	}

	public boolean isFromIncluded() {
		return this.isFromIncluded;
	}

	public void setFromIncluded(boolean isFromIncluded) {
		this.isFromIncluded = isFromIncluded;
	}

	public boolean isFromUnlimited() {
		return this.isFromUnlimited;
	}

	public void setFromUnlimited(boolean isFromUnlimited) {
		this.isFromUnlimited = isFromUnlimited;
	}

	public E getTo() {
		return to;
	}

	public void setTo(E to, boolean include) {
		this.to = to;
		this.isToIncluded = include;
		checkRangeValue();
	}

	public boolean isToIncluded() {
		return this.isToIncluded;
	}

	public void setToIncluded(boolean isToIncluded) {
		this.isToIncluded = isToIncluded;
	}

	public boolean isToUnlimited() {
		return this.isToUnlimited;
	}

	public void setToUnlimited(boolean isToUnlimited) {
		this.isToUnlimited = isToUnlimited;
	}

	// --------------------------------------------------------------------------------
	// overlap/containsValue
	// --------------------------------------------------------------------------------
	/**
	 * Check whether another range overlaps with this range.
	 * 
	 * @param range
	 * @return
	 */
	public boolean overlap(Range<E> range) {
		if (overlap(this, range)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param r1
	 * @param r2
	 * @return
	 */
	private boolean overlap(Range<E> r1, Range<E> r2) {
		if (r1 == null || r2 == null) {
			return false;
		}

		if (r1.getFrom() != null && r2.getTo() != null) {
			int result = r2.getTo().compareTo(r1.getFrom());
			if (result < 0) {
				return false;

			} else if (result == 0) {
				if (!r1.isFromIncluded() || !r2.isToIncluded()) {
					return false;
				}
			}
		}

		if (r1.getTo() != null && r2.getFrom() != null) {
			int result = r1.getTo().compareTo(r2.getFrom());
			if (result < 0) {
				return false;

			} else if (result == 0) {
				if (!r1.isToIncluded() || !r2.isFromIncluded()) {
					return false;
				}
			}
		}

		return true;
	}

	/**
	 * Check whether an element in the range.
	 * 
	 * @param e
	 * @return
	 */
	public boolean containsValue(E e) {
		if (e == null) {
			return false;
		}

		boolean withinFrom = false;
		boolean withinTo = false;

		if (this.from != null) {
			// from is set
			int result = this.from.compareTo(e);
			if (this.isFromIncluded) {
				withinFrom = (result <= 0) ? true : false;
			} else {
				withinFrom = (result < 0) ? true : false;
			}

		} else {
			// from is not set
			if (this.isFromUnlimited) {
				// can from any value
				withinFrom = true;
			} else {
				// the range setting is incomplete.
				withinFrom = false;
			}
		}

		if (this.to != null) {
			// to is set
			int result = e.compareTo(this.to);
			if (this.isToIncluded) {
				withinTo = (result <= 0) ? true : false;
			} else {
				withinTo = (result < 0) ? true : false;
			}

		} else {
			// to is not set
			if (this.isToUnlimited) {
				// can to any value
				withinTo = true;
			} else {
				// the range setting is incomplete.
				withinTo = false;
			}
		}

		return (withinFrom && withinTo) ? true : false;
	}

	// --------------------------------------------------------------------------------
	// compareTo/hashCode/equals/toString
	// --------------------------------------------------------------------------------
	@Override
	public int compareTo(Range<E> other) {
		if (this.isFromUnlimited() && !other.isFromUnlimited()) {
			return -1;
		} else if (!this.isFromUnlimited() && other.isFromUnlimited()) {
			return 1;
		}

		if (this.getFrom() != null && other.getFrom() != null) {
			int compareFrom = this.getFrom().compareTo(other.getFrom());
			if (compareFrom != 0) {
				return compareFrom;
			}
		}

		if (this.isToUnlimited() && !other.isToUnlimited()) {
			return 1;
		} else if (!this.isToUnlimited() && other.isToUnlimited()) {
			return -1;
		}

		if (this.getTo() != null && other.getTo() != null) {
			int compareTo = this.getTo().compareTo(other.getTo());
			if (compareTo != 0) {
				return compareTo;
			}
		}

		return 0;
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Range)) {
			return false;
		}

		Range<?> other = (Range<?>) obj;
		String otherToString = other.toString();
		String thisToString = this.toString();

		return (thisToString.equals(otherToString)) ? true : false;
	}

	@Override
	public String toString() {
		String str = "";

		if (this.from != null) {
			str += this.isFromIncluded ? "[" : "(";
			str += this.from.toString();

		} else {
			if (this.isFromUnlimited) {
				str += "(~";
			} else {
				str += "(n/a";
			}
		}

		str += ", ";

		if (this.to != null) {
			str += this.to.toString();
			str += this.isToIncluded ? "]" : ")";

		} else {
			if (this.isToUnlimited) {
				str += "~)";
			} else {
				str += "n/a)";
			}
		}

		return str;
	}

}
