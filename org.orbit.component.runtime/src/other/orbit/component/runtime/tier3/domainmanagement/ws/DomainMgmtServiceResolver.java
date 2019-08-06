package other.orbit.component.runtime.tier3.domainmanagement.ws;

import javax.ws.rs.ext.ContextResolver;

import org.orbit.component.runtime.ComponentServices;
import org.orbit.component.runtime.tier3.domain.service.DomainManagementService;

public class DomainMgmtServiceResolver implements ContextResolver<DomainManagementService> {

	@Override
	public DomainManagementService getContext(Class<?> clazz) {
		return ComponentServices.getInstance().getDomainService();
	}

}
