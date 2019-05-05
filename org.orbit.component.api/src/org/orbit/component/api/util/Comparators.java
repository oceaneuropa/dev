package org.orbit.component.api.util;

import java.util.Collections;
import java.util.List;

import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.origin.common.util.BaseComparator;

public class Comparators {

	public static PlatformConfigHostURLComparator PlatformConfigHostURLComparator_ASC = new PlatformConfigHostURLComparator(BaseComparator.SORT_ASC);
	public static PlatformConfigHostURLComparator PlatformConfigHostURLComparator_DESC = new PlatformConfigHostURLComparator(BaseComparator.SORT_DESC);

	public static NodeInfoIdComparator NodeInfoIdComparator_ASC = new NodeInfoIdComparator(BaseComparator.SORT_ASC);
	public static NodeInfoIdComparator NodeInfoIdComparator_DESC = new NodeInfoIdComparator(BaseComparator.SORT_DESC);

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

	public static class NodeInfoIdComparator extends BaseComparator<NodeInfo> {

		public NodeInfoIdComparator(String sort) {
			super(sort);
		}

		@Override
		public int compare(NodeInfo node1, NodeInfo node2) {
			String id1 = node1.getId();
			String id2 = node2.getId();
			if (id1 == null) {
				id1 = "";
			}
			if (id2 == null) {
				id2 = "";
			}

			Object[] result1 = getBaseNameAndSuffixNum(id1);
			Object[] result2 = getBaseNameAndSuffixNum(id2);

			id1 = (String) result1[0];
			int suffixNum1 = (int) result1[1];

			id2 = (String) result2[0];
			int suffixNum2 = (int) result2[1];

			if (desc()) {
				if (id1.equals(id2)) {
					return suffixNum2 - suffixNum1;
				} else {
					return id2.compareTo(id1);
				}

			} else {
				if (id1.equals(id2)) {
					return suffixNum1 - suffixNum2;
				} else {
					return id1.compareTo(id2);
				}
			}
		}

		protected Object[] getBaseNameAndSuffixNum(String name) {
			String baseName = name;
			int suffixNum = -1;

			int endingDigitIndex = -1;
			int charIndex = name.length() - 1;
			while (charIndex >= 0) {
				char currChar = name.charAt(charIndex);
				if (!Character.isDigit(currChar)) {
					break;
				} else {
					endingDigitIndex = charIndex;
					charIndex--;
				}
			}
			if (endingDigitIndex >= 0) {
				if (endingDigitIndex == 0) {
					baseName = "";
				} else {
					baseName = name.substring(0, endingDigitIndex);
				}
				String digitStr = name.substring(endingDigitIndex);
				suffixNum = Integer.parseInt(digitStr);
			}
			return new Object[] { baseName, suffixNum };
		}
	}

}
