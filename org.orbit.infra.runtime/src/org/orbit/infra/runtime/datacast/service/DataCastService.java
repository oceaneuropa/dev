package org.orbit.infra.runtime.datacast.service;

import java.util.List;

import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface DataCastService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	String getDataCastId();

	List<DataTubeMetadata> getDataTubes();

}
