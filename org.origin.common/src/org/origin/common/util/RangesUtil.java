package org.origin.common.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RangesUtil {

	/**
	 * 
	 * @param ranges
	 * @param values
	 * @return
	 */
	public static Map<Range<Integer>, Integer> getRangeCountMap(Ranges<Integer> ranges, List<Integer> values) {
		Map<Range<Integer>, Integer> rangeCountMap = new TreeMap<Range<Integer>, Integer>();

		for (Integer value : values) {
			Range<Integer> currRange = ranges.findRange(value);

			Integer currCount = rangeCountMap.get(currRange);
			if (currCount == null) {
				currCount = new Integer(1);
			} else {
				currCount = new Integer(currCount.intValue() + 1);
			}
			rangeCountMap.put(currRange, currCount);
		}

		return rangeCountMap;
	}

}
