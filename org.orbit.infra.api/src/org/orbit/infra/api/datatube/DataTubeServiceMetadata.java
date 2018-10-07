package org.orbit.infra.api.datatube;

import org.origin.common.rest.model.ServiceMetadata;

public interface DataTubeServiceMetadata extends ServiceMetadata {

	String getDataCastId();

	String getDataTubeId();

}
