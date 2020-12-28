package org.orbit.component.api.util;

import org.orbit.component.api.ComponentConstants;
import org.origin.common.model.AbstractConfigPropertiesHandler;

/**
 * 
 * @author <a href="mailto:yangyang4j@gmail.com">Yang Yang</a>
 *
 */
public class ComponentsConfigPropertiesHandler extends AbstractConfigPropertiesHandler {

	protected static ComponentsConfigPropertiesHandler INSTANCE = new ComponentsConfigPropertiesHandler();

	public static ComponentsConfigPropertiesHandler getInstance() {
		return INSTANCE;
	}

	@Override
	public String[] getConfigPropertyNames() {
		String[] propNames = new String[] { //
				ComponentConstants.ORBIT_DATACAST_URL, //
				ComponentConstants.ORBIT_WEB_CONSOLE_URL, //
				ComponentConstants.ORBIT_IDENTITY_SERVICE_URL, //
				ComponentConstants.ORBIT_USER_ACCOUNTS_URL, //
				ComponentConstants.ORBIT_AUTH_URL, //
				ComponentConstants.ORBIT_APP_STORE_URL, //
				ComponentConstants.ORBIT_DOMAIN_SERVICE_URL, //
				ComponentConstants.ORBIT_NODE_CONTROL_URL, //
				ComponentConstants.ORBIT_MISSION_CONTROL_URL, //
		};
		return propNames;
	}

	/**
	 * Getting config property for DataCast service URL. This can only be done for such services which are know and common.
	 * 
	 * For services that could be introduced in the future, which can be dynamically deployed to arbitrary nodes (so their service URL is dynamic and could even be changed dynamically between accessing of the service), need mechanism to dynamically discover such services.
	 * 
	 * This could be done to look up service URL every time from service publisher (e.g. using the indexing service).
	 * 
	 * Or this could be done by caching the service URL and then look up the service URL again (using unique service id or service name) as needed (e.g. when service 404 or using notification when service URL is changed --- need remote notification mechanism.)
	 * 
	 * @return
	 */
	public String getDataCastURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_DATACAST_URL);
		return serviceUrl;
	}

	public String getIdentityServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_IDENTITY_SERVICE_URL);
		return serviceUrl;
	}

	public String getUserAcountServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_USER_ACCOUNTS_URL);
		return serviceUrl;
	}

	public String getAuthServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_AUTH_URL);
		return serviceUrl;
	}

	public String getAppStoreURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_APP_STORE_URL);
		return serviceUrl;
	}

	public String getDomainServiceURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_DOMAIN_SERVICE_URL);
		return serviceUrl;
	}

	public String getNodeControlURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_NODE_CONTROL_URL);
		return serviceUrl;
	}

	public String getMissionControlURL() {
		String serviceUrl = getProperty(ComponentConstants.ORBIT_MISSION_CONTROL_URL);
		return serviceUrl;
	}

}
