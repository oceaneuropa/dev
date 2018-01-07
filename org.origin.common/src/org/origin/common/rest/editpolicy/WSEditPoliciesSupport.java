package org.origin.common.rest.editpolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.origin.common.rest.model.Request;

public class WSEditPoliciesSupport implements WSEditPolicies {

	protected Map<Class<?>, Object> servicesMap = new HashMap<Class<?>, Object>();
	protected List<WSEditPolicy> editPolicies = new ArrayList<WSEditPolicy>();

	@Override
	public Class<?>[] getServiceClasses() {
		return this.servicesMap.keySet().toArray(new Class<?>[this.servicesMap.size()]);
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S> S getService(Class<S> clazz) {
		S service = null;
		Object object = this.servicesMap.get(clazz);
		if (object != null && clazz.isAssignableFrom(object.getClass())) {
			service = (S) object;
		}
		return service;
	}

	@Override
	public void setService(Class<?> clazz, Object service) {
		if (service != null) {
			this.servicesMap.put(clazz, service);
		} else {
			this.servicesMap.remove(clazz);
		}
	}

	@Override
	public synchronized List<WSEditPolicy> getEditPolicies() {
		return this.editPolicies;
	}

	@Override
	public synchronized WSEditPolicy getEditPolicy(String id) {
		if (id == null) {
			return null;
		}
		WSEditPolicy editPolicy = null;
		for (WSEditPolicy currEditPolicy : this.editPolicies) {
			if (id.equals(currEditPolicy.getId())) {
				editPolicy = currEditPolicy;
				break;
			}
		}
		return editPolicy;
	}

	@Override
	public synchronized boolean installEditPolicy(WSEditPolicy editPolicy) {
		if (editPolicy == null || this.editPolicies.contains(editPolicy)) {
			return false;
		}

		// edit policy with same id already exists
		String id = editPolicy.getId();
		WSEditPolicy existingEditPolicy = getEditPolicy(id);
		if (existingEditPolicy != null) {
			System.err.println(WSEditPoliciesSupport.class.getSimpleName() + ".installEditPolicy(WSEditPolicy) WSEditPolicy with id '" + id + "' already exists.");
			return false;
		}

		return this.editPolicies.add(editPolicy);
	}

	@Override
	public synchronized boolean uninstallEditPolicy(WSEditPolicy editPolicy) {
		if (editPolicy == null || !this.editPolicies.contains(editPolicy)) {
			return false;
		}
		return this.editPolicies.remove(editPolicy);
	}

	@Override
	public synchronized WSEditPolicy uninstallEditPolicy(String id) {
		if (id == null) {
			return null;
		}
		WSEditPolicy editPolicy = null;
		for (WSEditPolicy currEditPolicy : this.editPolicies) {
			if (id.equals(currEditPolicy.getId())) {
				editPolicy = currEditPolicy;
				break;
			}
		}
		if (editPolicy != null) {
			this.editPolicies.remove(editPolicy);
		}
		return editPolicy;
	}

	@Override
	public synchronized WSCommand getCommand(Request request) {
		WSCommand command = null;
		for (WSEditPolicy editPolicy : this.editPolicies) {
			WSCommand currCommand = editPolicy.getCommand(request);
			if (currCommand != null) {
				command = currCommand;
				break;
			}
		}
		return command;
	}

}
