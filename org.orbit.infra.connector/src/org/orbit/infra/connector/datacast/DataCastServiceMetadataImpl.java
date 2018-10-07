package org.orbit.infra.connector.datacast;

import org.orbit.infra.api.datacast.DataCastServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class DataCastServiceMetadataImpl extends ServiceMetadataImpl implements DataCastServiceMetadata {

	public static String PROP__DATA_CAST_ID = "data_cast_id";

	@Override
	public String getDataCastId() {
		String dataCastId = null;
		if (hasProperty(PROP__DATA_CAST_ID)) {
			dataCastId = getProperty(PROP__DATA_CAST_ID, String.class);
		}
		return dataCastId;
	}

}
