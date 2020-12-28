package org.orbit.component.runtime.tier1.session.service;

import org.origin.common.rest.editpolicy.ServiceEditPolicies;
import org.osgi.framework.BundleContext;

public class OAuth2ServiceImpl implements OAuth2Service {

	@Override
	public String getAccessToken() {
		return null;
	}

	@Override
	public void start(BundleContext bundleContext) throws Exception {

	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {

	}

	/** IWebService */
	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getHostURL() {
		return null;
	}

	@Override
	public String getContextRoot() {
		return null;
	}

	@Override
	public ServiceEditPolicies getEditPolicies() {
		return null;
	}

}
