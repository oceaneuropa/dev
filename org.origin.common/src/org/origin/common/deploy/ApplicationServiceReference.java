package org.origin.common.deploy;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.osgi.service.http.HttpService;

public class ApplicationServiceReference {

	protected String contextRoot;
	protected Application application;
	protected Set<HttpService> deployedToHttpServices = new LinkedHashSet<HttpService>();

	/**
	 * 
	 * @param contextRoot
	 * @param application
	 */
	public ApplicationServiceReference(String contextRoot, Application application) {
		this.contextRoot = contextRoot;
		this.application = application;
	}

	public Application getApplication() {
		return application;
	}

	public String getContextRoot() {
		return contextRoot;
	}

	/**
	 * 
	 * @return
	 */
	public Set<HttpService> getDeployedHttpServices() {
		return deployedToHttpServices;
	}

	/**
	 * 
	 * @param httpService
	 * @return
	 */
	public boolean isDeployedTo(HttpService httpService) {
		if (httpService != null && deployedToHttpServices.contains(httpService)) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param httpService
	 */
	public void setDeployedTo(HttpService httpService) {
		if (httpService != null) {
			deployedToHttpServices.add(httpService);
		}
	}

	/**
	 * 
	 * @param httpService
	 */
	public void unsetDeployedTo(HttpService httpService) {
		if (httpService != null && deployedToHttpServices.contains(httpService)) {
			deployedToHttpServices.remove(httpService);
		}
	}

}
