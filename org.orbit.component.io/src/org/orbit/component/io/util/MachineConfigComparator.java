package org.orbit.component.io.util;

import java.util.Comparator;

import org.orbit.component.api.tier3.domain.MachineConfig;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class MachineConfigComparator implements Comparator<Object> {

	protected boolean ASC = true;

	public MachineConfigComparator() {
	}

	public boolean isASC() {
		return this.ASC;
	}

	public void setASC(boolean ASC) {
		this.ASC = ASC;
	}

	@Override
	public int compare(Object obj1, Object obj2) {
		if (obj1 instanceof MachineConfig && obj2 instanceof MachineConfig) {
			MachineConfig m1 = (MachineConfig) obj1;
			MachineConfig m2 = (MachineConfig) obj2;

			String name1 = m1.getName();
			String name2 = m2.getName();
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
