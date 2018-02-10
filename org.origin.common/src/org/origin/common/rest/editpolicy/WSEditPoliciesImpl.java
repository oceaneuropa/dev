package org.origin.common.rest.editpolicy;

import java.util.List;

import org.origin.common.rest.model.Request;

public class WSEditPoliciesImpl implements WSEditPolicies {

	protected WSEditPoliciesSupport editPoliciesSupport = new WSEditPoliciesSupport();

	@Override
	public Class<?>[] getServiceClasses() {
		return this.editPoliciesSupport.getServiceClasses();
	}

	@Override
	public <S> S getService(Class<S> clazz) {
		return this.editPoliciesSupport.getService(clazz);
	}

	@Override
	public void setService(Class<?> clazz, Object service) {
		this.editPoliciesSupport.setService(clazz, service);
	}

	@Override
	public List<WSEditPolicy> getEditPolicies() {
		return this.editPoliciesSupport.getEditPolicies();
	}

	@Override
	public WSEditPolicy getEditPolicy(String id) {
		return this.editPoliciesSupport.getEditPolicy(id);
	}

	@Override
	public boolean installEditPolicy(WSEditPolicy editPolicy) {
		// Inject the services to the WSEditPolicy
		configure(editPolicy);

		return this.editPoliciesSupport.installEditPolicy(editPolicy);
	}

	@Override
	public boolean uninstallEditPolicy(WSEditPolicy editPolicy) {
		boolean succeed = this.editPoliciesSupport.uninstallEditPolicy(editPolicy);
		if (succeed) {
			deconfigure(editPolicy);
		}
		return succeed;
	}

	@Override
	public WSEditPolicy uninstallEditPolicy(String id) {
		WSEditPolicy editPolicy = this.editPoliciesSupport.uninstallEditPolicy(id);
		if (editPolicy != null) {
			deconfigure(editPolicy);
		}
		return editPolicy;
	}

	protected void configure(WSEditPolicy editPolicy) {
		Class<?>[] serviceClasses = getServiceClasses();
		if (serviceClasses != null) {
			for (Class<?> serviceClass : serviceClasses) {
				Object service = getService(serviceClass);
				if (service != null) {
					editPolicy.setService(serviceClass, service);
				}
			}
		}
	}

	protected void deconfigure(WSEditPolicy editPolicy) {
		Class<?>[] serviceClasses = getServiceClasses();
		if (serviceClasses != null) {
			for (Class<?> serviceClass : serviceClasses) {
				Object service = editPolicy.getService(serviceClass);
				if (service != null) {
					editPolicy.setService(serviceClass, null);
				}
			}
		}
	}

	@Override
	public WSCommand getCommand(Request request) {
		return this.editPoliciesSupport.getCommand(request);
	}

}
