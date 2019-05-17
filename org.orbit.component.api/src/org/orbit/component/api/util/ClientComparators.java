package org.orbit.component.api.util;

import java.util.Collections;
import java.util.List;

import org.orbit.component.api.tier3.domain.PlatformConfig;
import org.orbit.component.api.tier3.nodecontrol.NodeInfo;
import org.origin.common.util.BaseComparator;
import org.origin.common.util.NameUtil;

public class ClientComparators {

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

			return NameUtil.compareStringWithSuffixNum(hostURL1, hostURL2, asc());
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

			return NameUtil.compareStringWithSuffixNum(id1, id2, asc());
		}
	}

}
