package org.orbit.component.io.util;

import java.util.Comparator;

import org.orbit.component.api.tier3.nodecontrol.NodeInfo;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class NodeInfoComparator implements Comparator<Object> {

	protected boolean ASC = true;

	public NodeInfoComparator() {
	}

	public boolean isASC() {
		return this.ASC;
	}

	public void setASC(boolean ASC) {
		this.ASC = ASC;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof NodeInfo && obj2 instanceof NodeInfo) {
			NodeInfo n1 = (NodeInfo) obj1;
			NodeInfo n2 = (NodeInfo) obj2;

			String name1 = n1.getName();
			String name2 = n2.getName();
			if (name1 == null) {
				name1 = "";
			}
			if (name2 == null) {
				name2 = "";
			}

			if (this.ASC) {
				return name1.compareTo(name2);
			} else {
				return name2.compareTo(name1);
			}
		}
		return 0;
	}

}
