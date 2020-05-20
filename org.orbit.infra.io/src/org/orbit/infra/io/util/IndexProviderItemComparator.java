package org.orbit.infra.io.util;

import java.util.Comparator;

import org.orbit.infra.api.indexes.IndexProviderItem;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class IndexProviderItemComparator implements Comparator<Object> {

	protected boolean ASC = true;

	public IndexProviderItemComparator() {
	}

	public boolean isASC() {
		return this.ASC;
	}

	public void setASC(boolean ASC) {
		this.ASC = ASC;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof IndexProviderItem && obj2 instanceof IndexProviderItem) {
			IndexProviderItem item1 = (IndexProviderItem) obj1;
			IndexProviderItem item2 = (IndexProviderItem) obj2;

			String name1 = item1.getName();
			String name2 = item2.getName();
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
