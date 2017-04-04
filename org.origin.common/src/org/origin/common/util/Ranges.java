package org.origin.common.util;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 * @param <E>
 */
public class Ranges<E extends Comparable<E>> {

	protected boolean isOverlapAllowed;
	protected List<Range<E>> ranges = new ArrayList<Range<E>>();

	/**
	 * 
	 * @return
	 */
	public boolean isOverlapAllowed() {
		return isOverlapAllowed;
	}

	/**
	 * 
	 * @param isOverlapAllowed
	 */
	public void setOverlapAllowed(boolean isOverlapAllowed) {
		this.isOverlapAllowed = isOverlapAllowed;
	}

	/**
	 * Get ranges.
	 * 
	 * @return
	 */
	public List<Range<E>> getRanges() {
		return this.ranges;
	}

	/**
	 * Add a range.
	 * 
	 * @param range
	 * @return
	 */
	public boolean add(Range<E> range) {
		if (range == null) {
			return false;
		}

		if (!isOverlapAllowed) {
			for (Range<E> currRange : this.ranges) {
				if (currRange.overlap(range)) {
					throw new RuntimeException(MessageFormat.format("Overlapping ranges are not allowed. Range ''{0}'' overlaps with existing range ''{1}''.", new Object[] { range, currRange }));
				}
			}
		}

		if (!this.ranges.contains(range)) {
			return this.ranges.add(range);
		}
		return false;
	}

	/**
	 * Remove a range.
	 * 
	 * @param range
	 * @return
	 */
	public boolean remove(Range<E> range) {
		if (range == null) {
			return false;
		}
		if (this.ranges.contains(range)) {
			return this.ranges.remove(range);
		}
		return false;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public boolean containsValue(E e) {
		for (Range<E> range : this.ranges) {
			if (range.containsValue(e)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public Range<E> findRange(E e) {
		for (Range<E> currRange : this.ranges) {
			if (currRange.containsValue(e)) {
				return currRange;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param e
	 * @return
	 */
	public List<Range<E>> findRanges(E e) {
		List<Range<E>> matchedRanges = new ArrayList<Range<E>>();
		for (Range<E> currRange : this.ranges) {
			if (currRange.containsValue(e)) {
				matchedRanges.add(currRange);
			}
		}
		return matchedRanges;
	}

	@Override
	public String toString() {
		String str = "";

		int i = 0;
		for (Range<E> currRange : this.ranges) {
			String rangeStr = currRange.toString();

			if (i > 0) {
				str += " ";
			}

			str += rangeStr;
			i++;
		}

		return str;
	}

}
