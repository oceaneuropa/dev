package org.orbit.component.api.util;

import java.util.Collections;
import java.util.List;

import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.origin.common.util.BaseComparator;

public class Comparators {

	public static PlatformConfigHostURLComparator PlatformConfigHostURLComparator_ASC = new PlatformConfigHostURLComparator(BaseComparator.SORT_ASC);
	public static PlatformConfigHostURLComparator PlatformConfigHostURLComparator_DESC = new PlatformConfigHostURLComparator(BaseComparator.SORT_DESC);

	/**
	 * 
	 * @param list
	 * @param comparator
	 */
	public static void sort(List<PlatformConfig> list, PlatformConfigHostURLComparator comparator) {
		if (list != null && list.size() > 1 && comparator != null) {
			Collections.sort(list, comparator);
		}
	}

	public static class PlatformConfigHostURLComparator extends BaseComparator<PlatformConfig> {

		public PlatformConfigHostURLComparator(String sort) {
			super(sort);
		}

		@Override
		public int compare(PlatformConfig config1, PlatformConfig config2) {
			String hostURL1 = config1.getHostURL();
			String hostURL2 = config2.getHostURL();
			if (hostURL1 == null) {
				hostURL1 = "";
			}
			if (hostURL2 == null) {
				hostURL2 = "";
			}
			if (desc()) {
				return hostURL2.compareTo(hostURL1);
			} else {
				return hostURL1.compareTo(hostURL2);
			}
		}
	}

}
