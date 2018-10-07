package org.orbit.infra.connector.datatube;

import org.orbit.infra.api.datatube.DataTubeServiceMetadata;
import org.origin.common.rest.model.ServiceMetadataImpl;

public class DataTubeServiceMetadataImpl extends ServiceMetadataImpl implements DataTubeServiceMetadata {

	public static String PROP__DATA_CAST_ID = "data_cast_id";
	public static String PROP__DATA_TUBE_ID = "data_tube_id";

	@Override
	public String getDataCastId() {
		String dataCastId = null;
		if (hasProperty(PROP__DATA_CAST_ID)) {
			dataCastId = getProperty(PROP__DATA_CAST_ID, String.class);
		}
		return dataCastId;
	}

	@Override
	public String getDataTubeId() {
		String dataTubeId = null;
		if (hasProperty(PROP__DATA_TUBE_ID)) {
			dataTubeId = getProperty(PROP__DATA_TUBE_ID, String.class);
		}
		return dataTubeId;
	}

}
