package org.orbit.infra.api.util;

import java.util.Comparator;

import org.orbit.infra.api.datacast.ChannelMetadata;
import org.origin.common.util.BaseComparator;

public class ChannelMetadataComparator extends BaseComparator<ChannelMetadata> implements Comparator<ChannelMetadata> {

	public static ChannelMetadataComparator ASC = new ChannelMetadataComparator(SORT_ASC);
	public static ChannelMetadataComparator DESC = new ChannelMetadataComparator(SORT_DESC);

	/**
	 * 
	 * @param sort
	 */
	public ChannelMetadataComparator(String sort) {
		super(sort);
	}

	@Override
	public int compare(ChannelMetadata channel1, ChannelMetadata channel2) {
		String dataTube1 = channel1.getDataTubeId();
		String dataTube2 = channel2.getDataTubeId();
		String name1 = channel1.getName();
		String name2 = channel2.getName();

		if (dataTube1 == null) {
			dataTube1 = "";
		}
		if (dataTube2 == null) {
			dataTube2 = "";
		}
		if (name1 == null) {
			name1 = "";
		}
		if (name2 == null) {
			name2 = "";
		}

		if (desc()) {
			if (!dataTube2.equals(dataTube1)) {
				return dataTube2.compareTo(dataTube1);
			} else {
				return name2.compareTo(name1);
			}
		} else {
			if (!dataTube1.equals(dataTube2)) {
				return dataTube1.compareTo(dataTube2);
			} else {
				return name1.compareTo(name2);
			}
		}
	}

}
