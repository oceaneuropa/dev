package org.orbit.infra.io.util;

import org.orbit.infra.io.configregistry.IConfigElement;
import org.orbit.infra.io.configregistry.IConfigRegistry;
import org.origin.common.util.BaseComparator;
import org.origin.common.util.NameUtil;

public class ClientComparators {

	public static ConfigRegistryComparator ConfigRegistryComparator_ASC = new ConfigRegistryComparator(BaseComparator.SORT_ASC);
	public static ConfigRegistryComparator ConfigRegistryComparator_DESC = new ConfigRegistryComparator(BaseComparator.SORT_DESC);

	public static ConfigElementComparator ConfigElementComparator_ASC = new ConfigElementComparator(BaseComparator.SORT_ASC);
	public static ConfigElementComparator ConfigElementComparator_DESC = new ConfigElementComparator(BaseComparator.SORT_DESC);

	public static class ConfigRegistryComparator extends BaseComparator<IConfigRegistry> {

		public ConfigRegistryComparator(String sort) {
			super(sort);
		}

		@Override
		public int compare(IConfigRegistry configReg1, IConfigRegistry configReg2) {
			String type1 = configReg1.getType();
			String type2 = configReg2.getType();
			String name1 = configReg1.getName();
			String name2 = configReg2.getName();

			if (!type1.equals(type2)) {
				return NameUtil.compareStringWithSuffixNum(type1, type2, asc());
			} else {
				return NameUtil.compareStringWithSuffixNum(name1, name2, asc());
			}
		}
	}

	public static class ConfigElementComparator extends BaseComparator<IConfigElement> {

		public ConfigElementComparator(String sort) {
			super(sort);
		}

		@Override
		public int compare(IConfigElement configElement1, IConfigElement configElement2) {
			String name1 = configElement1.getName();
			String name2 = configElement2.getName();

			return NameUtil.compareStringWithSuffixNum(name1, name2, asc());
		}
	}

}
