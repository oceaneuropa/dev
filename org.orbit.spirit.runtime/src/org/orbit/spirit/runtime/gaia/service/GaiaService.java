package org.orbit.spirit.runtime.gaia.service;

import org.orbit.spirit.runtime.gaia.world.Worlds;
import org.origin.common.jdbc.ConnectionAware;
import org.origin.common.rest.editpolicy.EditPoliciesAwareService;
import org.origin.common.service.PropertiesAware;
import org.origin.common.service.WebServiceAware;

public interface GaiaService extends WebServiceAware, PropertiesAware, ConnectionAware, EditPoliciesAwareService {

	Worlds getWorlds();

}
